package client.GUI;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Reuse in client GUI
 */

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
