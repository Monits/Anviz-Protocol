package com.monits.anviz.net.packet;

public class AttendanceRecordsRequest extends Command {
	
	public static final int MAX_RESULTS = 0x19;

	public AttendanceRecordsRequest(boolean first) {
		super((short) 0x40, new byte[2]);
		
		if (first) {
			payload[0] = 0x01;
		} else {
			payload[0] = 0x00;
		}
		
		payload[1] = MAX_RESULTS;
	}

}
