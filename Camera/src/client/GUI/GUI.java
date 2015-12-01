package client.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame {
	private JLabel synchMode;
	// FIXME Dispaly current synch mode in GUI

	public GUI(ImagePanel cameraLeft, ImagePanel cameraRight, ConnectionHandling leftConnection,
			ConnectionHandling rightConnection) {
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

		synchMode = new JLabel("Asynchronized");
		synchMode.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		add(synchMode, c);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setBackground(Color.BLACK);
		setVisible(true);
	}

	public void setSynchMode(boolean synch) {
		if (synch) {
			synchMode.setText("Synchronized");
		} else {
			synchMode.setText("Asynchronized");
		}
	}

}