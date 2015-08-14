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
package com.monits.anviz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.monits.anviz.model.Action;
import com.monits.anviz.model.CheckIn;
import com.monits.anviz.model.CheckOut;
import com.monits.anviz.model.User;
import com.monits.anviz.net.Connection;
import com.monits.anviz.net.InvalidChecksumException;
import com.monits.anviz.net.packet.AttendanceRecordsRequest;
import com.monits.anviz.net.packet.GetDateTimeRequest;
import com.monits.anviz.net.packet.PurgeRecordsRequest;
import com.monits.anviz.net.packet.Response;
import com.monits.anviz.net.packet.SetDateTimeRequest;
import com.monits.anviz.net.packet.UserListRequest;
import com.monits.anviz.net.packet.payload.AttendanceList;
import com.monits.anviz.net.packet.payload.AttendanceRecord;
import com.monits.anviz.net.packet.payload.DeviceDate;
import com.monits.anviz.net.packet.payload.PurgeResponse;
import com.monits.anviz.net.packet.payload.UserEntry;
import com.monits.anviz.net.packet.payload.UserList;
import com.monits.jpack.Packer;

/**
 * Represents an Anviz Timekeeper device.
 *
 * @author jpcivile
 */
public class TimeKeeper {

	private static final int BUILD_DATETIME_YEAR = 2000;
	private static final int DEFAULT_PORT = 5010;
	private static final String INVALID_RESPONSE_MESSAGE = "Got an invalid response";
	private Connection conn;
	private DateTimeZone timezone;

	/**
	 *
	 * @param ip         The ip the device has on the network.
	 * @param port       The port the device listens on.
	 * @param deviceCode The code for the device.
	 * @param timezone   The timezone your device uses.
	 *
	 * @throws IOException Can't connect with device
	 */
	public TimeKeeper(@Nonnull final String ip, final int port,
			final long deviceCode,
			@Nullable final DateTimeZone timezone) throws IOException {
		conn = new Connection(ip, port, deviceCode);
		this.timezone = timezone;
	}

	/**
	 * Construct a new {@link TimeKeeper} using the default system timezone.
	 *
	 * @param ip         The ip the device has on the network.
	 * @param port       The port the device listens on.
	 * @param deviceCode The code for the device.
	 *
	 * @throws IOException Can't connect with device
	 */
	public TimeKeeper(@Nonnull final String ip, final int port, final long deviceCode)
			throws IOException {
		this(ip, port, deviceCode, null);
	}

	/**
	 * Construct a new {@link TimeKeeper} with the default system timezone and default port.
	 *
	 * @param ip         The ip the device has on the network.
	 * @param deviceCode The code for the device.
	 *
	 * @throws IOException Can't connect with device
	 */
	public TimeKeeper(@Nonnull final String ip, final long deviceCode) throws IOException {
		this(ip, DEFAULT_PORT, deviceCode);
	}

	/**
	 * Get all the check ins and check outs stored in the device.
	 *
	 * @return A list of actions.
	 *
	 * @throws UncooperativeDeviceException Not a valid response
	 */
	@Nonnull
	public List<Action> getActions() throws UncooperativeDeviceException {

		boolean first = true;
		int results = 0;
		final List<Action> records = new ArrayList<Action>();

		do {
			final AttendanceRecordsRequest cmd = new AttendanceRecordsRequest(first);
			Response response;
			try {
				response = conn.send(cmd);
			} catch (final IOException e) {
				throw new UncooperativeDeviceException(e);
			} catch (final InvalidChecksumException e) {
				throw new UncooperativeDeviceException(e);
			}

			if (!isValidResponse(response)) {
				throw new UncooperativeDeviceException(INVALID_RESPONSE_MESSAGE);
			}

			final AttendanceList list = Packer.unpack(AttendanceList.class, response.getData());
			results = list.getCount();

			for (final AttendanceRecord record : list.getRecords()) {

				final DateTime date = buildDateTime(record.getSeconds());
				if (record.getType() == 0x00) {
					records.add(new CheckIn(record.getUserId(), date));
				} else {
					records.add(new CheckOut(record.getUserId(), date));
				}
			}

			first = false;
		} while (AttendanceRecordsRequest.MAX_RESULTS <= results);

		return records;
	}

	@Nonnull
	private DateTime buildDateTime(final long seconds) {
		// The documentation says that dates are given as seconds since year 2000
		// However, experience shows that it's actually from the second day of the year 2000
		return new DateTime(BUILD_DATETIME_YEAR, 01, 02, 00, 00, 00, timezone).plusSeconds((int) seconds);
	}

	/**
	 * Purges all actions stored in the device.
	 *
	 * This cannot be rolled back. It's a destructive operation.
	 * Make sure you reaaaaaally mean to call this when you do.
	 *
	 * Any actions that haven't been processed before, are lost.
	 *
	 * @return Purged actions count
	 * @throws UncooperativeDeviceException Not a valid response
	 *
	 */
	public int purgeActions() throws UncooperativeDeviceException {

		int purged = 0;
		int total = 0;

		do {
			final PurgeRecordsRequest cmd = new PurgeRecordsRequest(true);
			Response response;
			try {
				response = conn.send(cmd);
			} catch (final IOException e) {
				throw new UncooperativeDeviceException(e);
			} catch (final InvalidChecksumException e) {
				throw new UncooperativeDeviceException(e);
			}

			if (!isValidResponse(response)) {
				throw new UncooperativeDeviceException(INVALID_RESPONSE_MESSAGE);
			}

			final PurgeResponse purge = Packer.unpack(PurgeResponse.class, response.getData());
			purged = purge.getCount();
			total += purged;

		} while (purged > 0);

		return total;
	}

	/**
	 * Get all the users registered with the device.
	 *
	 * @return A list of users.
	 *
	 * @throws UncooperativeDeviceException Not a valid response
	 */
	@Nonnull
	public List<User> getUsers() throws UncooperativeDeviceException {

		boolean first = true;
		int results = 0;
		final List<User> users = new ArrayList<User>();

		do {
			final UserListRequest cmd = new UserListRequest(first);
			Response response;
			try {
				response = conn.send(cmd);
			} catch (final IOException e) {
				throw new UncooperativeDeviceException(e);
			} catch (final InvalidChecksumException e) {
				throw new UncooperativeDeviceException(e);
			}

			if (!isValidResponse(response)) {
				throw new UncooperativeDeviceException(INVALID_RESPONSE_MESSAGE);
			}

			final UserList list = Packer.unpack(UserList.class, response.getData());
			results = list.getCount();

			for (final UserEntry user : list.getUsers()) {
				users.add(new User(user.getUserId(), user.getName()));
			}

			first = false;
		} while (UserListRequest.MAX_RESULTS <= results);

		return users;
	}

	private boolean isValidResponse(@Nonnull final Response res) {
		return res != null && res.getAck() == Response.SUCCESS;
	}

	/**
	 * Get the device date
	 *
	 * @return The device date
	 *
	 * @throws UncooperativeDeviceException Not a valid response
	 */
	@Nonnull
	public DateTime getDeviceDate() throws UncooperativeDeviceException {

		final GetDateTimeRequest cmd = new GetDateTimeRequest();
		Response response;
		try {
			response = conn.send(cmd);
		} catch (final IOException e) {
			throw new UncooperativeDeviceException(e);
		} catch (final InvalidChecksumException e) {
			throw new UncooperativeDeviceException(e);
		}
		if (!isValidResponse(response)) {
			throw new UncooperativeDeviceException(INVALID_RESPONSE_MESSAGE);
		}

		final DeviceDate deviceDate = Packer.unpack(DeviceDate.class, response.getData());
		return new DateTime(BUILD_DATETIME_YEAR + deviceDate.getYear(),
				deviceDate.getMonth(),
				deviceDate.getDay(),
				deviceDate.getHour(),
				deviceDate.getMinute(),
				deviceDate.getSecond()).withZoneRetainFields(timezone);
	}

	/**
	 * Sets the device Date
	 *
	 * @param dt The date time to set to the device
	 *
	 * @throws UncooperativeDeviceException	Not a valid response
	 */
	public void setDateToDevice(@Nonnull final DateTime dt) throws UncooperativeDeviceException {
		final DeviceDate date = new DeviceDate();

		final DateTime dateAndZone;

		if (timezone == null) {
			dateAndZone = dt;
		} else {
			dateAndZone = dt.withZone(timezone);
		}

		date.setYear((short) dateAndZone.getYearOfCentury());
		date.setMonth((short) dateAndZone.getMonthOfYear());
		date.setDay((short) dateAndZone.getDayOfMonth());
		date.setHour((short) dateAndZone.getHourOfDay());
		date.setMinute((short) dateAndZone.getMinuteOfHour());
		date.setSecond((short) dateAndZone.getSecondOfMinute());

		final SetDateTimeRequest cmd = new SetDateTimeRequest(date);
		Response response;

		try {
			response = conn.send(cmd);
		} catch (final IOException e) {
			throw new UncooperativeDeviceException(e);
		} catch (final InvalidChecksumException e) {
			throw new UncooperativeDeviceException(e);
		}
		if (!isValidResponse(response)) {
			throw new UncooperativeDeviceException(INVALID_RESPONSE_MESSAGE);
		}
	}

	@Override
	public String toString() {
		return "TimeKeeper [conn=" + conn + ", timezone=" + timezone + " ]";
	}
}