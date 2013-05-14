package it.unitn.vanguard.reminiscence;


public class Friend {

	private String name;
	private String surname;
	private int id;
	private String email;
	private boolean request = false;

	public Friend(String name, String surname, String email, int id) {
		super();
		this.name = name;
		this.surname = surname;
		this.id = id;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isRequest() {
		return request;
	}

	public void setRequest(boolean request) {
		this.request = request;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
