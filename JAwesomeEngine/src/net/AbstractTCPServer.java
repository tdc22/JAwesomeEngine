package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public abstract class AbstractTCPServer implements Runnable {
	protected ServerSocket serverSocket;
	protected boolean isStopped = false;

	public AbstractTCPServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected synchronized boolean isStopped() {
		return isStopped;
	}

	protected abstract void onClientConnect(Socket clientSocket);

	@Override
	public void run() {
		Socket socket = null;
		while (!isStopped()) {
			try {
				socket = serverSocket.accept();
			} catch (SocketException e1) {
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null)
				onClientConnect(socket);
		}
		stop();
	}

	public abstract void stop();
}
