package com.monits.anviz.model;

import org.joda.time.DateTime;

public class CheckIn extends Action {

	public CheckIn(long id, DateTime time) {
		super(id, time);
	}

	@Override
	public String toString() {
		return "CheckIn [id=" + id + ", time=" + time + "]";
	}
	
}
