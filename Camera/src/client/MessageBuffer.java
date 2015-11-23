package client;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageBuffer {
	private Queue<ServerMessage> buffer;

	public MessageBuffer() {
		buffer = new ArrayDeque<ServerMessage>();
	}
	
	public synchronized void addMessage(ServerMessage message) {
		buffer.offer(message);
		notifyAll();
	}
	
	public synchronized ServerMessage getMessage() {
		while(buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buffer.poll();
	}
}
