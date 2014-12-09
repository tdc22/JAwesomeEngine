package net;

import java.io.IOException;
import java.net.Socket;

public abstract class AbstractTCPConnecter implements Runnable {
	protected Socket socket;
	protected boolean isConnected;

	public AbstractTCPConnecter() {
	};

	public AbstractTCPConnecter(Socket socket) {
		setSocket(socket);
	};

	protected abstract void closeIO() throws IOException;

	public void disconnect() {
		setConnected(false);
		removeFromServer();
		if (!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			closeIO();
		} catch (IOException e1) {
		}
		onDisconnect();
	}

	protected abstract void initIO() throws IOException;

	protected boolean isConnected() {
		return isConnected;
	}

	protected void onConnect() {
	}

	protected void onDisconnect() {
	}

	protected abstract void removeFromServer();

	protected void setConnected(boolean connected) {
		isConnected = connected;
	}

	public void setSocket(Socket socket) {
		if (isConnected())
			disconnect();
		this.socket = socket;

		try {
			initIO();
		} catch (IOException e) {
			e.printStackTrace();
		}

		isConnected = true;
		new Thread(this).start();
		onConnect();
	}
}
