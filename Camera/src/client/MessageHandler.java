package client;

import java.io.IOException;
import java.io.OutputStream;

public class MessageHandler extends Thread {
	private MessageBuffer buffer;
	private OutputStream out;
	private ConnectionMonitor server;

	public MessageHandler(MessageBuffer buffer, ConnectionMonitor server) {
		this.buffer = buffer;
		this.server = server;
	}

	@Override
	public void run() {
		waitForConnection();
		while (true) {
			if (server.isConnected()) {
				try {
					byte msg = buffer.getMessage();
					if (msg == -1) {
						waitForConnection();
					} else {
						out.write(msg);
					}
				} catch (IOException e) {
					System.out.println("MessageHandler Disconnected"); // TODO
				}
			} else {
				waitForConnection();
			}
		}
	}

	private void waitForConnection() {
		out = server.getOutputStream();
		buffer.clear();
	}
}
