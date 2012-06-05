package com.monits.anviz.model;

import org.joda.time.DateTime;

public abstract class Action {

	private short id;
	
	private DateTime time;

	public Action(short id, DateTime time) {
		super();
		this.id = id;
		this.time = time;
	}

	public short getId() {
		return id;
	}

	public DateTime getTime() {
		return time;
	}
	
}
