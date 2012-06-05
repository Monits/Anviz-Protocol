package com.monits.anviz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.monits.anviz.model.Action;
import com.monits.anviz.model.User;
import com.monits.anviz.net.Connection;
import com.monits.anviz.net.InvalidChecksumException;
import com.monits.anviz.net.packet.Command;
import com.monits.anviz.net.packet.Response;
import com.monits.anviz.net.packet.UserListRequest;
import com.monits.anviz.net.packet.payload.UserEntry;
import com.monits.anviz.net.packet.payload.UserList;
import com.monits.packer.Packer;

public class TimeKeeper {
	
	private Connection conn;
	
	public TimeKeeper(String ip, int port, long deviceCode) throws IOException {
		conn = new Connection(ip, port, deviceCode);
	}
	
	public List<Action> getActions() {
		return null;
	}
	
	public void purgeActions() {
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
			return cmd.getCommand() + 0x80 == res.getCommand() && res.getAck() == Response.SUCCESS;
		} else {
			return false;
		}
	}

	public static void main(String[] args) throws IOException, UncooperativeDeviceException {
		TimeKeeper timeKeeper = new TimeKeeper("localhost", 1337, 1L);
		System.out.println(timeKeeper.getUsers());
	}
}
