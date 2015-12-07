package server;

import client.CameraProtocolConstants;

public class ImagePackage {
	private byte[] packageData;
	private boolean motionDetected;

	public ImagePackage(boolean motionDetected, byte[] timestamp, int length, byte[] jpeg) {
		this.motionDetected = motionDetected;
		packageData = new byte[CameraProtocolConstants.SERVER_IMAGE_HEADER_LENGTH + length];
		packageData[0] = CameraProtocolConstants.SERVER_IMAGE_MESSAGE;
		System.arraycopy(timestamp, 0, packageData, 1, 8);
		addLength(length);
		System.arraycopy(jpeg, 0, packageData, CameraProtocolConstants.SERVER_IMAGE_HEADER_LENGTH, length);
	}

	private void addLength(int length) {
		for (int i = 0; i < 4; i++) {
			packageData[i + 9] = (byte) (length >> 8 * i);
		}
	}

	public boolean motionDetected() {
		return motionDetected;
	}

	public byte[] toByteArray() {
		return packageData;
	}

}
