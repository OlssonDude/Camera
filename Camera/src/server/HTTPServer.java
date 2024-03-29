package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;

// Provides AxisM3006V
import se.lth.cs.eda040.fakecamera.AxisM3006V;

/**
 * Itsy bitsy teeny weeny web server. Always returns an image, regardless of the
 * requested file name.
 */
public class HTTPServer {

	// ----------------------------------------------------------- MAIN PROGRAM

	public static void main(String[] args) {
		HTTPServer theServer = new HTTPServer(Integer.parseInt(args[0]));
		try {
			theServer.handleRequests();
		} catch (IOException e) {
			System.out.println("Error!");
			theServer.destroy();
			System.exit(1);
		}
	}

	// ------------------------------------------------------------ CONSTRUCTOR

	/**
	 * @param port
	 *            The TCP port the server should listen to
	 */
	public HTTPServer(int port) {
		myPort = port;
		myCamera = new AxisM3006V();
		myCamera.init();
		myCamera.setProxy("argus-1.student.lth.se", port);
	}

	// --------------------------------------------------------- PUBLIC METHODS

	/**
	 * This method handles client requests. Runs in an eternal loop that does
	 * the following:
	 * <UL>
	 * <LI>Waits for a client to connect
	 * <LI>Reads a request from that client
	 * <LI>Sends a JPEG image from the camera (if it's a GET request)
	 * <LI>Closes the socket, i.e. disconnects from the client.
	 * </UL>
	 *
	 * Two simple help methods (getLine/putLine) are used to read/write entire
	 * text lines from/to streams. Their implementations follow below.
	 */
	public void handleRequests() throws IOException {
		byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		ServerSocket serverSocket = new ServerSocket(myPort);
		System.out.println("HTTP server operating at port " + myPort + ".");

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				InputStream is = clientSocket.getInputStream();
				OutputStream os = clientSocket.getOutputStream();
				// Read the request
				String request = getLine(is);
				// The request is followed by some additional header lines,
				// followed by a blank line. Those header lines are ignored.
				String header;
				boolean cont;
				do {
					header = getLine(is);
					cont = !(header.equals(""));
				} while (cont);

				System.out.println("HTTP request '" + request + "' received.");

				// Interpret the request. Complain about everything but GET.
				// Ignore the file name.
				if (request.substring(0, 4).equals("GET ")) {
					// Got a GET request. Respond with a JPEG image from the
					// camera. Tell the client not to cache the image
					putLine(os, "HTTP/1.0 200 OK");
					putLine(os, "Content-Type: image/jpeg");
					putLine(os, "Pragma: no-cache");
					putLine(os, "Cache-Control: no-cache");
					putLine(os, ""); // Means 'end of header'

					if (!myCamera.connect()) {
						System.out.println("Failed to connect to camera!");
						System.exit(1);
					}
					int len = myCamera.getJPEG(jpeg, 0);

					os.write(jpeg, 0, len);
					myCamera.close();
				} else {
					// Got some other request. Respond with an error message.
					putLine(os, "HTTP/1.0 501 Method not implemented");
					putLine(os, "Content-Type: text/plain");
					putLine(os, "");
					putLine(os, "No can do. Request '" + request + "' not understood.");
				}

				os.flush(); // Flush any remaining content
				clientSocket.close(); // Disconnect from the client
			} catch (IOException e) {
				System.out.println("Caught exception " + e);
			}
		}
	}

	public void destroy() {
		myCamera.destroy();
	}

	// -------------------------------------------------------- PRIVATE METHODS

	/**
	 * Read a line from InputStream 's', terminated by CRLF. The CRLF is not
	 * included in the returned string.
	 */
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

	/**
	 * Send a line on OutputStream 's', terminated by CRLF. The CRLF should not
	 * be included in the string str.
	 */
	private static void putLine(OutputStream s, String str) throws IOException {
		s.write(str.getBytes());
		s.write(CRLF);
	}

	// ----------------------------------------------------- PRIVATE ATTRIBUTES

	private int myPort; // TCP port for HTTP server
	private AxisM3006V myCamera; // Makes up the JPEG images

	// By convention, these bytes are always sent between lines
	// (CR = 13 = carriage return, LF = 10 = line feed)

	private static final byte[] CRLF = { 13, 10 };
}