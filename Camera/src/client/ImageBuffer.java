package client;

import java.util.LinkedList;
import java.util.PriorityQueue;

/*
 * TODO Ingen aning om det är såhär synch ska fungera
 */

public class ImageBuffer {
	public static final int SYNCH_TRESHOLD = 200;
	public static final int DELAY_HISTORY_SIZE = 20;
	private PriorityQueue<CameraImage> buffer;
	private boolean synchMode;
	private boolean forcedSynchMode;
	private boolean forcedAsynchMode;
	private long prevReceiveTime;
	private int notInSynchCount;
	private LinkedList<Integer> delayHistory;
	private int averageDelay;

	public ImageBuffer() {
		buffer = new PriorityQueue<CameraImage>();
		delayHistory = new LinkedList<>();
	}

	public synchronized CameraImage getNextImage() {
		while (buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		CameraImage image = buffer.poll();
		if ((synchMode && !forcedAsynchMode) || forcedSynchMode) {
			long diff;
			while ((diff = image.getDisplayTime() - System.currentTimeMillis()) > 0) {
				try {
					wait(diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return image;
	}

	public synchronized void addImage(CameraImage image) {
		synchHandling(image);
		buffer.add(image);
		notifyAll();
	}

	// TODO better name
	private void synchHandling(CameraImage img) {
		long currentReceiveTime = System.currentTimeMillis();
		delayHistory.addLast((int) (currentReceiveTime - img.getTimestamp()));
		if (delayHistory.size() >= 20)
			delayHistory.pollFirst();
		int sum = 0;
		for (int d : delayHistory) {
			sum += d;
		}
		averageDelay = sum / delayHistory.size();

		boolean inSynchWindow = currentReceiveTime - prevReceiveTime < SYNCH_TRESHOLD;
		if (!synchMode && inSynchWindow) {
			synchMode = true;
		}
		if (synchMode) {
			if (!inSynchWindow) {
				synchMode = (++notInSynchCount >= 3) ? false : true;
			} else {
				notInSynchCount = 0;
			}
			img.setDisplayTime(img.getTimestamp() + averageDelay);
		}
		prevReceiveTime = currentReceiveTime;
	}

	public synchronized void setForced(boolean synch, boolean asynch) {
		forcedSynchMode = synch;
		forcedAsynchMode = asynch;
	}

}
