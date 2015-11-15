package server;

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
	
	public synchronized boolean isConnected() {
		return client != null;
	}

	public synchronized void addPackage(ClientPackage clientPackage) {
		toSend = clientPackage;
		notifyAll();
	}

}
