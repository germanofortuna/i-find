package br.com.ifind;

public class Greeting {
	private final String content;
	private final long id;
	
	public Greeting(String content, long id) {
		super();
		this.content = content;
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public long getId() {
		return id;
	}
		
}
