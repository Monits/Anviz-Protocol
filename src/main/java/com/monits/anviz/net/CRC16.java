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
