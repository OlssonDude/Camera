package client;

public class Image {
	private int cameraID;
	private byte[] jpeg;
	private long timestamp;

	public Image(int cameraID, long timestamp, byte[] jpeg) {
		super();
		this.cameraID = cameraID;
		this.jpeg = jpeg;
		this.timestamp = timestamp;
	}

}
