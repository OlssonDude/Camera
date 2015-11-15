package client.GUI;

import java.awt.GridLayout;
import javax.swing.JFrame;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class TestGUI extends JFrame {

	public TestGUI(ImagePanel cameraLeft, ImagePanel cameraRigth) {

		setLayout(new GridLayout(1, 2, 10, 0));
		add(cameraLeft);
		add(cameraRigth);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(AxisM3006V.IMAGE_WIDTH * 2, AxisM3006V.IMAGE_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}