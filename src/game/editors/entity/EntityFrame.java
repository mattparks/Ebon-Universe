package game.editors.entity;

import flounder.engine.*;
import flounder.helpers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EntityFrame {
	private JFrame jFrame;

	private JPanel contentPanel;

	public EntityFrame() {
		jFrame = new JFrame(FlounderEngine.getDevices().getDisplay().getTitle());
		jFrame.setSize(FlounderEngine.getDevices().getDisplay().getWidth(), FlounderEngine.getDevices().getDisplay().getHeight());
		jFrame.setResizable(true);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.setLayout(new GridLayout(1, 2));
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

		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		addLeftOptions();
		addComponentOptions();

		jFrame.add(contentPanel);
		jFrame.setLocationByPlatform(true);
		jFrame.setVisible(true);
	}

	private void addComponentOptions() {
		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent panel1 = makeTextPanel("Panel #1");
		tabbedPane.addTab("Tab 1", null, panel1, "");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeTextPanel("Panel #2");
		tabbedPane.addTab("Tab 2", null, panel2, "");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = makeTextPanel("Panel #3");
		tabbedPane.addTab("Tab 3", null, panel3, "");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JComponent panel4 = makeTextPanel( "Panel #4");
		tabbedPane.addTab("Tab 4", null, panel4, "");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		jFrame.add(tabbedPane);
	}

	private JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	private void addLeftOptions() {
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

		contentPanel.add(checkBoxPanel);
	}
}
