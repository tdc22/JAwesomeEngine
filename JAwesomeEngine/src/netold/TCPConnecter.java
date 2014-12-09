package netold;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public abstract class TCPConnecter implements Runnable {
	protected SocketChannel socketChannel;
	protected int buffersize = 1024;
	protected CharsetDecoder decoder;
	protected boolean connected = false;

	ByteBuffer receivedbytes;
	int messagelength = 0;

	public TCPConnecter() {
		setCharset("US-ASCII");
		receivedbytes = ByteBuffer.allocate(0);
	}

	private int bytestoint(byte[] bytes) {
		return bytes[1] & 0xFF | (bytes[0] & 0xFF) << 8;
	}

	private void calculateMessageLength() {
		if (receivedbytes.limit() >= 2)
			messagelength = bytestoint(new byte[] { receivedbytes.get(0),
					receivedbytes.get(1) });
		else
			messagelength = 0;
	}

	public String decode(ByteBuffer buffer) {
		String message = "";
		try {
			message = decoder.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		return message;
	}

	public void disconnect() {
		if (socketChannel != null) {
			try {
				connected = false;
				if (socketChannel.isOpen())
					socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			onDisconnect();
		}
	}

	public int getBuffersize() {
		return buffersize;
	}

	private byte[] inttobytes(int a) {
		byte[] result = new byte[2];
		result[0] = Integer.valueOf(a >> 8).byteValue();
		result[1] = Integer.valueOf(a % 256).byteValue();
		return result;
	}

	public boolean isConnected() {
		return connected;
	}

	protected abstract void onDisconnect();

	protected abstract void receive(ByteBuffer buffer);

	private void receiveBuf(ByteBuffer buffer) {
		setReceivedBufferCapacity(receivedbytes.limit() + buffer.limit());
		System.out.println("problem?2 " + receivedbytes.capacity() + "; "
				+ receivedbytes.toString() + "; " + buffer.toString());
		receivedbytes.put(buffer);
		receivedbytes.flip();
		System.out.println(receivedbytes.limit() + "; " + messagelength);

		buffer.clear();
		if (messagelength == 0) {
			calculateMessageLength();
		}
		while (receivedbytes.limit() >= messagelength && messagelength > 0) {
			ByteBuffer buf = ByteBuffer.allocate(messagelength - 2);
			for (int p = 2; p < messagelength; p++) {
				buf.put(receivedbytes.get(p));
			}
			buf.flip();
			receive(buf);
			buf.clear();

			receivedbytes.position(messagelength);
			receivedbytes.compact();
			receivedbytes.flip();
			setReceivedBufferCapacity(receivedbytes.capacity() - messagelength);

			receivedbytes.flip();
			calculateMessageLength();
		}
		System.out.println(receivedbytes.limit());
	}

	@Override
	public void run() {
		connected = true;
		while (connected) {
			ByteBuffer buf = ByteBuffer.allocate(getBuffersize() + 2);
			buf.clear();
			try {
				socketChannel.read(buf);
			} catch (IOException e) {
			}
			buf.flip();
			if (buf.hasRemaining()) {
				receiveBuf(buf);
			} else {
				connected = false;
			}
			buf.clear();
		}
		disconnect();
	}

	public void send(ByteBuffer buffer) {
		int size = buffer.limit() + 2;
		ByteBuffer buf = ByteBuffer.allocate(size);
		buf.put(inttobytes(size));
		buf.put(buffer);

		System.out.println(decode(buffer) + " : " + size);
		buf.flip();
		try {
			if (socketChannel.isOpen())
				socketChannel.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffer.clear();
	}

	public void sendMessage(String message) {
		send(ByteBuffer.wrap(message.getBytes()));
	}

	public void setBuffersize(int buffersize) {
		this.buffersize = buffersize;
	}

	public void setCharset(String charsetname) {
		decoder = Charset.forName(charsetname).newDecoder();
	}

	private ByteBuffer setReceivedBufferCapacity(int capacity) {
		ByteBuffer temp = receivedbytes;
		receivedbytes = ByteBuffer.allocate(capacity);
		System.out.println("problem? " + capacity + "; " + temp.toString()
				+ "; " + receivedbytes.toString());
		if (temp.capacity() > 0) {
			temp.flip();
			receivedbytes.put(temp);
		}
		System.out.println("problem?1 " + capacity + "; " + temp.toString()
				+ "; " + receivedbytes.toString());
		temp.clear();
		return receivedbytes;
	}
}