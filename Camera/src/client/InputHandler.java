package client;

import java.io.IOException;
import java.io.InputStream;

public class InputHandler extends Thread {
	public static final byte IMAGE_MESSAGE = 1;
	public static final byte MOTION_MESSAGE = 0;
	private int cameraID;
	private int otherCameraID;
	private InputStream in;
	private ImageBuffer imgBuffer;
	private MessageBuffer msgBuffer;

	public InputHandler(int cameraID, int otherCameraID, ImageBuffer imgBuffer, MessageBuffer msgBuffer, InputStream in) {
		this.cameraID = cameraID;
		this.otherCameraID = otherCameraID;
		this.imgBuffer = imgBuffer;
		this.msgBuffer = msgBuffer;
		this.in = in;
	}

	@Override
	public void run() {
		while (true) {
			byte msgType = readType();
			if (msgType == IMAGE_MESSAGE) {
				handleImage();
			} else if (msgType == MOTION_MESSAGE) {
				handleMotion();
			} else {
				// TODO ? Unknown / end of stream ?
			}
		}
	}

	private byte readType() {
		byte type = -1;
		try {
			type = (byte) in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return type;
	}

	private void handleMotion() {
		System.out.println("Client received motion message"); //TODO - remove when logger implemented
		msgBuffer.addMessage(new ServerMessage(otherCameraID, ServerMessage.MOVIE_MESSAGE));
	}

	private void handleImage() {
		long timestamp = readTimestamp();
		int length = readLength();
		byte[] jpeg = readImage(length);

		imgBuffer.addImage(new CameraImage(cameraID, timestamp, jpeg));
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
