package client;

public class CameraImage implements Comparable<CameraImage> {
	private int cameraID;
	private byte[] jpeg;
	private long timestamp;
	private int delay;

	public CameraImage(int cameraID, long timestamp, byte[] jpeg, int delay) {
		super();
		this.cameraID = cameraID;
		this.jpeg = jpeg;
		this.timestamp = timestamp;
		this.delay = delay;
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
	
	public int getDelay() {
		return delay;
	}
}
