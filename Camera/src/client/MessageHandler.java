package client;

import java.io.IOException;
import java.io.OutputStream;

public class MessageHandler extends Thread {
	private MessageBuffer buffer;
	private OutputStream out1;
	private OutputStream out2;

	public MessageHandler(MessageBuffer buffer, OutputStream out1, OutputStream out2) {
		this.buffer = buffer;
		this.out1 = out1;
		this.out2 = out2;
	}

	@Override
	public void run() {
		while (true) {
			ServerMessage message = buffer.getMessage();
			if (message.getId() == 1) {
				try {
					out1.write(message.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					out2.write(message.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
