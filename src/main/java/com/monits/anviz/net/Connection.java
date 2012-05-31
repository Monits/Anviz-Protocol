package com.monits.anviz.net;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.monits.anviz.net.packet.Command;
import com.monits.anviz.net.packet.Response;
import com.monits.packer.CodecFactory;
import com.monits.packer.Packer;
import com.monits.packer.codec.Codec;

public class Connection {
	
	private Socket socket;
	
	public Connection(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
	}
	
	public Response send(int cmd, byte[] payload) throws IOException {
		
		Command command = new Command();
		
		command.setCommand((short) cmd);
		command.setLength(payload.length);
		command.setData(payload);
		command.setDeviceCode(1L);
		command.setMagic((short) 0xA5);
		
		byte[] encoded = Packer.encode(command);
		int crc = CRC16.calculateCCITT(encoded);
		
		byte[] data = Arrays.copyOf(encoded, encoded.length + 2);
		data[encoded.length] = (byte) (crc & 0xFF);
		data[encoded.length + 1] = (byte) ((crc >> 8) & 0xFF);
		
		socket.getOutputStream().write(data);
		Codec<Response> codec = CodecFactory.get(Response.class);
		return codec.decode(new PackerInputStream(socket.getInputStream()), null);
	}
	
}
