package client;

import javax.swing.SwingUtilities;

import client.GUI.GUI;
import client.GUI.ImagePanel;

public class ImageUpdater extends Thread {
	private ImagePanel cameraLeft;
	private ImagePanel cameraRight;
	private GUI gui;
	private ImageBuffer imgBuffer;

	public ImageUpdater(ImagePanel cameraLeft, ImagePanel cameraRight, GUI gui, ImageBuffer imgBuffer) {
		this.cameraLeft = cameraLeft;
		this.cameraRight = cameraRight;
		this.gui = gui;
		this.imgBuffer = imgBuffer;
	}

	@Override
	public void run() {
		while (true) {
			SwingUtilities.invokeLater(new Updater(imgBuffer.getNextImage()));
		}
	}

	private class Updater implements Runnable {
		private CameraImage img;

		public Updater(CameraImage img) {
			this.img = img;
		}

		@Override
		public void run() {
			if (img.getCameraID() == Client.LEFT_CAMERA) {
				cameraLeft.refresh(img);
			} else {
				cameraRight.refresh(img);
			}
			gui.setSynchMode(img.isSynchMode());
		}
	}
}
