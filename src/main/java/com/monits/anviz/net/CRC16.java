package com.monits.anviz.net;

public class CRC16 {
	
	private CRC16() {
	}
	
	public static int calculateCCITT(byte[] data) {
		
		// Implementation taken from http://introcs.cs.princeton.edu/java/51data/CRC16CCITT.java.html
		
		int crc = 0xFFFF;
        int polynomial = 0x1021; 

        for (byte b : data) {
        	
            for (int i = 0; i < 8; i++) {
                boolean bit = (b >> (7 - i) & 1) == 1;
                boolean c15 = (crc >> 15 & 1) == 1;
                
                crc <<= 1;
                
                if (c15 ^ bit) {
                	crc ^= polynomial;
                }
             }
        }

        return crc & 0xFFFF;
	}

}
