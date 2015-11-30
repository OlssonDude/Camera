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
		out = server.getOutputStream();
		buffer.clear();
		while (true) {
			if (server.isConnected()) {
				try {
					ServerMessage msg = buffer.getMessage();
					if (msg == null) {
						out = server.getOutputStream();
						buffer.clear();
					} else {
						out.write(msg.toByte());
					}
				} catch (IOException e) {
					System.out.println("MessageHandler Disconnected"); // TODO print
				}
			} else {
				out = server.getOutputStream();
				buffer.clear();
			}
		}
	}
}
