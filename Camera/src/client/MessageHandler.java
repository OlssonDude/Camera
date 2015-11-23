package client;

import java.io.IOException;
import java.io.OutputStream;

public class MessageHandler extends Thread {
	private MessageBuffer buffer;
	private OutputStream out0;
	private OutputStream out1;

	public MessageHandler(MessageBuffer buffer, OutputStream out0, OutputStream out1) {
		this.buffer = buffer;
		this.out0 = out0;
		this.out1 = out1;
	}

	@Override
	public void run() {
		while (true) {
			ServerMessage message = buffer.getMessage();
			if (message.getId() == 1) {
				try {
					out0.write(message.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					out1.write(message.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
