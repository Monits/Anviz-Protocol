package com.monits.anviz.net.packet;

public class UserListRequest extends Command {

	public static final int MAX_RESULTS = 0x0C;

	public UserListRequest(boolean first) {
		super((short) 0x72, new byte[2]);
		
		if (first) {
			payload[0] = 0x01;
		} else {
			payload[0] = 0x00;
		}
		
		payload[1] = MAX_RESULTS;
	}

}
