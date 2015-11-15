package test;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.SwingUtilities;

public class FakeClient extends Thread {
	private ImagePanel display;
	private InputStream in;

	public FakeClient(ImagePanel display, InputStream in) {
		this.display = display;
		this.in = in;
	}

	@Override
	public void run() {
		while (true) {
			int length = readLength();
			byte[] jpeg = new byte[length];
			int read = 0;
			while (read < length) {
				try {
					read += in.read(jpeg, read, length - read);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			SwingUtilities.invokeLater(new Updater(jpeg));
		}
	}

	private int readLength() {
		int read = 0;
		byte[] length = new byte[4];
		while (read < 4) {
			try {
				read += in.read(length, read, 4 - read);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (length[3] << (8 * 3)) | (length[2] & 0xFF) << (8 * 2) | (length[1] & 0xFF) << (8 * 1)
				| (length[0] & 0xFF);
	}

	private class Updater implements Runnable {
		private byte[] jpeg;

		public Updater(byte[] jpeg) {
			this.jpeg = jpeg;
		}

		@Override
		public void run() {
			display.refresh(jpeg);
		}

	}
}
