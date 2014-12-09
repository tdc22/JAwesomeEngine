package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import utils.StringEncoder;

public abstract class TCPByteConnecter extends AbstractTCPConnecter {
	protected TCPByteServer server;
	protected StringEncoder encoder;

	protected byte[] buffer;
	protected int readbytes;

	protected InputStream inputstream;
	protected OutputStream outputstream;

	public TCPByteConnecter() {
		setBufferSize(1024);
		encoder = new StringEncoder("US-ASCII");
	}

	public TCPByteConnecter(Socket socket) {
		super(socket);
		setBufferSize(1024);
		encoder = new StringEncoder("US-ASCII");
	}

	@Override
	protected void closeIO() throws IOException {
		inputstream.close();
		outputstream.close();
	}

	public String decode(byte[] data) {
		return encoder.decode(data);
	}

	public byte[] encode(String string) {
		return encoder.encode(string);
	}

	@Override
	protected void initIO() throws IOException {
		inputstream = socket.getInputStream();
		outputstream = socket.getOutputStream();
	}

	protected abstract void received(byte[] received);

	@Override
	public void removeFromServer() {
		if (server != null)
			server.removeConnecter(this);
	}

	@Override
	public void run() {
		try {
			while (isConnected()) {
				readbytes = inputstream.read(buffer);
				if (readbytes != -1)
					received(Arrays.copyOf(buffer, readbytes));
				else
					setConnected(false);
			}
		} catch (IOException e1) {
		}
		disconnect();
	}

	public void send(byte[] output) {
		try {
			outputstream.write(output);
			outputstream.flush();
		} catch (SocketException e1) {
			disconnect();
		} catch (IOException e) {
			disconnect();
		}
	}

	public void sendMessage(String message) {
		send(encode(message));
	}

	protected void setBufferSize(int size) {
		buffer = new byte[size];
	}

	public void setCharset(String charsetname) {
		encoder.setCharset(charsetname);
	}

	public void setServer(TCPByteServer server) {
		this.server = server;
	}
}