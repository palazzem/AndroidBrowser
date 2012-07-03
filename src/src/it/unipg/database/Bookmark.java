package it.unipg.database;

public class Bookmark {
	// Model field
	private long id;
	private String url;
	
	// Getter and Setter
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return url;
	}
}
