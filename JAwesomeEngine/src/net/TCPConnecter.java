package net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class TCPConnecter extends AbstractTCPConnecter {
	protected TCPServer server;

	protected String message;

	protected BufferedReader reader;
	protected BufferedWriter writer;

	public TCPConnecter() {
	}

	public TCPConnecter(Socket socket) {
		super(socket);
	}

	@Override
	protected void closeIO() throws IOException {
		reader.close();
		writer.close();
	}

	@Override
	protected void initIO() throws IOException {
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
	}

	protected abstract void received(String received);

	@Override
	public void removeFromServer() {
		if (server != null)
			server.removeConnecter(this);
	}

	@Override
	public void run() {
		try {
			while (isConnected()) {
				message = reader.readLine();
				received(message);
			}
		} catch (IOException e1) {
		}
		disconnect();
	}

	public void send(String message) {
		try {
			writer.write(message);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			disconnect();
		}
	}

	public void setServer(TCPServer server) {
		this.server = server;
	}
}
