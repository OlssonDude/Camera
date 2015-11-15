package test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ServerTest {

	public static void main(String[] args) {
		ImagePanel c1 = new ImagePanel();
		ImagePanel c2 = new ImagePanel();
		
		new TestGUI(c1,c2);
		
		try {
			Socket s1 = new Socket("localhost", 8888);
			new FakeClient(c1, s1.getInputStream()).start();
			Socket s2 = new Socket("localhost", 8890);
			new FakeClient(c2, s2.getInputStream()).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
