package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import utils.StringEncoder;

public abstract class UDPConnecter implements Runnable {
	DatagramSocket socket;
	DatagramPacket received;
	boolean isConnected;

	protected int receiveport;

	protected StringEncoder encoder;

	public UDPConnecter() {
		setBufferSize(1024);
		encoder = new StringEncoder("US-ASCII");
	}

	public String decode(byte[] data) {
		return encoder.decode(data);
	}

	public void disconnect() {
		setConnected(false);
		socket.close();
	}

	public byte[] encode(String string) {
		return encoder.encode(string);
	}

	protected boolean isConnected() {
		return isConnected;
	}

	protected abstract void received(DatagramPacket packet);

	@Override
	public void run() {
		while (isConnected()) {
			try {
				socket.receive(received);
				// System.out.println("received: " +
				// decode(received.getData()));
				received(received);
			} catch (SocketException e1) {
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void setBufferSize(int size) {
		received = new DatagramPacket(new byte[size], size);
	}

	public void setCharset(String charsetname) {
		encoder.setCharset(charsetname);
	}

	protected void setConnected(boolean connected) {
		isConnected = connected;
	}

	protected void startReceiving() {
		new Thread(this).start();
	}
}