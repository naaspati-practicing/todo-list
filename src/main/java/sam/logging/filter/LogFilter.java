package sam.logging.filter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

public class LogFilter implements Filter {
	private static final Set<String> notAllowed;
	private static final Set<String> loggernames;

	static {
		try {
			Path p = Paths.get("logging-not-allowed.txt");
			if(Files.notExists(p))
				notAllowed = null;
			else {

				notAllowed = Files.lines(p).map(String::trim).collect(Collectors.toSet());

			}

			if(!"true".equalsIgnoreCase(System.getenv("LOGGER_NAMES")))
				loggernames = null;
			else {
				p = Paths.get("allowed-logger-names.txt");
				if(Files.exists(p))
					loggernames = Files.lines(p).collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
				else
					loggernames = new HashSet<>();
				int size = loggernames.size();
				Path p2 = p;
				
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					if(size == loggernames.size()) return;
					try {
						Files.write(p2, loggernames, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
						System.out.println("SAVED: "+p2);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		if(notAllowed == null)
			return true;
		
		boolean b = !notAllowed.contains(record.getLoggerName());
		if(b && loggernames != null) {
			synchronized (loggernames) {
				loggernames.add(record.getLoggerName());
			}
		}
		return b;
	}

}
