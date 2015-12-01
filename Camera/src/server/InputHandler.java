package server;

import java.io.IOException;
import java.io.InputStream;

import client.CameraProtocolConstants;

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
					switch (message) {
					case CameraProtocolConstants.CLIENT_DISCONNECT_MESSAGE:
						monitor.disconnect();
						System.out.println("Disconenct Recieved");
						break;
					case CameraProtocolConstants.CLIENT_IDLE_MESSAGE:
						monitor.setMovieMode(false);
						System.out.println("Idle Recieved");
						break;
					case CameraProtocolConstants.CLIENT_MOVIE_MESSAGE:
						monitor.setMovieMode(true);
						System.out.println("Movie Recieved");
						break;
					case CameraProtocolConstants.CLIENT_FORCE_IDLE:
						monitor.forceIdle();
						System.out.println("Force Idle Recieved");
						break;
					case CameraProtocolConstants.CLIENT_FORCE_MOVIE:
						monitor.forceMovie();
						System.out.println("Force Movie Recieved");
						break;
					case CameraProtocolConstants.CLIENT_FORCE_NONE:
						monitor.forceNone();
						System.out.println("Force None Recieved");
						break;
					case -1:
							monitor.disconnect();
						break;
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
