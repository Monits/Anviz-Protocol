package com.monits.anviz.net;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.monits.anviz.net.packet.Command;
import com.monits.anviz.net.packet.Request;
import com.monits.anviz.net.packet.Response;
import com.monits.packer.CodecFactory;
import com.monits.packer.Packer;
import com.monits.packer.codec.Codec;

public class Connection {
	
	private Socket socket;
	
	private long deviceCode;
	
	public Connection(String ip, int port, long deviceCode) throws IOException {
		socket = new Socket(ip, port);
		this.deviceCode = deviceCode;
	}
	
	public Response send(Command cmd) throws IOException, InvalidChecksumException {
		
		Request request = new Request();
		
		byte[] payload = cmd.getPayload();
		request.setCommand(cmd.getCommand());
		request.setLength(payload.length);
		request.setData(payload);
		request.setDeviceCode(deviceCode);
		request.setMagic((short) 0xA5);
		
		byte[] encoded = Packer.encode(request);
		int crc = CRC16.calculateCCITT(encoded);
		
		byte[] data = Arrays.copyOf(encoded, encoded.length + 2);
		data[encoded.length] = (byte) (crc & 0xFF);
		data[encoded.length + 1] = (byte) ((crc >> 8) & 0xFF);
		
		socket.getOutputStream().write(data);
		Codec<Response> codec = CodecFactory.get(Response.class);
		
		Response response = codec.decode(new PackerInputStream(socket.getInputStream()), null);
		if (!hasValidCRC(response)) {
			throw new InvalidChecksumException();
		}
		
		return response;
	}

	private boolean hasValidCRC(Response response) {
		
		byte[] packed = Packer.encode(response);
		if (packed == null || packed.length < 2) {
			return false;
		}
		
		byte[] withoutCRC = Arrays.copyOf(packed, packed.length - 2);
		int crc = CRC16.calculateCCITT(withoutCRC);
		
		return packed[packed.length - 2] == (byte) (crc & 0xFF)
			&& packed[packed.length - 1] == (byte) ((crc >> 8) & 0xFF);
	}
	
}
