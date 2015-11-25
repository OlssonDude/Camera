package client;

import java.util.PriorityQueue;

// TODO - Add functionality for checking synchronized-mode

public class ImageBuffer {
	private PriorityQueue<CameraImage> buffer;	
	public ImageBuffer() {
		buffer = new PriorityQueue<CameraImage>();
	}
	
	public synchronized CameraImage getNextImage() {
		while(buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buffer.poll();
	}
	
	public synchronized void addImage(CameraImage image) {
		buffer.add(image);
		notifyAll();
	}
}
