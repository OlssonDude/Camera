package client.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;

public class TestGUI extends JFrame {

	public TestGUI(ImagePanel cameraLeft, ImagePanel cameraRight) {

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);
		add(cameraLeft, c);
		c.gridx = 1;
		add(cameraRight, c);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}