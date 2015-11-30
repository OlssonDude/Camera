package client.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import client.ConnectionMonitor;

public class ConnectionHandling extends JPanel {
	private JButton connect;
	private JButton disconnect;
	private ImagePanel cameraDisplay;
	private ConnectionMonitor server;
	private ConnectionDialog connectionDialog;

	public ConnectionHandling(ConnectionMonitor server, ImagePanel cameraDisplay) {
		this.server = server;
		this.cameraDisplay = cameraDisplay;
		connectionDialog = new ConnectionDialog();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		connect = new JButton("Connect");
		disconnect = new JButton("Disconnect");
		styleButton(connect, new Color(90, 230, 90));
		styleButton(disconnect, new Color(230, 90, 90));
		connect.setPreferredSize(disconnect.getPreferredSize());
		disconnect.addActionListener(new DisconnectListener());
		connect.addActionListener(new ConnctListener());

		add(Box.createHorizontalGlue());
		add(connect);
		add(Box.createHorizontalStrut(10));
		add(disconnect);
		add(Box.createHorizontalGlue());
	}

	private void styleButton(JButton b, Color c) {
		b.setFont(b.getFont().deriveFont(Font.BOLD, 20));
		b.setForeground(Color.WHITE);
		b.setBackground(c);
		b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		b.setFocusPainted(false);
	}

	private class ConnctListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = connectionDialog.showDialog();
			if (choice == JOptionPane.OK_OPTION) {
				if (connectionDialog.isValidPort()) {
					String port = connectionDialog.getPort();
					String host = connectionDialog.getHost();
					boolean connected = server.connect(host, Integer.parseInt(port));
					if (connected) {
						cameraDisplay.setConnected(connected);
						cameraDisplay.setIdleMode();
						connect.setEnabled(false);
					} else {
						JOptionPane.showMessageDialog(null, "Could not connect to " + host + " at port " + port,
								"Unable to connect", JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Port must be a non negative number");
				}
			}
			connectionDialog.reset();
		}
	}

	private class DisconnectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			server.disconnect();
			cameraDisplay.setConnected(false);
			cameraDisplay.setNoCameraConnected();
			connect.setEnabled(true);
		}

	}

}
