package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import client.CameraProtocolConstants;

public class Server extends Thread {
	private static final byte[] CRLF = { 13, 10 };
	private int port;
	private ServerSocket serverSocket;
	private Monitor monitor;

	public Server(int port) {
		this.port = port;
		monitor = new Monitor();
		new CameraThread(monitor).start();
		new InputHandler(monitor).start();
		new OutputHandler(monitor).start();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket client = serverSocket.accept();
				handleRequest(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleRequest(Socket client) {
		try {
			String request = getLine(client.getInputStream());
			System.out.println(request);
			if (request.length() >= 4 && request.substring(0, 4).equals("GET ")) {
				handleHTTPRequest(client);
			} else if (request.length() >= 6 && request.substring(0, 6).equals("CLIENT")) {
				monitor.setSocket(client);
			}
		} catch (IOException e) {
		}
	}

	private void handleHTTPRequest(Socket client) {
		try {
			InputStream in = client.getInputStream();
			OutputStream out = client.getOutputStream();

			// Skip header
			String header;
			boolean cont;
			do {
				header = getLine(in);
				cont = !(header.equals(""));
			} while (cont);

			// Write header
			putLine(out, "HTTP/1.0 200 OK");
			putLine(out, "Content-Type: image/jpeg");
			putLine(out, "Pragma: no-cache");
			putLine(out, "Cache-Control: no-cache");
			putLine(out, "");

			byte[] toSend = monitor.getImage().toByteArray();
			out.write(toSend, CameraProtocolConstants.SERVER_IMAGE_HEADER_LENGTH,
					toSend.length - CameraProtocolConstants.SERVER_IMAGE_HEADER_LENGTH);
			out.flush();
			out.close();

		} catch (IOException e) {
		}
	}

	private boolean isHTTP(Socket client) {
		try {
			String request = getLine(client.getInputStream());
			if (request.length() >= 4 && request.substring(0, 4).equals("GET ")) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
		}
		return true;
	}

	private static String getLine(InputStream s) throws IOException {
		boolean done = false;
		String result = "";

		while (!done) {
			int ch = s.read(); // Read
			if (ch <= 0 || ch == 10) {
				// Something < 0 means end of data (closed socket)
				// ASCII 10 (line feed) means end of line
				done = true;
			} else if (ch >= ' ') {
				result += (char) ch;
			}
		}
		return result;
	}

	private static void putLine(OutputStream s, String str) throws IOException {
		s.write(str.getBytes());
		s.write(CRLF);
	}

}
