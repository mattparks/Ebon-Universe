package game.editors.entity;

import flounder.engine.*;
import flounder.helpers.*;
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
	private String[] components = new String[]{
			ColliderComponent.class.getName(),
			CollisionComponent.class.getName(),
			FadeRemove.class.getName(),
			ModelComponent.class.getName(),
			ParticleSystemComponent.class.getName(),
	};

	private static JFrame jFrame;

	private static JPanel contentPanel;
	private static JPanel checkBoxPanel;

	private static JTabbedPane componentsPane;

	private static JComboBox componentDropdown;
	private static JButton componentAdd;
	private static JTextField nameField;
	private static JCheckBox polygonMode;
	private static JCheckBox drawAABBs;
	private static JCheckBox rotateEntity;
	private static JButton resetButton;
	private static JButton saveButton;

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
		checkBoxPanel = new JPanel();

		addSidePane();
		addComponentsDropdown();
		addComponentsButton();
		entityName();
		polygonMode();
		drawAABBs();
		rotate();
		reset();
		save();

		contentPanel.add(checkBoxPanel);
		jFrame.add(contentPanel);
		jFrame.setLocationByPlatform(true);
		jFrame.setVisible(true);
	}

	public static JPanel makeTextPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		return panel;
	}

	public static void addSideTab(String tabName, JPanel panel) {
		componentsPane.addTab(tabName, null, panel, "");
	}

	public static void removeSideTab(String tabName) {

	}

	private void addSidePane() {
		if (componentsPane != null) {
			for (int i = 0; i < componentsPane.getTabCount(); i++) {
				componentsPane.remove(i);
			}
		} else {
			componentsPane = new JTabbedPane();
			jFrame.add(componentsPane);
		}
	}

	private void addComponentsDropdown() {
		// Component Dropdown.
		componentDropdown = new JComboBox();

		for (int i = 0; i < components.length; i++) {
			componentDropdown.addItem(components[i].split("\\.")[ByteWork.getCharCount(components[i], '.')].replace("Component", ""));
		}

		checkBoxPanel.add(componentDropdown);
	}

	private void addComponentsButton() {
		componentAdd = new JButton("Add Component");
		componentAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String component = (String) componentDropdown.getSelectedItem();
				IEntityComponent entityComponent = null;

				for (int i = 0; i < components.length; i++) {
					if (components[i].split("\\.")[ByteWork.getCharCount(components[i], '.')].replace("Component", "").equals(component)) {
						if (EntityGame.focusEntity != null && !EntityGame.focusEntity.hasComponent(components[i])) {
							try {
								FlounderEngine.getLogger().log("Adding component: " + component);
								Class componentClass = Class.forName(components[i]);
								Class[] componentTypes = new Class[]{Entity.class};
								@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
								Object[] componentParameters = new Object[]{EntityGame.focusEntity};
								entityComponent = (IEntityComponent) componentConstructor.newInstance(componentParameters);
							} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
								FlounderEngine.getLogger().error("While loading component" + components[i] + "'s constructor could not be found!");
								FlounderEngine.getLogger().exception(ex);
							}
						} else {
							FlounderEngine.getLogger().error("Entity already has instance of " + components[i]);
						}
					}
				}

				if (component.equals("Model")) {
					EntityGame.PATH_MODEL = new MyFile(MyFile.RES_FOLDER, "entities", "spaceShip.obj");
					EntityGame.PATH_TEXTURE = new MyFile(MyFile.RES_FOLDER, "entities", "crate.png");
					EntityGame.PATH_NORMALMAP = new MyFile(MyFile.RES_FOLDER, "entities", "crateNormal.png");
				}

				if (entityComponent != null) {
					JPanel panel = makeTextPanel();
					entityComponent.addToPanel(panel);
					componentAddRemove(component, panel, entityComponent);
					addSideTab(component, panel);
				}
			}
		});
		checkBoxPanel.add(componentAdd);
	}

	private void componentAddRemove(String tabName, JPanel panel, IEntityComponent component) {
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(jFrame,
						"Are you sure you want to remove this component.", "Any unsaved component data will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					removeSideTab(tabName);
					EntityGame.focusEntity.removeComponent(component);
				}
			}
		});
		panel.add(removeButton);
	}

	private void entityName() {
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
	}

	private void polygonMode() {
		polygonMode = new JCheckBox("Polygon Mode");
		polygonMode.setSelected(EntityGame.POLYGON_MODE);
		polygonMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EntityGame.POLYGON_MODE = !EntityGame.POLYGON_MODE;
			}
		});
		checkBoxPanel.add(polygonMode);
	}

	private void drawAABBs() {
		drawAABBs = new JCheckBox("Draw AABBs");
		drawAABBs.setSelected(FlounderEngine.getAABBs().renders());
		drawAABBs.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				FlounderEngine.getAABBs().setRenders(drawAABBs.isSelected());
			}
		});
		checkBoxPanel.add(drawAABBs);
	}

	private void rotate() {
		rotateEntity = new JCheckBox("Rotate Entity");
		rotateEntity.setSelected(EntityGame.ENTITY_ROTATE);
		rotateEntity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				EntityGame.ENTITY_ROTATE = rotateEntity.isSelected();
			}
		});
		checkBoxPanel.add(rotateEntity);
	}

	private void reset() {
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
					addSidePane();
				}
			}
		});
		checkBoxPanel.add(resetButton);
	}

	private void save() {
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
	}
}
