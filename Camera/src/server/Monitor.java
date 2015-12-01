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
	private boolean forceMovie;
	private boolean forceIdle;
	private long imageGetTime;

	public Monitor() {

	}

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

	public synchronized void disconnect() {
		if (isConnected()) {
			try {
				client.getOutputStream().flush();
				client.close();
			} catch (Exception e) {
			}
			client = null;
			movie = false;
			motionMessageSent = false;
			notifyAll();
		}
	}

	public synchronized void addImage(ClientPackage clientPackage) {
		image = clientPackage;
		notifyAll();
	}

	public synchronized OutputStream getOutputStream() {
		while (!isConnected()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			return client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
		return getOutputStream();
	}

	public synchronized InputStream getInputStream() {
		while (!isConnected()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			return client.getInputStream();
		} catch (IOException e) {
			disconnect();
			e.printStackTrace();
		}
		return getInputStream();
	}

	private boolean isIdleWait(long toWait) {
		if (forceMovie) {
			return false;
		}

		if (forceIdle || !movie) {
			return toWait > 0;
		}

		return false;
	}

	public synchronized ClientPackage getImage() {
		long toWait = imageGetTime - System.currentTimeMillis();
		while (isIdleWait(toWait)) { // !forceMovie && (((forceIdle || !movie)
										// && toWait > 0))
			System.out.println("Waiting");
			try {
				wait(toWait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			toWait = imageGetTime - System.currentTimeMillis();
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
		if (isConnected()) {
			if (!movie) {
				motionMessageSent = false;
			}
			this.movie = movie;
			notifyAll();
		}
	}

	public synchronized boolean MotionMessageSent() {
		return motionMessageSent;
	}

	public synchronized void setMotionMessageSent(boolean status) {
		motionMessageSent = status;
	}

	public synchronized void forceMovie() {
		forceIdle = false;
		forceMovie = true;
	}

	public synchronized void forceIdle() {
		forceMovie = false;
		forceIdle = true;
	}

	public synchronized void forceNone() {
		forceMovie = false;
		forceIdle = false;
	}

}
