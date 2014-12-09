package net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class TCPClient extends TCPConnecter {
	public void connect(String address, int port) {
		try {
			setSocket(new Socket(address, port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
