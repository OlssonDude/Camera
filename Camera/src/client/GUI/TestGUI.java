package client.GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

public class TestGUI extends JFrame {

	public TestGUI(ImagePanel cameraLeft, ImagePanel cameraRight, ConnectionHandling leftConnection, ConnectionHandling rightConnection) {
		super("Argus Camera Surveillance");
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);
		add(leftConnection, c);
		add(rightConnection, c);
		c.gridwidth = 1;
		c.gridy = 1;
		add(cameraLeft, c);
		c.gridx = 1;
		add(cameraRight, c);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setBackground(Color.BLACK);
		setVisible(true);
	}
}