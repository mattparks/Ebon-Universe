package editor.entities;

import ebon.entities.*;
import editor.entities.components.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.logger.*;
import flounder.physics.bounding.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;

public class EntitiesFrame {
	public final IEditorComponent[] components = new IEditorComponent[]{
			new EditorCollider((Entity) null),
			new EditorCollision((Entity) null),
			new EditorRemoveFade((Entity) null),
			new EditorModel((Entity) null),
			new EditorParticleSystem((Entity) null),
	};

	private JFrame jFrame;

	public JPanel contentPanel;
	public JPanel checkBoxPanel;

	public JTabbedPane componentsPane;

	public JComboBox componentDropdown;
	public JButton componentAdd;
	public JTextField nameField;
	public JButton loadButton;
	public JButton converterButton;
	public JCheckBox polygonMode;
	public JCheckBox drawAABBs;
	public JCheckBox rotateEntity;
	public JButton resetButton;
	public JButton saveButton;

	public List<IEditorComponent> editorComponents;

	public EntitiesFrame() {
		jFrame = new JFrame(FlounderDisplay.getTitle());
		jFrame.setSize(FlounderDisplay.getWidth(), FlounderDisplay.getHeight());
		jFrame.setResizable(true);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.setLayout(new GridLayout(2, 1));
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
		editorComponents = new ArrayList<>();
	}

	public void create() {
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		checkBoxPanel = new JPanel();

		addSidePane();
		addComponentsDropdown();
		addComponentsButton();
		entityName();
		addEntityLoad();
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

	public JPanel makeTextPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		return panel;
	}

	public void addSideTab(String tabName, JPanel panel) {
		componentsPane.addTab(tabName, null, panel, "");
	}

	public void removeSideTab(String tabName) {
		List<Integer> ids = new ArrayList<>();

		for (int i = 0; i < componentsPane.getTabCount(); i++) {
			if (componentsPane.getTitleAt(i).contains(tabName)) {
				ids.add(i);
			}
		}

		Collections.reverse(ids);
		ids.forEach(componentsPane::remove);
	}

	public void clearSideTab() {
		componentsPane.removeAll();
	}

	public void addSidePane() {
		componentsPane = new JTabbedPane();
		jFrame.add(componentsPane);
	}

	private void addComponentsDropdown() {
		// Component Dropdown.
		componentDropdown = new JComboBox();

		for (int i = 0; i < components.length; i++) {
			componentDropdown.addItem(components[i].getTabName());
		}

		checkBoxPanel.add(componentDropdown);
	}

	private void addComponentsButton() {
		componentAdd = new JButton("Add Component");
		componentAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String component = (String) componentDropdown.getSelectedItem();
				IEditorComponent editorComponent = null;

				for (int i = 0; i < components.length; i++) {
					if (components[i].getTabName().equals(component)) {
						if (EditorEntities.instance.focusEntity != null && EditorEntities.instance.focusEntity.getComponent(components[i].getComponent().getId()) == null) {
							try {
								FlounderLogger.log("Adding component: " + component);
								Class componentClass = Class.forName(components[i].getClass().getName());
								Class[] componentTypes = new Class[]{Entity.class};
								@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
								Object[] componentParameters = new Object[]{EditorEntities.instance.focusEntity};
								editorComponent = (IEditorComponent) componentConstructor.newInstance(componentParameters);
							} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
								FlounderLogger.error("While loading component" + components[i] + "'s constructor could not be found!");
								FlounderLogger.exception(ex);
							}
						} else {
							FlounderLogger.error("Entity already has instance of " + components[i]);
						}
					}
				}

				//	if (component.equals("Model")) {
				//		EditorEntities.instance.PATH_MODEL = new MyFile(MyFile.RES_FOLDER, "entities", "spaceShip.obj");
				//		EditorEntities.instance.PATH_TEXTURE = new MyFile(MyFile.RES_FOLDER, "entities", "crate.png");
				//		EditorEntities.instance.PATH_NORMALMAP = new MyFile(MyFile.RES_FOLDER, "entities", "crateNormal.png");
				//	}

				if (editorComponent != null) {
					editorComponents.add(editorComponent);

					JPanel panel = makeTextPanel();
					editorComponent.addToPanel(panel);
					componentAddRemove(panel, editorComponent);
					addSideTab(component, panel);
				}
			}
		});
		checkBoxPanel.add(componentAdd);
	}

	public void componentAddRemove(JPanel panel, IEditorComponent editorComponent) {
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(jFrame,
						"Are you sure you want to remove this component.", "Any unsaved component data will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					editorComponents.remove(editorComponent);
					removeSideTab(editorComponent.getTabName());
					EditorEntities.instance.focusEntity.removeComponent(editorComponent.getComponent());
				}
			}
		});
		panel.add(removeButton);
	}

	private void entityName() {
		nameField = new JTextField(EditorEntities.instance.ENTITY_NAME);
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
				EditorEntities.instance.ENTITY_NAME = nameField.getText();
			}
		});
		checkBoxPanel.add(nameField);
	}

	private void addEntityLoad() {
		loadButton = new JButton("Select .entity");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

					if (selectedFile.contains("res/entities")) {
						String[] filepath = selectedFile.split("/");
						String fileName = filepath[filepath.length - 1];

						clearSideTab();
						EditorEntities.instance.LOAD_FROM_ENTITY = fileName.replace(".entity", "");
					} else {
						FlounderLogger.error("The selected file path is not inside the res/entities folder!");
					}
				}
			}
		});
		checkBoxPanel.add(loadButton);
	}

	private void polygonMode() {
		polygonMode = new JCheckBox("Polygon Mode");
		polygonMode.setSelected(EditorEntities.instance.POLYGON_MODE);
		polygonMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditorEntities.instance.POLYGON_MODE = !EditorEntities.instance.POLYGON_MODE;
			}
		});
		checkBoxPanel.add(polygonMode);
	}

	private void drawAABBs() {
		drawAABBs = new JCheckBox("Draw AABBs");
		drawAABBs.setSelected(FlounderBounding.renders());
		drawAABBs.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				FlounderBounding.setRenders(drawAABBs.isSelected());
			}
		});
		checkBoxPanel.add(drawAABBs);
	}

	private void rotate() {
		rotateEntity = new JCheckBox("Rotate Entity");
		rotateEntity.setSelected(EditorEntities.instance.ENTITY_ROTATE);
		rotateEntity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				EditorEntities.instance.ENTITY_ROTATE = rotateEntity.isSelected();
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
					EditorEntities.instance.PATH_MODEL = null;
					EditorEntities.instance.PATH_TEXTURE = null;
					EditorEntities.instance.PATH_NORMALMAP = null;
					EditorEntities.instance.generateBlankEntity();
					clearSideTab();
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
				if (EditorEntities.instance.focusEntity != null) {
					EntitiesSaver.save(EditorEntities.instance.focusEntity, editorComponents, EditorEntities.instance.ENTITY_NAME);
				}
			}
		});
		checkBoxPanel.add(saveButton);
	}
}
