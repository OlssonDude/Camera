package client;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageBuffer {
	private Queue<ServerMessage> buffer;
	private boolean disconnected;

	public MessageBuffer() {
		buffer = new ArrayDeque<ServerMessage>();
	}

	public synchronized void addMessage(ServerMessage message) {
		buffer.offer(message);
		notifyAll();
	}

	public synchronized ServerMessage getMessage() {
		while (buffer.isEmpty() && !disconnected) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		if (disconnected) {
			disconnected = false;
			return null;
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
