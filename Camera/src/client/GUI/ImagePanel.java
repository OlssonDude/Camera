package client.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import client.CameraImage;
import client.MessageBuffer;
import client.ServerMessage;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ImagePanel extends JPanel {
	private ImageIcon icon;
	private JLabel mode;
	private JLabel camera;
	private JLabel delay;
	private JPanel cameraPanel;
	private boolean movie;
	private boolean connected;
	private MessageBuffer msgBuffer;
	private CameraBorder idleBorder;
	private CameraBorder movieBorder;
	private CameraBorder noConnectionBorder;

	public ImagePanel(MessageBuffer msgBuffer) {
		this.msgBuffer = msgBuffer;
		idleBorder = new CameraBorder(Color.GREEN);
		movieBorder = new CameraBorder(Color.RED);
		noConnectionBorder = new CameraBorder(Color.BLACK);
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

		mode = new JLabel();
		mode.setFont(font);
		c.gridx = 0;
		c.gridy = 0;
		add(mode, c);

		icon = new ImageIcon();
		camera = new JLabel(icon);
		camera.setFont(font);
		cameraPanel = new JPanel();
		cameraPanel.add(camera);
		c.gridy = 1;
		camera.setPreferredSize(new Dimension(AxisM3006V.IMAGE_WIDTH, AxisM3006V.IMAGE_HEIGHT));
		cameraPanel.setBorder(idleBorder);
		cameraPanel.addMouseListener(new IdleListener());
		add(cameraPanel, c);

		delay = new JLabel();
		delay.setFont(font);
		c.gridy = 2;
		add(delay, c);

		setNoCameraConnected();
	}

	public void refresh(CameraImage data) {
		Image image = getToolkit().createImage(data.getImage());
		getToolkit().prepareImage(image, -1, -1, null);
		icon.setImage(image);
		icon.paintIcon(camera, camera.getGraphics(), 0, 0);
		delay.setText("Delay: " + (System.currentTimeMillis() - data.getTimestamp()) + "ms");
	}

	public void setMovieMode(boolean trigger) {
		if (connected) {
			movie = true;
			cameraPanel.setBorder(movieBorder);
			if (trigger) {
				mode.setText("Movie (TRIGGER)");
			} else {
				mode.setText("Movie");
			}
		}
	}

	public void setIdleMode() {
		if (connected) {
			movie = false;
			cameraPanel.setBorder(idleBorder);
			mode.setText("Idle");
		}
	}

	public void setNoCameraConnected() {
		camera.setIcon(null);
		camera.setText("No camera connected");
		cameraPanel.setBorder(noConnectionBorder);
		mode.setText(" ");
		delay.setText(" ");
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	private class CameraBorder extends LineBorder {
		private Insets insets = new Insets(-3, -3, -3, -3);

		public CameraBorder(Color color) {
			super(color, 2);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return insets;
		}

	}

	private class IdleListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (movie) {
				msgBuffer.addMessage(new ServerMessage(ServerMessage.IDLE_MESSAGE));
				setIdleMode();
			}
		}
	}

}
