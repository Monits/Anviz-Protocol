package com.monits.anviz.net.packet;

import javax.annotation.Nonnull;

import com.monits.anviz.net.packet.payload.DeviceDate;
import com.monits.jpack.Packer;

/**
 * SetDateTimeRequest Command
 *
 * @author ndbernardi
 */
public class SetDateTimeRequest extends Command {

	private static final int SET_TIME_COMMAND = 0x39;

	/**
	 * Set the request params to the Command
	 * @param date DeviceDate
	 */
	public SetDateTimeRequest(@Nonnull final DeviceDate date) {
		super((short) SET_TIME_COMMAND, Packer.pack(date));
	}

}
