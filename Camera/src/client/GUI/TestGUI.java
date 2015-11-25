package client.GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TestGUI extends JFrame {

	public TestGUI(ImagePanel cameraLeft, ImagePanel cameraRight) {

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		addComponent(c, cameraLeft, 0, 0, 1, 3, GridBagConstraints.NONE, 0, 0);
		addComponent(c, cameraRight, 1, 0, 1, 3, GridBagConstraints.NONE, 0, 0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addComponent(GridBagConstraints c, JComponent component, int x, int y) {
		addComponent(c, component, x, y, 1, 1, GridBagConstraints.NONE, 0, 0);
	}

	private void addComponent(GridBagConstraints c, JComponent component, int x, int y, int width, int height, int fill,
			double wx, double wy) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		c.fill = fill;
		c.weightx = wx;
		c.weighty = wy;
		add(component, c);
	}

}