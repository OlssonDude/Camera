package test;

import java.net.Socket;

import client.ImageBuffer;
import client.ImageUpdater;
import client.InputHandler;
import client.MessageBuffer;
import client.MessageHandler;
import client.GUI.ImagePanel;
import client.GUI.TestGUI;
import server.Server;

public class MainTest {
	private static int LEFT = 0;
	private static int RIGHT = 1;
	
	public static void main(String[] args) throws Exception {
		new Server(8888).start();
		new Server(8890).start();

		ImagePanel cameraLeft = new ImagePanel();
		ImagePanel cameraRight = new ImagePanel();
		new TestGUI(cameraLeft, cameraRight);

		Socket left = new Socket("localhost", 8888);
		Socket right = new Socket("localhost", 8890);
		ImageBuffer imgBuffer = new ImageBuffer();
		MessageBuffer msgBuffer = new MessageBuffer();
		new InputHandler(LEFT, RIGHT, imgBuffer, msgBuffer, left.getInputStream()).start();
		new InputHandler(RIGHT, LEFT, imgBuffer, msgBuffer, right.getInputStream()).start();
		new ImageUpdater(cameraLeft, cameraRight, imgBuffer).start();
		new MessageHandler(msgBuffer, left.getOutputStream(), right.getOutputStream());
	}

}
