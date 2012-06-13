package com.monits.anviz.net.packet.payload;

import com.monits.anviz.codec.ThreeByteCodec;
import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.UseCodec;

public class PurgeResponse {
	
	@Encode(0)
	@UseCodec(ThreeByteCodec.class)
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
