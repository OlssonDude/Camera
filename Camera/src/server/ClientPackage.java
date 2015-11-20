package server;

public class ClientPackage {
	private byte[] packageData;
	private boolean motionDetected;
	public static final int HEADER_LENGTH = 13;
	public static final byte IMAGE_MESSAGE = 1;
	public static final byte MOTION_MESSAGE = 0;

	public ClientPackage(boolean motionDetected, byte[] timestamp, int length, byte[] jpeg) {
		this.motionDetected = motionDetected;
		packageData = new byte[HEADER_LENGTH + length];
		packageData[0] = IMAGE_MESSAGE;
		System.arraycopy(timestamp, 0, packageData, 1, 8);
		addLength(length);
		System.arraycopy(jpeg, 0, packageData, HEADER_LENGTH, length);
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
