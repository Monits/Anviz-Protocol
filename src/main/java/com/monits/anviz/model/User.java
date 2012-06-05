package com.monits.anviz.model;

public class User {
	
	private short id;
	
	private String name;

	public User(short id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}
