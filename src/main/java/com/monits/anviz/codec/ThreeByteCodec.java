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
package com.monits.anviz.codec;

import java.io.IOException;

import com.monits.jpack.codec.Codec;
import com.monits.jpack.streams.InputByteStream;
import com.monits.jpack.streams.OutputByteStream;

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
