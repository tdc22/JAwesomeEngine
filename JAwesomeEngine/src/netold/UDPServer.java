package netold;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class UDPServer extends UDPConnecter {
	protected int serverPort;
	protected boolean isStopped = false;
	protected HashMap<InetAddress, Integer> clients; // Address, port
	protected HashMap<InetAddress, Long> lastmessage; // Address, time
	protected List<DatagramPacket> received;
	protected Runnable timeoutchecker, process;

	public UDPServer(int port, final long timeout, final int checkdelta) {
		super();

		clients = new HashMap<InetAddress, Integer>();
		lastmessage = new HashMap<InetAddress, Long>();
		received = new ArrayList<DatagramPacket>();

		timeoutchecker = new Runnable() {
			@Override
			public void run() {
				while (!isStopped()) {
					long time = System.currentTimeMillis();
					for (InetAddress address : lastmessage.keySet()) {
						Long lasttime = lastmessage.get(address);
						if (time - lasttime > timeout) {
							removeClient(address);
						}
					}
					try {
						Thread.sleep(checkdelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		process = new Runnable() {
			@Override
			public void run() {
				while (!isStopped()) {
					int l = received.size();
					for (int p = 0; p < l; p++) {
						DatagramPacket packet = received.get(0);
						lastmessage.put(packet.getAddress(),
								System.currentTimeMillis());
						receive(packet);
						received.remove(0);
					}
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		new Thread(this).start();
		new Thread(process).start();
		new Thread(timeoutchecker).start();
	}

	protected void addClient(DatagramPacket packet) {
		InetAddress address = packet.getAddress();
		if (!clients.containsKey(address)) {
			int port = packet.getPort();
			clients.put(address, port);
		}
		lastmessage.put(address, System.currentTimeMillis());
	}

	protected void addClient(InetAddress address, int port) {
		if (!clients.containsKey(address)) {
			clients.put(address, port);
		}
		lastmessage.put(address, System.currentTimeMillis());
	}

	protected synchronized boolean isStopped() {
		return isStopped;
	}

	protected abstract void onClientRemove(InetAddress address);

	@Override
	protected void receivePacket(DatagramPacket packet) {
		received.add(packet);
	}

	protected void removeClient(InetAddress address) {
		System.out.println("Client removed.");
		clients.remove(address);
		lastmessage.remove(address);
		onClientRemove(address);
	}

	public void send(ByteBuffer buffer, InetAddress address, int port) {
		try {
			socket.send(new DatagramPacket(buffer.array(),
					buffer.array().length, address, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer.clear();
	}

	// public void sendMessageAllExcept(String message, InetAddress exception) {
	// sendAllExcept(ByteBuffer.wrap(message.getBytes()), exception);
	// }
	//
	// public void sendAllExcept(ByteBuffer buffer, InetAddress exception) {
	// for (InetAddress address : clients.keySet()) {
	// if(!address.equals(exception)) send(buffer, address,
	// clients.get(address));
	// }
	// buffer.clear();
	// }

	public void sendAll(ByteBuffer buffer) {
		for (InetAddress address : clients.keySet()) {
			send(buffer, address, clients.get(address));
		}
		buffer.clear();
	}

	public void sendMessage(String message, InetAddress address, int port) {
		send(ByteBuffer.wrap(message.getBytes()), address, port);
	}

	public void sendMessageAll(String message) {
		sendAll(ByteBuffer.wrap(message.getBytes()));
	}

	public void stop() {
		isStopped = true;
		connected = false;
		socket.close();
	}
}
