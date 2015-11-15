package server;

public class InputHandler extends Thread {
	private Monitor monitor;
	
	public InputHandler(Monitor monitor) {
		this.monitor = monitor;
	}

}
