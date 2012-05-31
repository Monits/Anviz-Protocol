package com.monits.anviz.net;

import java.io.IOException;
import java.io.InputStream;

import com.monits.packer.streams.InputByteStream;

public class PackerInputStream implements InputByteStream {
	
	private InputStream is;
	
	private Byte peek;
	
	public PackerInputStream(InputStream is) {
		super();
		this.is = is;
	}

	@Override
	public byte peek() throws IOException {
		
		if (peek == null) {
			peek = getByte();
		}
		
		return peek;
	}

	@Override
	public byte[] getBytes(int count) throws IOException {
		
		int offset = 0;
		byte[] data = new byte[count];
		if (peek != null) {
			data[0] = peek;
			offset++;
			
			peek = null;
		}
		
		if (is.read(data, offset, count - offset) == -1) {
			throw new IOException(); 
		}
		
		return data;
	}

	@Override
	public byte getByte() throws IOException {
		
		if (peek != null) {
			byte res = peek;
			peek = null;
			
			return res;
		}
		
		int res = is.read();
		if (res == -1) {
			throw new IOException();
		}
		
		return (byte) res;
	}

}
