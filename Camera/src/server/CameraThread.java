package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

//TODO - Add Motiondetection 

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
			int length =  camera.getJPEG(jpeg, 0);
			monitor.addPackage(new ClientPackage(length, jpeg));
		}
	}
}
