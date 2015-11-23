package server;

import java.io.IOException;
import java.io.InputStream;

import client.ServerMessage;

public class InputHandler extends Thread {
	private Monitor monitor;
	private InputStream in;

	public InputHandler(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		in = monitor.getInputStream();
		while (true) {
			if (monitor.isConnected()) {
				try {
					byte message = (byte) in.read();
					if (message == ServerMessage.DISCONNECT_MESSAGE) {
						monitor.disconnect();
						System.out.println("Disconenct Recieved"); // TODO - Replace by logger
					} else if (message == ServerMessage.IDLE_MESSAGE) {
						monitor.setMovieMode(false);
						System.out.println("Idle Recieved"); // TODO - Replace by logger
					} else if (message == ServerMessage.MOVIE_MESSAGE) {
						monitor.setMovieMode(true);
						System.out.println("Movie Recieved"); // TODO - Replace by logger
					}
				} catch (IOException e) {
					monitor.disconnect();
				}
			} else {
				in = monitor.getInputStream();
			}
		}
	}
}
