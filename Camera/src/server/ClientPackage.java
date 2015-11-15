package server;

//TODO - Add rest of package (1 byte type, 8 byte timestamp) --> modify header lenght 

public class ClientPackage {
	private byte[] clientPackage;
	public static final int HEADR_LENGTH = 4;

	public ClientPackage(int length, byte[] jpeg) {

		clientPackage = new byte[HEADR_LENGTH + length];
		addLength(length);

		System.arraycopy(jpeg, 0, clientPackage, 4, length);
	}

	/**
	 * Converts length as integer to bytes and adds to client package
	 * 
	 * @param length
	 */
	private void addLength(int length) {
		clientPackage[3] = (byte) (length >> 8 * 3);
		clientPackage[2] = (byte) (length >> 8 * 2);
		clientPackage[1] = (byte) (length >> 8);
		clientPackage[0] = (byte) length;
	}

	public byte[] toByteArray() {
		return clientPackage;
	}

}
