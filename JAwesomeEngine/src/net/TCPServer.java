package net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TCPServer extends AbstractTCPServer {
	protected List<TCPConnecter> handlerlist, addlist, removelist;

	public TCPServer(int port) {
		super(port);
		handlerlist = new ArrayList<TCPConnecter>();
		addlist = new ArrayList<TCPConnecter>();
		removelist = new ArrayList<TCPConnecter>();
		new Thread(this).start();
	}

	public void addConnecter(TCPConnecter connecter) {
		addlist.add(connecter);
		connecter.setServer(this);
	}

	public void disconnectClient(TCPConnecter connecter) {
		removeConnecter(connecter);
		connecter.disconnect();
	}

	public void removeConnecter(TCPConnecter connecter) {
		removelist.add(connecter);
	}

	public void sendAll(String message) {
		updateClients();
		int size = handlerlist.size();
		for (int i = 0; i < size; i++) {
			handlerlist.get(i).send(message);
		}
	}

	@Override
	public synchronized void stop() {
		isStopped = true;
		for (TCPConnecter handler : handlerlist) {
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