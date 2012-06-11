package com.monits.anviz.net;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.monits.anviz.UncooperativeDeviceException;
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
	
	public Response send(Command cmd) throws IOException, InvalidChecksumException, UncooperativeDeviceException {
		
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
		
		/*
		 * A sane man might ask, "why the loop champo? It makes no sense!"
		 * Well, sane man, the answer is simple. 
		 * Even though the device is request-response oriented, it seems that at any given moment, 
		 * it might decide to send you a TA record. The last one it seems.
		 * 
		 * The response code for that message is 0xDF. It contains at most one TA record.
		 * Since this doesn't mean that TA record is discarded from the device, logic follows we just ignore it.
		 */
		
		Response response = null;
		do {
			if (response != null) {
				System.out.println("Retrying after an 0xDF");
			}
			
			response = codec.decode(new PackerInputStream(socket.getInputStream()), null);
			if (!hasValidCRC(response)) {
				throw new InvalidChecksumException();
			}
		
		} while (response.getCommand() == 0xDF);
		
		if (cmd.getCommand() + 0x80 != response.getCommand()) {
			throw new UncooperativeDeviceException("The response code doesnt match the command code");
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
