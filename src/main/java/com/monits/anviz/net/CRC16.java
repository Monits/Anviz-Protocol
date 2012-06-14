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

public class CRC16 {
	
	private static final int[] table = new int[256];
	static {
		int polynomial = 0x8408;
		
		for (int b = 0; b < 256; b++) {
			
			int crc = b;
			for (int i = 0; i < 8; i++) {
	            crc = (crc >> 1) ^ ((crc & 1) == 1 ? polynomial : 0);
	        }
			
			table[b] = 0xFFFF & crc; 
		}
		
		
	}
	
	private CRC16() {
	}
	
	public static int calculateCCITT(byte[] data) {
		
		int crc = 0xFFFF;
        for (byte b : data) {
        	crc = (crc >> 8) ^ table[(crc ^ b) & 0xFF];
        }

        return crc & 0xFFFF;
	}

}
