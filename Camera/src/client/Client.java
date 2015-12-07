package client;

import javax.swing.SwingUtilities;

import client.GUI.ConnectionHandling;
import client.GUI.DebugFrame;
import client.GUI.ImagePanel;
import client.GUI.GUI;

public class Client {
	public static final int LEFT_CAMERA = 0;
	public static final int RIGHT_CAMERA = 1;
	private ImageBuffer imgBuffer;
	private MessageBuffer leftMsgBuffer;
	private MessageBuffer rightMsgBuffer;
	private ConnectionMonitor leftConnectionMonitor;
	private ConnectionMonitor rightConnectionMonitor;
	private ImagePanel cameraLeft;
	private ImagePanel cameraRight;
	private ConnectionHandling leftConnectionHandling;
	private ConnectionHandling rightConnectionHandling;
	private GUI gui;

	public Client() throws Exception {
		imgBuffer = new ImageBuffer();
		leftMsgBuffer = new MessageBuffer();
		rightMsgBuffer = new MessageBuffer();
		leftConnectionMonitor = new ConnectionMonitor();
		rightConnectionMonitor = new ConnectionMonitor();

		SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {
				cameraLeft = new ImagePanel(leftMsgBuffer);
				cameraRight = new ImagePanel(rightMsgBuffer);
				leftConnectionHandling = new ConnectionHandling(leftConnectionMonitor, cameraLeft);
				rightConnectionHandling = new ConnectionHandling(rightConnectionMonitor, cameraRight);
				gui = new GUI(cameraLeft, cameraRight, leftConnectionHandling, rightConnectionHandling);
				new DebugFrame(imgBuffer, cameraLeft, cameraRight, leftMsgBuffer, rightMsgBuffer, gui);
			}
		});

		new ImageUpdater(cameraLeft, cameraRight, gui, imgBuffer).start();
		cameraSetup(LEFT_CAMERA, leftMsgBuffer, rightMsgBuffer, cameraLeft, cameraRight, leftConnectionMonitor,
				rightConnectionMonitor);
		cameraSetup(RIGHT_CAMERA, rightMsgBuffer, leftMsgBuffer, cameraRight, cameraLeft, rightConnectionMonitor,
				leftConnectionMonitor);
	}

	private void cameraSetup(int id, MessageBuffer thisCamMessageBuffer, MessageBuffer otherCamMessageBuffer,
			ImagePanel thisCamDisplay, ImagePanel otherCamDisplay, ConnectionMonitor thisServer,
			ConnectionMonitor otherServer) {
		new InputHandler(id, imgBuffer, thisCamMessageBuffer, otherCamMessageBuffer, thisCamDisplay, otherCamDisplay,
				thisServer).start();
		new MessageHandler(otherCamMessageBuffer, otherServer).start();
	}
}
