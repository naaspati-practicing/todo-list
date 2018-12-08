
import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;
import static spark.Spark.stop;

import java.util.Scanner;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpHeaderValue;
import org.eclipse.jetty.http.MimeTypes;
import org.json.JSONArray;

import sam.todo.dao.Store;
import sam.todo.dao.ToDo;
import sam.todo.mvc.Renderer;
import sam.todo.mvc.RequestMethods;
import spark.staticfiles.MimeType;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		port(8080);
		staticFiles.location("/public");
		
		before("*", (req, res) -> {
			String s = req.uri();
			if(s.length() > 1 && s.charAt(s.length() - 1) == '/')
				res.redirect(s.substring(0, s.length() - 1));
		});
		
		final Store store = new Store();
		final RequestMethods methods = new RequestMethods(store);
		final Renderer renderer = new Renderer(store); 
		
		get("/", (req, res) -> {
			res.redirect("/index.html");
			return null;
		});
		get("/all", MimeTypes.Type.TEXT_JSON.asString(),  (req, res) -> {
			res.header(HttpHeader.CONTENT_TYPE.asString(), MimeTypes.Type.APPLICATION_JSON.asString());
			if(store.isEmpty())
				return "[]";
			
			JSONArray array = new JSONArray();
			store.forEach(t -> array.put(t.toJsonObject()));
			return array.toString();
		});
		
		path("todos", () -> {
			path("/:id", () -> {
				get("", renderer::renderOne);
				get("/edit", renderer::renderEditor);
				
				delete("", methods::delete);
				post("", methods::post);
				post("/toggle_status", methods::toggleStatusById);
			});
			delete("/completed", methods::deleteCompleted);
			post("/toggle_status", methods::toggleStatusAll);
			
			put("", methods::put);
		});
		
		awaitInitialization();
		Thread.sleep(200);
		waitForExit();
		stop();
	}

	private static void waitForExit() {
		System.out.println("\n\ntype --exit to exit....");
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while(true) {
			if(sc.nextLine().trim().equalsIgnoreCase("--exit"))
				break;
		}
	}

}
