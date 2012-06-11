package com.monits.anviz.net.packet.payload;

import com.monits.packer.annotation.Encode;
import com.monits.packer.annotation.FixedLength;
import com.monits.packer.annotation.Unsigned;

public class AttendanceRecord {

	@Encode(0)
	private byte discard;
	
	@Encode(1)
	@Unsigned
	private long userId;
	
	@Encode(2)
	@Unsigned
	private long seconds;
	
	@Encode(3)
	private byte backupCode;
	
	@Encode(4)
	private byte type;
	
	@Encode(5)
	@FixedLength(3)
	private byte[] workType;

	public byte getDiscard() {
		return discard;
	}

	public void setDiscard(byte discard) {
		this.discard = discard;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getSeconds() {
		return seconds;
	}

	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}

	public byte getBackupCode() {
		return backupCode;
	}

	public void setBackupCode(byte backupCode) {
		this.backupCode = backupCode;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte[] getWorkType() {
		return workType;
	}

	public void setWorkType(byte[] workType) {
		this.workType = workType;
	}
	
}
