package client;

import javax.swing.SwingUtilities;

import client.GUI.ImagePanel;

public class ImageUpdater extends Thread {
	private ImagePanel leftCamera;
	private ImagePanel rightCamera;
	private ImageBuffer imgBuffer;
	
	public ImageUpdater(ImagePanel leftCamera, ImagePanel rightCamera, ImageBuffer imgBuffer) {
		this.leftCamera = leftCamera;
		this.rightCamera = rightCamera;
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
				leftCamera.refresh(img.getImage());
			} else {
				rightCamera.refresh(img.getImage());
			}
		}

	}
}
