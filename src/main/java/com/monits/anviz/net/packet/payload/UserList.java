package com.monits.anviz.net.packet.payload;

import com.monits.packer.annotation.DependsOn;
import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.Unsigned;

public class UserList {

	@Encode(0)
	@Unsigned
	private short count;
	
	@Encode(1)
	@DependsOn("count")
	private UserEntry users[];

	public short getCount() {
		return count;
	}

	public void setCount(short count) {
		this.count = count;
	}

	public UserEntry[] getUsers() {
		return users;
	}

	public void setUsers(UserEntry[] users) {
		this.users = users;
	}
	
}
