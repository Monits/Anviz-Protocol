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
package com.monits.anviz.net.packet.payload;

import com.monits.packer.annotation.DependsOn;
import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.Unsigned;

public class AttendanceList {
	
	@Encode(0)
	@Unsigned
	private short count;
	
	@Encode(1)
	@DependsOn("count")
	private AttendanceRecord records[];

	public short getCount() {
		return count;
	}

	public void setCount(short count) {
		this.count = count;
	}

	public AttendanceRecord[] getRecords() {
		return records;
	}

	public void setRecords(AttendanceRecord[] records) {
		this.records = records;
	}
	
}
