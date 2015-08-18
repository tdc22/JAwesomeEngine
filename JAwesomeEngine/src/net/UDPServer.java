package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import utils.DefaultValues;

public abstract class UDPServer extends UDPConnecter {
	HashMap<InetAddress, Long> clients, clientqueue;
	HashMap<InetAddress, Integer> portlist, portqueue;
	long timeout;

	public UDPServer(int port) {
		super();
		init(port, DefaultValues.DEFAULT_UDP_SERVER_TIMEOUT);
	}

	public UDPServer(int port, long timeout) {
		super();
		init(port, timeout);
	}

	private void init(int port, long to) {
		clients = new HashMap<InetAddress, Long>();
		clientqueue = new HashMap<InetAddress, Long>();
		portlist = new HashMap<InetAddress, Integer>();
		portqueue = new HashMap<InetAddress, Integer>();
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		setConnected(true);
		setTimeout(to);
		// Runnable timeoutchecker = new Runnable() {
		// @Override
		// public void run() {
		// while (isConnected()) {
		// System.out.println("Checking timeouts.");
		// clients.putAll(clientqueue);
		// clientqueue.clear();
		//
		// long current = System.currentTimeMillis();
		// for(Map.Entry<InetAddress, Long> client : clients.entrySet()) {
		// System.out.println(current - client.getValue());
		// if(current - client.getValue() > timeout) {
		// clients.remove(client.getKey());
		// }
		// }
		//
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// };
		Thread timeoutchecker = new Thread() {
			@Override
			public void run() {
				System.out.println("started");
				while (isConnected()) {
					System.out.println("Checking timeouts.");

					clients.putAll(clientqueue);
					clientqueue.clear();

					portlist.putAll(portqueue);
					portqueue.clear();

					long current = System.currentTimeMillis();
					for (Map.Entry<InetAddress, Long> client : clients.entrySet()) {
						System.out.println(current - client.getValue());
						if (current - client.getValue() > timeout) {
							clients.remove(client.getKey());
						}
					}

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		System.out.println("Start Timeoutchecker");
		timeoutchecker.start();
		System.out.println("Done: Start Timeoutchecker");
		startReceiving();
	}

	protected abstract void process(DatagramPacket packet);

	@Override
	protected void received(DatagramPacket packet) {
		clientqueue.put(packet.getAddress(), System.currentTimeMillis());
		portqueue.put(packet.getAddress(), packet.getPort());
		process(packet);
	}

	public void sendAll(byte[] send) {
		sendAll(send, send.length);
	}

	public void sendAll(byte[] send, int length) {
		sendAll(new DatagramPacket(send, length, null, 0));
	}

	public void sendAll(DatagramPacket packet) {
		for (InetAddress clientaddress : clients.keySet()) {
			packet.setAddress(clientaddress);
			packet.setPort(portlist.get(clientaddress));
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendAll(String message) {
		sendAll(encoder.encode(message));
	}

	public void sendAllExcept(byte[] send, InetAddress client) {
		sendAllExcept(send, send.length, client);
	}

	public void sendAllExcept(byte[] send, int length, InetAddress client) {
		sendAllExcept(new DatagramPacket(send, length, null, 0), client);
	}

	public void sendAllExcept(DatagramPacket packet, InetAddress client) {
		for (InetAddress clientaddress : clients.keySet()) {
			if (!clientaddress.equals(client)) {
				packet.setAddress(clientaddress);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendAllExcept(String message, InetAddress client) {
		sendAllExcept(encoder.encode(message), client);
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
