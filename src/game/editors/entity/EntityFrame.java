package game.editors.entity;

import flounder.engine.*;
import flounder.resources.*;
import game.entities.*;
import game.entities.components.*;
import game.entities.loading.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class EntityFrame {
	private class FrameComponent {
		private String classPath;
		private String tabName;

		public FrameComponent(String classPath, String tabName) {
			this.classPath = classPath;
			this.tabName = tabName;
		}
	}

	private FrameComponent[] components = new FrameComponent[]{
			new FrameComponent(ColliderComponent.class.getName(), "Collider"),
			new FrameComponent(CollisionComponent.class.getName(), "Collision"),
			new FrameComponent(FadeRemove.class.getName(), "Remove Fade"),
			new FrameComponent(ModelComponent.class.getName(), "Model"),
			new FrameComponent(ParticleSystemComponent.class.getName(), "Particle System"),
	};

	private JFrame jFrame;

	private JPanel contentPanel;

	private JTabbedPane componentsPane;

	private JComboBox componentDropdown;
	private JButton componentAdd;
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
	}

	public void create() {
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

		//for (IEntityComponent component : EntityGame.focusEntity.getComponents()) {
		//	JPanel panel = makeTextPanel(component.getTabName());
		//		component.addToPanel(panel);
		//	componentsPane.addTab(component.getTabName(), null, panel, "");
		//	}

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

		// Component Dropdown.
		componentDropdown = new JComboBox();
		for (int i = 0; i < components.length; i++) {
			componentDropdown.addItem(components[i].tabName);
		}
		checkBoxPanel.add(componentDropdown);

		// Component Add Button.
		componentAdd = new JButton("Add Component");
		componentAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String component = (String) componentDropdown.getSelectedItem();
				IEntityComponent entityComponent = null;

				for (int i = 0; i < components.length; i++) {
					if (components[i].tabName.equals(component)) {
						if (EntityGame.focusEntity != null && !EntityGame.focusEntity.hasComponent(components[i].classPath)) {
							try {
								FlounderEngine.getLogger().log("Adding component: " + component);
								Class componentClass = Class.forName(components[i].classPath);
								Class[] componentTypes = new Class[]{Entity.class};
								@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
								Object[] componentParameters = new Object[]{EntityGame.focusEntity};
								entityComponent = (IEntityComponent) componentConstructor.newInstance(componentParameters);
							} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
								FlounderEngine.getLogger().error("While loading component" + components[i].classPath + "'s constructor could not be found!");
								FlounderEngine.getLogger().exception(ex);
							}
						} else {
							FlounderEngine.getLogger().error("Entity already has instance of " + components[i].classPath);
						}
					}
				}

				if (component.equals("Model")) {
					EntityGame.PATH_MODEL = new MyFile(MyFile.RES_FOLDER, "entities", "spaceShip.obj");
					EntityGame.PATH_TEXTURE = new MyFile(MyFile.RES_FOLDER, "entities", "crate.png");
					EntityGame.PATH_NORMALMAP = new MyFile(MyFile.RES_FOLDER, "entities", "crateNormal.png");
				}

				if (entityComponent != null) {
					JPanel panel = makeTextPanel(component);
					entityComponent.addToPanel(panel);
					componentsPane.addTab(component, null, panel, "");
				}
			}
		});
		checkBoxPanel.add(componentAdd);

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
		polygonMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
					EntityGame.PATH_MODEL = null;
					EntityGame.PATH_TEXTURE = null;
					EntityGame.PATH_NORMALMAP = null;
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
