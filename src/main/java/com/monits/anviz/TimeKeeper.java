package com.monits.anviz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.monits.anviz.model.Action;
import com.monits.anviz.model.CheckIn;
import com.monits.anviz.model.CheckOut;
import com.monits.anviz.model.User;
import com.monits.anviz.net.Connection;
import com.monits.anviz.net.InvalidChecksumException;
import com.monits.anviz.net.packet.AttendanceRecordsRequest;
import com.monits.anviz.net.packet.Command;
import com.monits.anviz.net.packet.PurgeRecordsRequest;
import com.monits.anviz.net.packet.Response;
import com.monits.anviz.net.packet.UserListRequest;
import com.monits.anviz.net.packet.payload.AttendanceList;
import com.monits.anviz.net.packet.payload.AttendanceRecord;
import com.monits.anviz.net.packet.payload.PurgeResponse;
import com.monits.anviz.net.packet.payload.UserEntry;
import com.monits.anviz.net.packet.payload.UserList;
import com.monits.packer.Packer;

public class TimeKeeper {
	
	private static final int DEFAULT_PORT = 5010;
	
	private Connection conn;

	private DateTimeZone timezone;
	
	
	public TimeKeeper(String ip, int port, long deviceCode, DateTimeZone timezone) throws IOException {
		conn = new Connection(ip, port, deviceCode);
		this.timezone = timezone;
	}
	
	public TimeKeeper(String ip, int port, long deviceCode) throws IOException {
		this(ip, port, deviceCode, null);
	}
	
	public TimeKeeper(String ip, long deviceCode) throws IOException {
		this(ip, DEFAULT_PORT, deviceCode);
	}

	public List<Action> getActions() throws UncooperativeDeviceException {
		
		boolean first = true;
		int results = 0;
		List<Action> records = new ArrayList<Action>();
		
		do {
			AttendanceRecordsRequest cmd = new AttendanceRecordsRequest(first);
			Response response;
			try {
				response = conn.send(cmd);
			} catch (IOException e) {
				throw new UncooperativeDeviceException(e);
			} catch (InvalidChecksumException e) {
				throw new UncooperativeDeviceException(e);
			}
			
			if (!isValidResponse(cmd, response)) {
				throw new UncooperativeDeviceException("Got an invalid response");
			}
			
			AttendanceList list = Packer.decode(AttendanceList.class, response.getData());
			results = list.getCount();
			
			for (AttendanceRecord record : list.getRecords()) {
				
				DateTime date = buildDateTime(record.getSeconds());
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
	
	private DateTime buildDateTime(long seconds) {
		// The documentation says that dates are given as seconds since year 2000
		// However, experience shows that it's actually from the second day of the year 2000
		return new DateTime(2000, 01, 02, 00, 00, 00, timezone).plusSeconds((int) seconds);
	}

	/**
	 * Purges all actions stored in the device.
	 * 
	 * This cannot be rolled back. It's a destructive operation.
	 * Make sure you reaaaaaally mean to call this when you do.
	 * 
	 * Any actions that haven't been processed before, are lost.
	 * @throws UncooperativeDeviceException 
	 * 
	 */
	public int purgeActions() throws UncooperativeDeviceException {
		
		int purged = 0;
		int total = 0;
		
		do {
			PurgeRecordsRequest cmd = new PurgeRecordsRequest(true);
			Response response;
			try {
				response = conn.send(cmd);
			} catch (IOException e) {
				throw new UncooperativeDeviceException(e);
			} catch (InvalidChecksumException e) {
				throw new UncooperativeDeviceException(e);
			}
			
			if (!isValidResponse(cmd, response)) {
				throw new UncooperativeDeviceException("Got an invalid response");
			}
			
			PurgeResponse purge = Packer.decode(PurgeResponse.class, response.getData());
			purged = purge.getCount();
			total += purged;
			
		} while (purged > 0);
		
		return total;
	}
	
	public List<User> getUsers() throws UncooperativeDeviceException {
		
		boolean first = true;
		int results = 0;
		List<User> users = new ArrayList<User>();
		
		do {
			UserListRequest cmd = new UserListRequest(first);
			Response response;
			try {
				response = conn.send(cmd);
			} catch (IOException e) {
				throw new UncooperativeDeviceException(e);
			} catch (InvalidChecksumException e) {
				throw new UncooperativeDeviceException(e);
			}
			
			if (!isValidResponse(cmd, response)) {
				throw new UncooperativeDeviceException("Got an invalid response");
			}
			
			UserList list = Packer.decode(UserList.class, response.getData());
			results = list.getCount();
			
			for (UserEntry user : list.getUsers()) {
				users.add(new User(user.getUserId(), user.getName()));
			}
			
			first = false;
		} while (UserListRequest.MAX_RESULTS <= results);
		
		return users;
	}
	
	private boolean isValidResponse(Command cmd, Response res) {
		if (res != null) {
			return res.getAck() == Response.SUCCESS;
		} else {
			return false;
		}
	}

}
