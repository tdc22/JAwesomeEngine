package net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TCPByteServer extends AbstractTCPServer {
	protected List<TCPByteConnecter> handlerlist, addlist, removelist;

	public TCPByteServer(int port) {
		super(port);
		handlerlist = new ArrayList<TCPByteConnecter>();
		addlist = new ArrayList<TCPByteConnecter>();
		removelist = new ArrayList<TCPByteConnecter>();
		new Thread(this).start();
	}

	public void addConnecter(TCPByteConnecter connecter) {
		addlist.add(connecter);
		connecter.setServer(this);
	}

	public void disconnectClient(TCPByteConnecter connecter) {
		removeConnecter(connecter);
		connecter.disconnect();
	}

	public void removeConnecter(TCPByteConnecter connecter) {
		removelist.add(connecter);
	}

	public void sendAll(String message) {
		updateClients();
		int size = handlerlist.size();
		for (int i = 0; i < size; i++) {
			handlerlist.get(i).sendMessage(message);
		}
	}

	@Override
	public synchronized void stop() {
		isStopped = true;
		for (TCPByteConnecter handler : handlerlist) {
			handler.disconnect();
		}
		try {
			if (!serverSocket.isClosed())
				serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void updateClients() {
		handlerlist.addAll(addlist);
		addlist.clear();
		handlerlist.removeAll(removelist);
		removelist.clear();
	}
}
