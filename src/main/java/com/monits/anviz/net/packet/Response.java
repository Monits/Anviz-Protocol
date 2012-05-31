package com.monits.anviz.net.packet;

import com.monits.packer.annotation.DependsOn;
import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.Unsigned;

public class Response {
	
	@Encode(0)
	@Unsigned
	private short magic;
	
	@Encode(1)
	@Unsigned
	private long deviceCode;
	
	@Encode(2)
	@Unsigned
	private short command;
	
	@Encode(3)
	@Unsigned
	private int length;
	
	@Encode(4)
	@Unsigned
	@DependsOn({ "length" })
	private short[] data;
	
	@Encode(5)
	private byte checksumLow;

	@Encode(6)
	private byte checksumHigh;
}
