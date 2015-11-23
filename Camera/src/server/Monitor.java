package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Monitor {
	public static final int IDLE_WAIT_TIME = 5000;
	private Socket client;
	private ClientPackage image;
	private boolean movie;
	private boolean motionMessageSent;
	private long imageGetTime;

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
		motionMessageSent = false;
		movie = false;
		notifyAll();
	}

	public synchronized void addImage(ClientPackage clientPackage) {
		image = clientPackage;
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

	public synchronized ClientPackage getImage() {
		long toWait;
		while (!movie && (toWait = imageGetTime - System.currentTimeMillis()) > 0) {
			try {
				wait(toWait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (image == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ClientPackage toReturn = image;
		image = null;
		imageGetTime = System.currentTimeMillis() + IDLE_WAIT_TIME;
		return toReturn;
	}

	public synchronized void setMovieMode(boolean movie) {
		if (!movie)
			motionMessageSent = false;
		this.movie = movie;
		notifyAll();
	}

	public synchronized boolean isMotionMessageSent() {
		return motionMessageSent;
	}

	public synchronized void setMotionMessageSent(boolean status) {
		motionMessageSent = status;
	}

	public synchronized InputStream getInputStream() {
		while(!isConnected()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			return client.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
