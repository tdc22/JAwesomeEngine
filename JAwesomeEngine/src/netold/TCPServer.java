package netold;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public abstract class TCPServer implements Runnable {
	protected ServerSocketChannel serverSocketChannel;
	protected int serverPort;
	protected boolean isStopped = false;
	protected List<TCPConnecter> connecterlist;

	public TCPServer(int port) {
		serverPort = port;
		connecterlist = new ArrayList<TCPConnecter>();
		new Thread(this).start();
	}

	protected synchronized void addConnecter(TCPConnecter handler) {
		connecterlist.add(handler);
	}

	protected synchronized boolean isStopped() {
		return isStopped;
	}

	protected abstract void onClientConnect(SocketChannel clientSocket);

	@Override
	public void run() {
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket()
					.bind(new InetSocketAddress(serverPort));
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (!isStopped()) {
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				onClientConnect(socketChannel);
			} catch (IOException e) {
			}
		}
	}

	public void sendAll(ByteBuffer buffer) {
		TCPConnecter connecter;
		for (int c = connecterlist.size() - 1; c >= 0; c--) {
			connecter = connecterlist.get(c);
			if (connecter.isConnected())
				connecter.send(buffer);
			else
				connecterlist.remove(connecter);
		}
	}

	public void sendMessageAll(String message) {
		sendAll(ByteBuffer.wrap(message.getBytes()));
	}

	public synchronized void stop() {
		isStopped = true;
		for (TCPConnecter handler : connecterlist) {
			handler.disconnect();
		}
		try {
			if (serverSocketChannel.isOpen())
				serverSocketChannel.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}
}
