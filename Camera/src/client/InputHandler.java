package client;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.SwingUtilities;

import client.GUI.ImagePanel;

public class InputHandler extends Thread {
	public static final byte MOTION_MESSAGE = 0;
	public static final byte IMAGE_MESSAGE = 1;
	private int cameraID;
	private int otherCameraID;
	private InputStream in;
	private ImageBuffer imgBuffer;
	private MessageBuffer msgBuffer;
	private ImagePanel thisCameraDisplay;
	private ImagePanel otherCameraDisplay;

	public InputHandler(int cameraID, int otherCameraID, ImageBuffer imgBuffer, MessageBuffer msgBuffer, InputStream in,
			ImagePanel thisCameraDisplay, ImagePanel otherCameraDisplay) {
		this.cameraID = cameraID;
		this.otherCameraID = otherCameraID;
		this.imgBuffer = imgBuffer;
		this.msgBuffer = msgBuffer;
		this.in = in;
		this.thisCameraDisplay = thisCameraDisplay;
		this.otherCameraDisplay = otherCameraDisplay;
	}

	@Override
	public void run() {
		while (true) {
			byte msgType = readType();
			if (msgType == IMAGE_MESSAGE) {
				handleImage();
			} else if(msgType == MOTION_MESSAGE){
				handleMotion();
			} else {
				// TODO ? Unknown message/end of stream
			}
		}
	}

	private byte readType() {
		byte type = -1;
		try {
			type = (byte) in.read();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO - Handle connection lost
		}
		return type;
	}

	private void handleMotion() {
		msgBuffer.addMessage(new ServerMessage(otherCameraID, ServerMessage.MOVIE_MESSAGE));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				thisCameraDisplay.setMovieMode(true);
				otherCameraDisplay.setMovieMode(false);
			}
		});

	}

	private void handleImage() {
		long timestamp = readTimestamp();
		int length = readLength();
		byte[] jpeg = readImage(length);
		int delay = (int) (System.currentTimeMillis() - timestamp);
		imgBuffer.addImage(new CameraImage(cameraID, timestamp, jpeg, delay));
	}

	private byte[] readImage(int length) {
		byte[] jpeg = new byte[length];
		int read = 0;
		while (read < length) {
			try {
				read += in.read(jpeg, read, length - read);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jpeg;
	}

	private long readTimestamp() {
		int read = 0;
		byte[] data = new byte[8];
		while (read < 8) {
			try {
				read += in.read(data, read, 8 - read);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long timeStamp = 0;
		for (int i = 0; i < 8; i++) {
			timeStamp <<= 8;
			timeStamp |= (data[i] & 0xFF);
		}
		return timeStamp;
	}

	private int readLength() {
		int read = 0;
		byte[] length = new byte[4];
		while (read < 4) {
			try {
				read += in.read(length, read, 4 - read);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (length[3] << (8 * 3)) | (length[2] & 0xFF) << (8 * 2) | (length[1] & 0xFF) << (8 * 1)
				| (length[0] & 0xFF);
	}
}
