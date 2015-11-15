package server;

import java.io.IOException;
import java.io.OutputStream;

public class OutputHandler extends Thread {
	private Monitor monitor;
	private OutputStream out;

	public OutputHandler(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		monitor.waitForConnection();
		out = monitor.getOutputStream();
		while (true) {
			if (monitor.isConnected()) {
				ClientPackage toSend = monitor.getNextPackage();
				try {
					out.write(toSend.toByteArray());
				} catch (IOException e) {
					monitor.disconnect();
				}
			} else {
				monitor.waitForConnection();
				out = monitor.getOutputStream();
			}
		}
	}
}
