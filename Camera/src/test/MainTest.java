package test;

import client.Client;
import server.Server;

public class MainTest {

	public static void main(String[] args) throws Exception {
		new Server(8888).start();
		new Server(8890).start();
		new Client();
	}

}
