package client;

import java.util.PriorityQueue;

// TODO - Add functionality for checking synchronized-mode

public class ImageBuffer {
	PriorityQueue<Image> buffer;
	
	public ImageBuffer() {
		buffer = new PriorityQueue<Image>();
	}
	
	public synchronized Image getNextImage() {
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
	
	public synchronized void addImage(Image image) {
		buffer.add(image);
		notifyAll();
	}

}
