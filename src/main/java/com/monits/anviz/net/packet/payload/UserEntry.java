package com.monits.anviz.net.packet.payload;

import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.FixedLength;
import com.monits.packer.annotation.Unsigned;

public class UserEntry {

	@Encode(0)
	@FixedLength(4)
	private byte[] unknown0;

	@Encode(1)
	@Unsigned
	private short userId;
	
	@Encode(2)
	@FixedLength(7)
	private byte[] unknown1;
	
	@Encode(3)
	@FixedLength(10)
	private String name;
	
	@Encode(4)
	@FixedLength(8)
	private byte[] uknown2;

	public byte[] getUnknown0() {
		return unknown0;
	}

	public void setUnknown0(byte[] unknown0) {
		this.unknown0 = unknown0;
	}

	public short getUserId() {
		return userId;
	}

	public void setUserId(short userId) {
		this.userId = userId;
	}

	public byte[] getUnknown1() {
		return unknown1;
	}

	public void setUnknown1(byte[] unknown1) {
		this.unknown1 = unknown1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getUknown2() {
		return uknown2;
	}

	public void setUknown2(byte[] uknown2) {
		this.uknown2 = uknown2;
	}
	
}
