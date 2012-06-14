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
package com.monits.anviz.net.packet;

public class PurgeRecordsRequest extends Command {
	
	protected PurgeRecordsRequest(byte code, int limit) {
		super((short) 0x4E, new byte[4]);
		payload[0] = code;
		
		payload[1] = (byte) ((0x00FF0000 & limit) >> 16);
		payload[2] = (byte) ((0x0000FF00 & limit) >> 8);
		payload[3] = (byte) (0x000000FF & limit);
	}
	
	public PurgeRecordsRequest(int limit) {
		this((byte) 0x02, limit);
		
	}
	
	public PurgeRecordsRequest(boolean all) {
		this((byte) (all ? 0x00 : 0x01), 0);
	}

}
