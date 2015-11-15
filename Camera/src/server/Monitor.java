package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Monitor {
	private Socket client;
	private ClientPackage toSend;

	public synchronized void setSocket(Socket client) {
		this.client = client;
		notifyAll();
	}

	public synchronized void waitForDisconnect() {
		while (isConnected()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void waitForConnection() {
		while (!isConnected()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean isConnected() {
		return client != null;
	}

	public synchronized void disconnect() {
		try {
			client.close();
		} catch (Exception e) {
		}
		client = null;
		notifyAll();
	}

	public synchronized void addPackage(ClientPackage clientPackage) {
		toSend = clientPackage;
		notifyAll();
	}

	public synchronized OutputStream getOutputStream() {
		if (isConnected()) {
			try {
				return client.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public synchronized ClientPackage getNextPackage() {
		while (toSend == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClientPackage toReturn = toSend;
		toSend = null;
		return toReturn;
	}

}
