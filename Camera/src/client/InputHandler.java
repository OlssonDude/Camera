package client;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.SwingUtilities;

import client.GUI.ImagePanel;

public class InputHandler extends Thread {
	public static final byte MOTION_MESSAGE = 0;
	public static final byte IMAGE_MESSAGE = 1;
	private int cameraID;
	private InputStream in;
	private ImageBuffer imgBuffer;
	private MessageBuffer otherMsgBuffer;
	private ImagePanel thisCameraDisplay;
	private ImagePanel otherCameraDisplay;
	private ConnectionMonitor server;
	private MessageBuffer thisMsgBuffer;

	public InputHandler(int cameraID, ImageBuffer imgBuffer, MessageBuffer thisMsgBuffer, MessageBuffer otherMsgBuffer,
			ImagePanel thisCameraDisplay, ImagePanel otherCameraDisplay, ConnectionMonitor server) {
		this.cameraID = cameraID;
		this.imgBuffer = imgBuffer;
		this.thisMsgBuffer = thisMsgBuffer;
		this.otherMsgBuffer = otherMsgBuffer;
		this.thisCameraDisplay = thisCameraDisplay;
		this.otherCameraDisplay = otherCameraDisplay;
		this.server = server;
	}

	@Override
	public void run() {
		in = server.getInputStream();
		while (true) {
			if (server.isConnected()) {
				try {
					byte msgType = readType();
					if (msgType == IMAGE_MESSAGE) {
						handleImage();
					} else if (msgType == MOTION_MESSAGE) {
						handleMotion();
					}
				} catch (IOException e) {
					thisMsgBuffer.setDisconnected(true);
					server.disconnect();
				}
			} else {
				in = server.getInputStream();
			}
		}
	}

	private byte readType() throws IOException {
		byte type = -1;
		type = (byte) in.read();
		if (type == -1) {
			throw new IOException("Disconnected");
		}
		return type;
	}

	private void handleMotion() {
		otherMsgBuffer.addMessage(new ServerMessage(ServerMessage.MOVIE_MESSAGE));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				thisCameraDisplay.setMovieMode(true);
				otherCameraDisplay.setMovieMode(false);
			}
		});

	}

	private void handleImage() throws IOException {
		long timestamp = readTimestamp();
		int length = readLength();
		byte[] jpeg = readImage(length);
		imgBuffer.addImage(new CameraImage(cameraID, timestamp, jpeg));
	}

	private byte[] readImage(int length) throws IOException {
		byte[] jpeg = new byte[length];
		int totRead = 0;
		while (totRead < length) {
			int read = in.read(jpeg, totRead, length - totRead);
			if (read == -1)
				throw new IOException("Disconnected");
			totRead += read;
		}
		return jpeg;
	}

	private long readTimestamp() throws IOException {
		int totRead = 0;
		int read = 0;
		byte[] data = new byte[8];
		while (totRead < 8) {
			read = in.read(data, totRead, 8 - totRead);
			if (read == -1)
				throw new IOException("Disconnected");
			totRead += read;
		}
		long timeStamp = 0;
		for (int i = 0; i < 8; i++) {
			timeStamp <<= 8;
			timeStamp |= (data[i] & 0xFF);
		}
		return timeStamp;
	}

	private int readLength() throws IOException {
		int totRead = 0;
		int read;
		byte[] length = new byte[4];
		while (totRead < 4) {
			read = in.read(length, totRead, 4 - totRead);
			if (read == -1)
				throw new IOException("Disconnected");
			totRead += read;
		}
		return (length[3] << (8 * 3)) | (length[2] & 0xFF) << (8 * 2) | (length[1] & 0xFF) << (8 * 1)
				| (length[0] & 0xFF);
	}
}
