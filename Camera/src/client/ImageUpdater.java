package client;

import javax.swing.SwingUtilities;

import client.GUI.ImagePanel;

public class ImageUpdater extends Thread {
	private ImagePanel cameraLeft;
	private ImagePanel cameraRight;
	private ImageBuffer imgBuffer;
	
	public ImageUpdater(ImagePanel cameraLeft, ImagePanel cameraRight, ImageBuffer imgBuffer) {
		this.cameraLeft = cameraLeft;
		this.cameraRight = cameraRight;
		this.imgBuffer = imgBuffer;
	}

	@Override
	public void run() {
		while(true) {
			SwingUtilities.invokeLater(new Updater(imgBuffer.getNextImage()));
		}
	}

	private class Updater implements Runnable {
		private Image img;

		public Updater(Image img) {
			this.img = img;
		}

		@Override
		public void run() {
			if(img.getCameraID() == 0) {
				cameraLeft.refresh(img.getImage());
			} else {
				cameraRight.refresh(img.getImage());
			}
		}

	}
}
