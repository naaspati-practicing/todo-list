package sam.todo.dao;

import java.util.Objects;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.json.JSONObject;

public class ToDo {
	private static final Logger LOGGER = Log.getLogger(ToDo.class);
	
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String COMPLETED = "completed";
	
	private final int id;
	private String title;
	private String content;
	private boolean completed;
	
	public ToDo(int id, String title, String content, boolean completed) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.completed = completed;
	}
	public ToDo(int id) {
		this.id = id;
	}
	public ToDo(int id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		if(LOGGER.isDebugEnabled() && !Objects.equals(this.title, title))
			LOGGER.debug("SET_TITLE: {} -> {}", this.title, title);

		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		if(LOGGER.isDebugEnabled() && !Objects.equals(this.content, content))
			LOGGER.debug("SET_CONTENT: {} -> {}", this.content, content);

		this.content = content;
	}
	public int getId() {
		return id;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		if(LOGGER.isDebugEnabled() && this.completed != completed)
			LOGGER.debug("SET_COMPLETED: {} -> {}", this.completed, completed);
		
		this.completed = completed;
	}
	public JSONObject toJsonObject() {
		JSONObject obj = new JSONObject();
		obj.put(ID, id);
		obj.put(TITLE, title);
		obj.put(CONTENT, content);
		obj.put(COMPLETED, completed);
		
		return obj;
	}
	@Override
	public String toString() {
		return "ToDo [id=" + id + ", title=" + title + ", completed=" + completed + "]";
	} 
}
