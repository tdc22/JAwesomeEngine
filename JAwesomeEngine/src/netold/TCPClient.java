package netold;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public abstract class TCPClient extends TCPConnecter {
	public TCPClient() {
		super();
	}

	public boolean connect(String hostname, int port) {
		disconnect();
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress(hostname, port));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		onConnect();
		new Thread(this).start();
		return true;
	}

	protected abstract void onConnect();

	public abstract void process();
}
