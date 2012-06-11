package com.monits.anviz.net.packet.payload;

import com.monits.packer.annotation.DependsOn;
import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.Unsigned;

public class AttendanceList {
	
	@Encode(0)
	@Unsigned
	private short count;
	
	@Encode(1)
	@DependsOn("count")
	private AttendanceRecord records[];

	public short getCount() {
		return count;
	}

	public void setCount(short count) {
		this.count = count;
	}

	public AttendanceRecord[] getRecords() {
		return records;
	}

	public void setRecords(AttendanceRecord[] records) {
		this.records = records;
	}
	
}
