package game.editors.particles;

import flounder.engine.*;
import flounder.helpers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ParticleFrame {
	private JFrame jFrame;
	private JPanel mainPanel;

	public ParticleFrame() {
		jFrame = new JFrame(FlounderEngine.getDevices().getDisplay().getTitle());
		jFrame.setSize(FlounderEngine.getDevices().getDisplay().getWidth(), FlounderEngine.getDevices().getDisplay().getHeight());
		jFrame.setResizable(true);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(jFrame,
						"Are you sure to close this editor?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(-1);
				} else {
					jFrame.setVisible(true);
				}
			}
		});

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		addViewOptions();

		jFrame.add(mainPanel);
		jFrame.pack();
		jFrame.setLocationByPlatform(true);
		jFrame.setVisible(true);
	}

	private void addViewOptions() {
		JPanel checkBoxPanel = new JPanel();

		mainPanel.add(checkBoxPanel);
	}
}
