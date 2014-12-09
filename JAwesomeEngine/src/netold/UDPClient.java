package netold;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public abstract class UDPClient extends UDPConnecter {
	protected InetAddress IPAddress;
	protected int port;
	protected Runnable sender, timeoutchecker;

	public UDPClient() {
		super();
	}

	public boolean connect(String hostname, int port) {
		disconnect();

		this.port = port;
		try {
			IPAddress = InetAddress.getByName(hostname);
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		new Thread(this).start();

		System.out.println("trying to connect to server");
		int connecttries = 0;
		while (lastmessage == 0 && connecttries <= 1000) {
			sendMessage("connect");
			connecttries++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (connecttries > 1000) {
			System.err.println("Connection to Server failed!");
			return false;
		}
		System.out.println("succeded to connect to server");

		final int updatedelay = 10;
		sender = new Runnable() {
			@Override
			public void run() {
				while (isConnected()) {
					sendUpdate();
					try {
						Thread.sleep(updatedelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(sender).start();

		final long timeout = 10000;
		final int checkdelta = 1000;
		timeoutchecker = new Runnable() {
			@Override
			public void run() {
				while (isConnected()) {
					long time = System.nanoTime();
					if (time - lastmessage > timeout) {
						disconnect();
						System.err.println("Connection timed out!");
					}
					try {
						Thread.sleep(checkdelta);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(timeoutchecker).start();

		return true;
	}

	public void disconnect() {
		connected = false;
		if (socket != null) {
			socket.disconnect();
			socket.close();
			onDisconnect();
		}
	}

	protected abstract void onDisconnect();

	protected abstract void process();

	@Override
	protected void receivePacket(DatagramPacket packet) {
		receive(packet);
	}

	public void send(ByteBuffer buffer) {
		try {
			socket.send(new DatagramPacket(buffer.array(),
					buffer.array().length, IPAddress, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer.clear();
	}

	public void sendMessage(String message) {
		send(ByteBuffer.wrap(message.getBytes()));
	}

	protected abstract void sendUpdate();
}
