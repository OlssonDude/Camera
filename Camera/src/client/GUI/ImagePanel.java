package client.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ImagePanel extends JPanel {
	ImageIcon icon;
	private JLabel label;
	public ImagePanel() {
		icon = new ImageIcon();
		label = new JLabel(icon);
		add(label, BorderLayout.CENTER);
	}

	public void refresh(byte[] data) {
		Image image = getToolkit().createImage(data);
		getToolkit().prepareImage(image, -1, -1, null);
		icon.setImage(image);
		icon.paintIcon(this, this.getGraphics(), 0, 0);
	}

}
