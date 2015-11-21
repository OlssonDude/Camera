package client;

public class Image implements Comparable<Image> {
	private int cameraID;
	private byte[] jpeg;
	private long timestamp;

	public Image(int cameraID, long timestamp, byte[] jpeg) {
		super();
		this.cameraID = cameraID;
		this.jpeg = jpeg;
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Image o) {
		return (int) (timestamp - o.timestamp);
	}
	
	public int getCameraID() {
		return cameraID;
	}
	
	public byte[] getImage() {
		return jpeg;
	}

}
