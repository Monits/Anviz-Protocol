package com.monits.anviz.net.packet;

/**
 * GetDateTimeRequest Command
 * @author ndbernardi
 */
public class GetDateTimeRequest extends Command {

	private static final int GET_TIME_COMMAND = 0x38;

	/**
	 * Set the request params to the command
	 */
	public GetDateTimeRequest() {
		super((short) GET_TIME_COMMAND, new byte[0]);
	}
}