package client;

public class CameraImage implements Comparable<CameraImage> {
	private int cameraID;
	private byte[] jpeg;
	private long timestamp;
	private long displayTime;

	public CameraImage(int cameraID, long timestamp, byte[] jpeg) {
		super();
		this.cameraID = cameraID;
		this.jpeg = jpeg;
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(CameraImage o) {
		return (int) (timestamp - o.timestamp);
	}

	public int getCameraID() {
		return cameraID;
	}

	public byte[] getImage() {
		return jpeg;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setDisplayTime(long time) {
		displayTime = time;
	}

	public long getDisplayTime() {
		return displayTime;
	}
}
