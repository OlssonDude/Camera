package server;

public class CameraThread extends Thread {
	private Monitor monitor;

	public CameraThread(Monitor monitor) {
		this.monitor = monitor;
	}
}
