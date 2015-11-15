package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	int port;
	private ServerSocket serverSocket;
	private Monitor monitor;

	public Server(int port) {
		this.port = port;
		monitor = new Monitor();
		new CameraThread(monitor).start();
		new InputHandler(monitor).start();
		new OutputHandler(monitor).start();
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket client = serverSocket.accept();
				monitor.setSocket(client);
				monitor.waitForDisconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Server(8888).start();
		new Server(8890).start();
	}
}
