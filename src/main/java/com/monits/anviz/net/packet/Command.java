package com.monits.anviz.net.packet;

public class Command {
	
	private short command;

	protected byte[] payload;

	public Command(short command, byte[] payload) {
		super();
		this.command = command;
		this.payload = payload;
	}

	public short getCommand() {
		return command;
	}

	public byte[] getPayload() {
		return payload;
	}
	
}
