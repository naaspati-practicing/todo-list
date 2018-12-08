package sam.todo.mvc;

import sam.todo.dao.Store;
import spark.Request;
import spark.Response;
import spark.Route;

public class Renderer {
	private final Store store;

	public Renderer(Store store) {
		this.store = store;
	}

	
	public Route renderEditor(Request req, Response res) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Route renderOne(Request req, Response res) {
		// TODO Auto-generated method stub
		return null;
	}
}
