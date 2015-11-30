package client.GUI;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConnectionDialog {
	private JTextField portField = new JTextField();
	private JComboBox<String> hostList;
	private Object[] inputs;

	public ConnectionDialog() {
		String[] hosts = new String[11];
		hosts[0] = "localhost";
		for (int i = 1; i < hosts.length; i++) {
			hosts[i] = "argus-" + i + ".student.lth.se";
		}
		hostList = new JComboBox<String>(hosts);
		inputs = new Object[] { new JLabel("Host"), hostList, new JLabel("Port"), portField };
	}

	public int showDialog() {
		return JOptionPane.showConfirmDialog(null, inputs, "Connect", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
	}

	public String getPort() {
		return portField.getText();
	}

	public String getHost() {
		return hostList.getSelectedItem().toString();

	}

	public void reset() {
		portField.setText("");
		hostList.setSelectedIndex(0);
	}

	public boolean isValidPort() {
		return portField.getText().matches("[0-9]+");
	}

}