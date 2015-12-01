package client;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageBuffer {
	private Queue<Byte> buffer;
	private boolean disconnected;

	public MessageBuffer() {
		buffer = new ArrayDeque<Byte>();
	}

	public synchronized void addMessage(byte message) {
		buffer.offer(message);
		notifyAll();
	}

	public synchronized byte getMessage() {
		while (buffer.isEmpty() && !disconnected) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		if (disconnected) {
			disconnected = false;
			return -1;
		}
		return buffer.poll();
	}

	public synchronized void clear() {
		buffer.clear();
	}

	public synchronized void setDisconnected(boolean status) {
		disconnected = status;
		notifyAll();
	}

}
