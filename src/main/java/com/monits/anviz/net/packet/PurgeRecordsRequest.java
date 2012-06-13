package com.monits.anviz.net.packet;

public class PurgeRecordsRequest extends Command {
	
	protected PurgeRecordsRequest(byte code, int limit) {
		super((short) 0x4E, new byte[4]);
		payload[0] = code;
		
		payload[1] = (byte) ((0x00FF0000 & limit) >> 16);
		payload[2] = (byte) ((0x0000FF00 & limit) >> 8);
		payload[3] = (byte) (0x000000FF & limit);
	}
	
	public PurgeRecordsRequest(int limit) {
		this((byte) 0x02, limit);
		
	}
	
	public PurgeRecordsRequest(boolean all) {
		this((byte) (all ? 0x00 : 0x01), 0);
	}

}
