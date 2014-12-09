package netold;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public abstract class UDPConnecter implements Runnable {
	protected DatagramSocket socket;
	protected int buffersize = 1024;
	protected long lastmessage;
	protected CharsetDecoder decoder;
	protected boolean connected = false;

	public UDPConnecter() {
		setCharset("US-ASCII");
		lastmessage = 0;
	}

	public String decode(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return decode(buffer);
	}

	public String decode(ByteBuffer buffer) {
		String message = "";
		try {
			message = decoder.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		return message;
	}

	public int getBuffersize() {
		return buffersize;
	}

	protected synchronized boolean isConnected() {
		return connected;
	}

	protected abstract void receive(DatagramPacket packet);

	protected abstract void receivePacket(DatagramPacket packet);

	@Override
	public void run() {
		connected = true;
		while (isConnected()) {
			DatagramPacket packet = new DatagramPacket(
					new byte[getBuffersize()], getBuffersize());
			try {
				socket.receive(packet);
			} catch (SocketException e) {
				// OK, otherwise gets thrown on shutting down server
			} catch (IOException e) {
				e.printStackTrace();
			}
			lastmessage = System.nanoTime();
			receivePacket(packet);
		}
	}

	public void setBuffersize(int buffersize) {
		this.buffersize = buffersize;
	}

	public void setCharset(String charsetname) {
		decoder = Charset.forName(charsetname).newDecoder();
	}
}
