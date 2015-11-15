package server;

public class OutputHandler extends Thread {
	private Monitor monitor;

	public OutputHandler(Monitor monitor) {
		this.monitor = monitor;
	}
}
