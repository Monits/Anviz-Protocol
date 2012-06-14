/*
 *
 * Copyright 2012 Monits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.monits.anviz.net;

import java.io.IOException;
import java.io.InputStream;

import com.monits.jpack.streams.InputByteStream;

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
