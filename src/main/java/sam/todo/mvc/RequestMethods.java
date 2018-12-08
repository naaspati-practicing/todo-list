package sam.todo.mvc;

import static sam.todo.dao.ToDo.COMPLETED;
import static sam.todo.dao.ToDo.CONTENT;
import static sam.todo.dao.ToDo.ID;
import static sam.todo.dao.ToDo.TITLE;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes;
import org.json.JSONArray;
import org.json.JSONObject;

import sam.todo.dao.Store;
import sam.todo.dao.ToDo;
import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class RequestMethods {
	private final Store store;
	

	public RequestMethods(Store store) {
		this.store = store;
	}

	public String put(Request req, Response res) {
		JSONObject json = json(req, res);
		if(json == null) return null;
		
		ToDo todo = store.put(string(json, TITLE), string(json, CONTENT));
		json.put(ID, todo.getId());

		return json.toString();
	}
	private String string(JSONObject json, String key) {
		return json.has(key) ? json.getString(key) : null;
	}
	private JSONObject json(Request req, Response res) {
		String s = req.body();
		if(res != null)
			res.header(HttpHeader.CONTENT_TYPE.asString(), MimeTypes.Type.APPLICATION_JSON.asString());
		return StringUtils.isBlank(s) ? null : new JSONObject(s);
	}
	private int id(Request req) {
		return Integer.parseInt(req.params(ID));
	}
	public String delete(Request req, Response res) {
		String s = req.params(ID);
		if(s.equals(COMPLETED))
			return deleteCompleted(req, res);
		int id = id(req);
		store.remove(id);
		return "{\""+ID+"\":"+id+", \"deleted\":true}";
	}

	public String deleteCompleted(Request req, Response res) {
		return "{\"ids\":"+store.removeCompleted()+"}";
	}
	/**
	 * modify existing entry
	 * @param req
	 * @param res
	 * @return
	 */
	public String post(Request req, Response res) {
		int id = id(req);
		
		JSONObject json = json(req, res);
		if(json == null) return null;
		
		ToDo todo = store.get(id);
		if(json.has(TITLE))
			todo.setTitle(json.getString(TITLE));
		if(json.has(CONTENT))
			todo.setContent(json.getString(CONTENT));
		if(json.has(COMPLETED))
			todo.setCompleted(json.getBoolean(COMPLETED));
		
		json.put("modified", true);
		return json.toString();
	}
	public String toggleStatusById(Request req, Response res) {
		int id = id(req);
		ToDo todo = store.get(id);
		if(todo == null)
			return null;
		
		JSONObject json = json(req, res);
		todo.setCompleted(json.getBoolean(COMPLETED));
		
		return todo.toString();
	}
	public String toggleStatusAll(Request req, Response res) {
		JSONObject json = json(req, res);
		if(json == null) return null;
		
		if(json.has("all")) {
			boolean b = json.getBoolean("all");
			store.forEach(t -> t.setCompleted(b));
		}
		if(json.has(COMPLETED)) {
			JSONArray b = json.getJSONArray(COMPLETED);
			for (int i = 0; i < b.length(); i++) {
				ToDo t = store.get(b.getInt(i));
				if(t != null)
					t.setCompleted(true);
			}
		}
		String key = "not-"+COMPLETED;
		if(json.has(key)) {
			JSONArray b = json.getJSONArray(key);
			
			for (int i = 0; i < b.length(); i++) {
				ToDo t = store.get(b.getInt(i));
				if(t != null)
					t.setCompleted(false);
			}
		}
		return null;
	}


}
