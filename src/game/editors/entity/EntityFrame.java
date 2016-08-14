package game.editors.entity;

import flounder.engine.*;
import game.editors.particles.*;
import game.entities.*;
import game.entities.loading.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class EntityFrame {
	private JFrame jFrame;

	private JPanel contentPanel;

	private JTabbedPane componentsPane;

	private JTextField nameField;
	private JCheckBox polygonMode;
	private JCheckBox drawAABBs;
	private JCheckBox rotateEntity;
	private JButton resetButton;
	private JButton saveButton;

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
					FlounderEngine.requestClose();
				} else {
					jFrame.setVisible(true);
				}
			}
		});

		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		addComponentOptions();
		addLeftOptions();

		jFrame.add(contentPanel);
		jFrame.setLocationByPlatform(true);
		jFrame.setVisible(true);
	}

	private void addComponentOptions() {
		if (componentsPane != null) {
			for (int i = 0; i < componentsPane.getTabCount(); i++) {
				componentsPane.remove(i);
			}

			jFrame.remove(componentsPane);
		}

		componentsPane = new JTabbedPane();

		for (IEntityComponent component : EntityGame.focusEntity.getComponents()) {
			JPanel panel = makeTextPanel(component.getTabName());
			component.addToPanel(panel);
			componentsPane.addTab(component.getTabName(), null, panel, "");
		}

		jFrame.add(componentsPane);
	}

	public static JPanel makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		return panel;
	}

	private void addLeftOptions() {
		// The area to put left options.
		JPanel checkBoxPanel = new JPanel();

		// Entity Name.
		nameField = new JTextField(EntityGame.ENTITY_NAME);
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				textUpdate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				textUpdate();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textUpdate();
			}

			private void textUpdate() {
				EntityGame.ENTITY_NAME = nameField.getText();
			}
		});
		checkBoxPanel.add(nameField);

		// Polygon Mode.
		polygonMode = new JCheckBox("Polygon Mode");
		polygonMode.setSelected(EntityGame.POLYGON_MODE);
		polygonMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				EntityGame.POLYGON_MODE = !EntityGame.POLYGON_MODE;
			}
		});
		checkBoxPanel.add(polygonMode);

		// Draw AABBs.
		drawAABBs = new JCheckBox("Draw AABBs");
		drawAABBs.setSelected(FlounderEngine.getAABBs().renders());
		drawAABBs.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				FlounderEngine.getAABBs().setRenders(drawAABBs.isSelected());
			}
		});
		checkBoxPanel.add(drawAABBs);

		// Rotate Entity,
		rotateEntity = new JCheckBox("Rotate Entity");
		rotateEntity.setSelected(EntityGame.ENTITY_ROTATE);
		rotateEntity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				EntityGame.ENTITY_ROTATE = rotateEntity.isSelected();
			}
		});
		checkBoxPanel.add(rotateEntity);

		// Reset Button.
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(jFrame,
						"Are you sure you want to reset entity settings?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					nameField.setText("unnamed");
					EntityGame.generateBlankEntity();
					addComponentOptions();
				}
			}
		});
		checkBoxPanel.add(resetButton);

		// Save Button.
		saveButton = new JButton("Save Entity");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (EntityGame.focusEntity != null) {
					EntitySaver.save(EntityGame.focusEntity, EntityGame.ENTITY_NAME);
				}
			}
		});
		checkBoxPanel.add(saveButton);

		// Adds the checkbox panel to the content panel.
		contentPanel.add(checkBoxPanel);
	}
}
