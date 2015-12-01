package server;

import java.io.IOException;
import java.io.OutputStream;

import client.CameraProtocolConstants;

public class OutputHandler extends Thread {
	private Monitor monitor;
	private OutputStream out;

	public OutputHandler(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		out = monitor.getOutputStream();
		while (true) {
			if (monitor.isConnected()) {
				ClientPackage toSend = monitor.getImage();
				try {
					if (toSend.motionDetected() && !monitor.MotionMessageSent()) {
						out.write(CameraProtocolConstants.SERVER_MOTION_MESSAGE);
						monitor.setMotionMessageSent(true);
					}
					out.write(toSend.toByteArray());
				} catch (IOException e) {
					monitor.disconnect();
				}
			} else {
				out = monitor.getOutputStream();
			}
		}
	}
}
