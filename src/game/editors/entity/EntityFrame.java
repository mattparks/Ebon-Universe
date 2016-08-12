package game.editors.entity;

import flounder.engine.*;
import flounder.helpers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EntityFrame {
	private JFrame jFrame;
	private JPanel mainPanel;

	public EntityFrame() {
		jFrame = new JFrame(FlounderEngine.getDevices().getDisplay().getTitle());
		jFrame.setSize(FlounderEngine.getDevices().getDisplay().getWidth(), FlounderEngine.getDevices().getDisplay().getHeight());
		jFrame.setResizable(true);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
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

		JCheckBox polygonMode = new JCheckBox("Polygon Mode");
		polygonMode.setSelected(OpenGlUtils.isInWireframe());
		polygonMode.addItemListener((ItemEvent e) -> OpenGlUtils.goWireframe(polygonMode.isSelected()));
		checkBoxPanel.add(polygonMode);

		JCheckBox drawAABBs = new JCheckBox("Draw AABBs");
		polygonMode.setSelected(FlounderEngine.getAABBs().renders());
		drawAABBs.addItemListener((ItemEvent e) -> FlounderEngine.getAABBs().setRenders(drawAABBs.isSelected()));
		checkBoxPanel.add(drawAABBs);

		JCheckBox rotateEntity = new JCheckBox("RotateEntity");
		rotateEntity.setSelected(EntityGame.ENTITY_ROTATE);
		rotateEntity.addItemListener((ItemEvent e) -> EntityGame.ENTITY_ROTATE = rotateEntity.isSelected());
		checkBoxPanel.add(rotateEntity);

		mainPanel.add(checkBoxPanel);
	}
}
