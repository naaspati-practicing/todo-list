package sam.todo.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


public class Store {
	private static final Logger LOGGER = Log.getLogger(Store.class);
	

	private int IDS = 1;
	private final Map<Integer, ToDo> map = new ConcurrentHashMap<>();
	
	public Store() {
		put("dummy-1-title", "dummy-1-content");
		put("dummy-2-title", "dummy-2-content");
		put("dummy-3-title", "dummy-3-content");
	}

	public ToDo put(String title, String content) {
		ToDo todo = new ToDo(IDS++, title, content);

		map.put(todo.getId(), todo);
		LOGGER.debug("ADD: {}", todo);
		return todo;
	}

	public ToDo remove(int id) {
		ToDo t = map.remove(id);
		LOGGER.debug("REMOVED: {}", t);
		return t;
	}
	public List<Integer> removeCompleted() {
		if(map.values().stream().anyMatch(ToDo::isCompleted)) {
			List<Integer> list = new ArrayList<>();
			map.values().removeIf(t -> {
				if(t.isCompleted()) {
					list.add(t.getId());
					LOGGER.debug("REMOVED: {}", t);
					return true;
				}
				return false;
			});
			return list;
		}
		return Collections.emptyList();
	}
	
	public ToDo get(int id) {
		return map.get(id);
	}
	public void forEach(Consumer<ToDo> action) {
		map.forEach((s,t) -> action.accept(t));
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Stream<ToDo> stream() {
		return map.values().stream();
	}
}
