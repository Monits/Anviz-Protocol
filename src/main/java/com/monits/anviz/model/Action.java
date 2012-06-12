package com.monits.anviz.model;

import org.joda.time.DateTime;

public abstract class Action {

	protected long id;
	
	protected DateTime time;

	public Action(long id, DateTime time) {
		super();
		this.id = id;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public DateTime getTime() {
		return time;
	}
	
}
