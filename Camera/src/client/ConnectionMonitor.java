package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionMonitor {
	private Socket server;

	public synchronized boolean connect(String host, int port) {
		try {
			server = new Socket(host, port);
			server.setTcpNoDelay(true);
			notifyAll();
			return true;
		} catch (Exception e) {
			System.out.println("Unable to connect to " + host + " at port " + port); // TODO
																						// print
			return false;
		}
	}

	public synchronized void disconnect() {
		if (isConnected()) {
			try {
				server.getOutputStream().flush();
				server.close();
			} catch (IOException e) {
			}
			server = null;
		}
	}

	public synchronized boolean isConnected() {
		return server != null;
	}

	public synchronized InputStream getInputStream() {
		waitForConnection();
		InputStream in = null;
		try {
			in = server.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

	public synchronized OutputStream getOutputStream() {
		waitForConnection();
		OutputStream out = null;
		try {
			out = server.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	private synchronized void waitForConnection() {
		while (server == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
