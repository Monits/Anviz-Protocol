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

import com.monits.jpack.annotation.Encode;
import com.monits.jpack.annotation.FixedLength;
import com.monits.jpack.annotation.Unsigned;

public class UserEntry {

	@Encode(0)
	private byte unknown0;

	@Encode(1)
	@Unsigned
	private long userId;
	
	@Encode(2)
	@FixedLength(7)
	private byte[] unknown1;
	
	@Encode(3)
	@FixedLength(10)
	private String name;
	
	@Encode(4)
	@FixedLength(8)
	private byte[] uknown2;

	public byte getUnknown0() {
		return unknown0;
	}

	public void setUnknown0(byte unknown0) {
		this.unknown0 = unknown0;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public byte[] getUnknown1() {
		return unknown1;
	}

	public void setUnknown1(byte[] unknown1) {
		this.unknown1 = unknown1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getUknown2() {
		return uknown2;
	}

	public void setUknown2(byte[] uknown2) {
		this.uknown2 = uknown2;
	}
	
}
