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

public class UserListRequest extends Command {

	public static final int MAX_RESULTS = 0x0C;

	public UserListRequest(boolean first) {
		super((short) 0x72, new byte[2]);
		
		if (first) {
			payload[0] = 0x01;
		} else {
			payload[0] = 0x00;
		}
		
		payload[1] = MAX_RESULTS;
	}

}
