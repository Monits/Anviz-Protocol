package com.monits.anviz.codec;

import java.io.IOException;

import com.monits.packer.codec.Codec;
import com.monits.packer.streams.InputByteStream;
import com.monits.packer.streams.OutputByteStream;

public class ThreeByteCodec implements Codec<Integer> {
	
	@Override
	public boolean encode(OutputByteStream payload, Integer object, Object[] dependants) {
		long val = object;
		
		payload.putByte((byte) ((0xFF0000 & val) >> 16));
		payload.putByte((byte) ((0x00FF00 & val) >> 8));
		payload.putByte((byte)  (0x0000FF & val));
		
		return true;
	}

	@Override
	public Integer decode(InputByteStream payload, Object[] dependants) throws IOException {
		
		byte[] data = payload.getBytes(3);
		
		return ((((int) data[0]) & 0xFF) << 16)
			| ((((int) data[1]) & 0xFF) << 8)
			| (((int) data[2]) & 0xFF);
	}

}
