package client.GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import client.CameraProtocolConstants;
import client.ImageBuffer;
import client.MessageBuffer;

public class DebugFrame extends JFrame {
	private MessageBuffer leftMsgBuffer;
	private MessageBuffer rightMsgBuffer;
	private ImageBuffer imgBuffer;
	private ImagePanel cameraLeft;
	private ImagePanel cameraRight;
	private JRadioButton modeMovie;
	private JRadioButton modeIdle;
	private JRadioButton modeAuto;
	private JRadioButton synchSynch;
	private JRadioButton synchAsync;
	private JRadioButton synchAuto;
	private GUI gui;

	public DebugFrame(ImageBuffer imgBuffer, ImagePanel cameraLeft, ImagePanel cameraRight, MessageBuffer leftMsgBuffer,
			MessageBuffer rightMsgBuffer, GUI gui) {
		super("Debug Options");
		this.leftMsgBuffer = leftMsgBuffer;
		this.rightMsgBuffer = rightMsgBuffer;
		this.imgBuffer = imgBuffer;
		this.cameraLeft = cameraLeft;
		this.cameraRight = cameraRight;
		this.gui = gui;

		// Padding Panel
		JPanel paddingPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		paddingPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Mode Panel
		modeMovie = new JRadioButton("Movie");
		modeIdle = new JRadioButton("Idle");
		modeAuto = new JRadioButton("Auto");
		modeAuto.setSelected(true);
		modeMovie.setFocusPainted(false);
		panelSetup(paddingPanel, "Mode Options", new ModeSelectionListener(), modeMovie, modeIdle, modeAuto);

		// Synch Panel
		synchSynch = new JRadioButton("Synchronized");
		synchAsync = new JRadioButton("Asynchronized");
		synchAuto = new JRadioButton("Auto");
		synchAuto.setSelected(true);
		panelSetup(paddingPanel, "Synchronization Options", new SynchSelectionListener(), synchSynch, synchAsync,
				synchAuto);

		// DebugFrame setup
		add(paddingPanel);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}

	private void panelSetup(JPanel paddingPanel, String title, ItemListener listener, JRadioButton... btns) {
		ButtonGroup group = new ButtonGroup();
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
				TitledBorder.CENTER, TitledBorder.TOP));
		for (JRadioButton b : btns) {
			b.addItemListener(listener);
			group.add(b);
			panel.add(b);
		}
		paddingPanel.add(panel);
	}

	private class ModeSelectionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				byte msg;
				if (e.getSource() == modeMovie) {
					msg = CameraProtocolConstants.CLIENT_FORCE_MOVIE;
					cameraLeft.setMovieMode(false);
					cameraRight.setMovieMode(false);
				} else if (e.getSource() == modeIdle) {
					msg = CameraProtocolConstants.CLIENT_FORCE_IDLE;
					cameraLeft.setIdleMode();
					cameraRight.setIdleMode();
				} else {
					msg = CameraProtocolConstants.CLIENT_FORCE_NONE;
					// FIXME - display correct mode on return to auto
				}
				leftMsgBuffer.addMessage(msg);
				rightMsgBuffer.addMessage(msg);
				///// UGLY SOLUTION TO PROBLEM ABOVE //////////
				if (msg == CameraProtocolConstants.CLIENT_FORCE_NONE) {
					leftMsgBuffer.addMessage(CameraProtocolConstants.CLIENT_IDLE_MESSAGE);
					rightMsgBuffer.addMessage(CameraProtocolConstants.CLIENT_IDLE_MESSAGE);
					cameraLeft.setIdleMode();
					cameraRight.setIdleMode();
				}
				/////////////////////////////////////////////
			}
		}
	}

	private class SynchSelectionListener implements ItemListener {
		// FIXME Update GUI based on mode
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == synchSynch) {
					imgBuffer.forceSynch();
					gui.setSynchMode(true);
				} else if (e.getSource() == synchAsync) {
					imgBuffer.forceAsynch();
					gui.setSynchMode(false);
				} else {
					imgBuffer.forceNone();
					gui.setSynchMode(imgBuffer.isSynch());
				}
			}
		}
	}
}
