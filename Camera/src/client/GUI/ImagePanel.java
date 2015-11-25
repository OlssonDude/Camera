package client.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import client.CameraImage;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

/*
 * Reuse in client GUI
 */

public class ImagePanel extends JPanel {
	ImageIcon icon;
	private JLabel mode;
	private JLabel camera;
	private JLabel delay;
	private JPanel cameraPanel;

	public ImagePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

		mode = new JLabel("Idle");
		mode.setFont(font);
		c.gridx = 0;
		c.gridy = 0;
		add(mode, c);

		icon = new ImageIcon();
		camera = new JLabel(icon);
		cameraPanel = new JPanel();
		cameraPanel.add(camera);
		c.gridy = 1;
		camera.setPreferredSize(new Dimension(AxisM3006V.IMAGE_WIDTH, AxisM3006V.IMAGE_HEIGHT));
		cameraPanel.setBorder(new MovieBorder(Color.GREEN, 2));
		add(cameraPanel, c);

		delay = new JLabel("Delay: 20ms");
		delay.setFont(font);
		c.gridy = 2;
		add(delay, c);
	}

	public void refresh(CameraImage data) {
		Image image = getToolkit().createImage(data.getImage());
		getToolkit().prepareImage(image, -1, -1, null);
		icon.setImage(image);
		icon.paintIcon(camera, camera.getGraphics(), 0, 0);
		delay.setText("Delay: " + data.getDelay() + "ms");
	}
	
	public void setMovieMode(boolean movie, boolean trigger) {
		if(movie) {
			cameraPanel.setBorder(new MovieBorder(Color.RED, 2));
			if(trigger) {
				mode.setText("Movie (TRIGGER)");
			} else {
				mode.setText("Movie");
			}
		} else {
			cameraPanel.setBorder(new MovieBorder(Color.GREEN, 2));
			mode.setText("Idle");
		}
	}

	private class MovieBorder extends LineBorder {
		private Insets insets = new Insets(-3, -3, -3, -3);

		public MovieBorder(Color color, int thickness) {
			super(color, thickness);
			// TODO Auto-generated constructor stub
		}

		public Insets getBorderInsets(Component c) {
			return insets;
		}

	}

}
