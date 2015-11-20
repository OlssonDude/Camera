package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;


public class CameraThread extends Thread {
	private Monitor monitor;
	private AxisM3006V camera;

	public CameraThread(Monitor monitor) {
		this.monitor = monitor;
		camera = new AxisM3006V();
		camera.init();
		camera.connect();
	}

	@Override
	public void run() {
		while (true) {
			byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
			byte[] timestamp = new byte[8];
			int length = camera.getJPEG(jpeg, 0);
			camera.getTime(timestamp, 0);
			boolean motionDetected = camera.motionDetected();
			monitor.addImage(new ClientPackage(motionDetected, timestamp, length, jpeg));
			
			if(motionDetected) {
				monitor.setMovieMode(true);
			}
		}
	}
}
