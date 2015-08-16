package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class UDPClient extends UDPConnecter {
	protected InetAddress address;
	protected int sendport;

	public void connect(InetAddress address, int sendport, int receiveport) {
		this.address = address;
		this.sendport = sendport;
		this.receiveport = receiveport;
		try {
			socket = new DatagramSocket(receiveport, address);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		setConnected(true);
		startReceiving();
	}

	public void connect(String address, int port) {
		connect(address, port, port);
	}

	public void connect(String address, int sendport, int receiveport) {
		try {
			connect(InetAddress.getByName(address), sendport, receiveport);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void send(byte[] send) {
		send(send, send.length);
	}

	public void send(byte[] send, int length) {
		send(new DatagramPacket(send, length, address, sendport));
	}

	public void send(DatagramPacket packet) {
		System.out.println("client send: " + packet.getAddress() + "; "
				+ packet.getPort() + "; " + decode(packet.getData()));
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String message) {
		send(encoder.encode(message));
	}
}