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
			System.out.println("Server " + port + " started");
			while (true) {
				Socket client = serverSocket.accept();
				System.out.println("Client connected to " + port);
				monitor.setSocket(client);
				monitor.waitForDisconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
