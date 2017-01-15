package editors.entities;

import ebon.entities.*;
import ebon.entities.editing.*;
import editors.editor.*;
import flounder.devices.*;
import flounder.framework.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.standard.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;

public class FrameEntities extends IStandard {
	private static JFrame frame;
	private static JMenuBar menuBar;
	private static JPanel componentPanel;
	private static JPanel mainPanel;
	private static JPanel renderPanel;

	public static JTabbedPane componentsPane;

	public static JComboBox componentDropdown;
	public static JButton componentAdd;
	public static JTextField nameField;
	public static JButton loadButton;
	public static JButton converterButton;
	public static JCheckBox polygonMode;
	public static JCheckBox rotateEntity;
	public static JButton resetButton;
	public static JButton saveButton;

	public static List<IEditorComponent> editorComponents;

	private static List<String> addedTabs = new ArrayList<>();

	public FrameEntities() {
		super(FlounderStandard.class, FlounderDisplayJPanel.class);
	}

	@Override
	public void init() {
		frame = new JFrame();
		frame.setTitle(FlounderDisplay.getTitle());
		frame.setSize(FlounderDisplay.getWidth(), FlounderDisplay.getHeight());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame,
						"Are you sure to close this editor?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					FlounderFramework.requestClose();
				} else {
					frame.setVisible(true);
				}
			}
		});

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		editorComponents = new ArrayList<>();

		menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem menuFileHelp = new JMenuItem("Help");
		menuFileHelp.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Use the bottom bar to select a existing entity, or to change views. The right panel is used to edit entity component, previewed in the top-left display.");
			}
		});
		menuFile.add(menuFileHelp);
		JMenuItem menuFileQuit = new JMenuItem("Quit");
		menuFileQuit.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FlounderFramework.requestClose();
			}
		});
		menuFile.add(menuFileQuit);
		menuBar.add(menuFile);
		frame.setJMenuBar(menuBar);

		componentPanel = new JPanel();
		componentsPane = new JTabbedPane();
		componentPanel.add(componentsPane);
		frame.add(componentPanel, BorderLayout.EAST);

		mainPanel = new JPanel();
		addComponentsDropdown();
		addComponentsButton();
		entityName();
		addEntityLoad();
		polygonMode();
		rotate();
		reset();
		save();
		frame.add(mainPanel, BorderLayout.SOUTH);

		renderPanel = FlounderDisplayJPanel.createPanel();
		frame.add(renderPanel, BorderLayout.CENTER);

		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();
	}

	private void addComponentsDropdown() {
		// Component Dropdown.
		componentDropdown = new JComboBox();

		for (int i = 0; i < IEditorComponent.EDITOR_COMPONENTS.length; i++) {
			componentDropdown.addItem(IEditorComponent.EDITOR_COMPONENTS[i].getTabName());
		}

		mainPanel.add(componentDropdown);
	}

	private void addComponentsButton() {
		componentAdd = new JButton("Add Component");
		componentAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String component = (String) componentDropdown.getSelectedItem();
				IEditorComponent editorComponent = null;

				for (int i = 0; i < IEditorComponent.EDITOR_COMPONENTS.length; i++) {
					if (IEditorComponent.EDITOR_COMPONENTS[i].getTabName().equals(component)) {
						if (((ExtensionEntities) FlounderEditor.getEditorType()).focusEntity != null && ((ExtensionEntities) FlounderEditor.getEditorType()).focusEntity.getComponent(IEditorComponent.EDITOR_COMPONENTS[i].getComponent().getId()) == null) {
							try {
								FlounderLogger.log("Adding component: " + component);
								Class componentClass = Class.forName(IEditorComponent.EDITOR_COMPONENTS[i].getClass().getName());
								Class[] componentTypes = new Class[]{Entity.class};
								@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
								Object[] componentParameters = new Object[]{((ExtensionEntities) FlounderEditor.getEditorType()).focusEntity};
								editorComponent = (IEditorComponent) componentConstructor.newInstance(componentParameters);
							} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
								FlounderLogger.error("While loading component" + IEditorComponent.EDITOR_COMPONENTS[i] + "'s constructor could not be found!");
								FlounderLogger.exception(ex);
							}
						} else {
							FlounderLogger.error("Entity already has instance of " + IEditorComponent.EDITOR_COMPONENTS[i]);
						}
					}
				}

				FlounderLogger.log(editorComponent);

				if (editorComponent != null) {
					editorComponents.add(editorComponent);

					JPanel panel = IEditorComponent.makeTextPanel();
					editorComponent.addToPanel(panel);
					componentAddRemove(panel, editorComponent);
					addSideTab(component, panel);
				}
			}
		});
		mainPanel.add(componentAdd);
	}

	private void entityName() {
		nameField = new JTextField("unnamed");
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
				((ExtensionEntities) FlounderEditor.getEditorType()).entityName = nameField.getText();
			}
		});
		mainPanel.add(nameField);
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

						FlounderLogger.error(fileName);

						clearSideTab();
						((ExtensionEntities) FlounderEditor.getEditorType()).loadFromEntity = fileName.replace(".entity", "");
					} else {
						FlounderLogger.error("The selected file path is not inside the res/entities folder!");
					}
				}
			}
		});
		mainPanel.add(loadButton);
	}

	private void polygonMode() {
		polygonMode = new JCheckBox("Polygon Mode");
		polygonMode.setSelected(false);
		polygonMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((ExtensionEntities) FlounderEditor.getEditorType()).polygonMode = !((ExtensionEntities) FlounderEditor.getEditorType()).polygonMode;
			}
		});
		mainPanel.add(polygonMode);
	}

	private void rotate() {
		rotateEntity = new JCheckBox("Rotate Entity");
		rotateEntity.setSelected(false);
		rotateEntity.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				((ExtensionEntities) FlounderEditor.getEditorType()).entityRotate = rotateEntity.isSelected();
			}
		});
		mainPanel.add(rotateEntity);
	}

	public static void addSideTab(String tabName, JPanel panel) {
		FlounderLogger.log("Adding side panel: " + tabName);
		addedTabs.add(tabName);
		componentsPane.addTab(tabName, null, panel, "");
	}

	public static void removeSideTab(String tabName) {
		FlounderLogger.log("Removing side panel: " + tabName);
		addedTabs.remove(tabName);
		List<Integer> ids = new ArrayList<>();

		for (int i = 0; i < componentsPane.getTabCount(); i++) {
			if (componentsPane.getTitleAt(i).contains(tabName)) {
				ids.add(i);
			}
		}

		Collections.reverse(ids);
		ids.forEach(componentsPane::remove);
	}

	public static void clearSideTab() {
		new ArrayList<>(addedTabs).forEach(FrameEntities::removeSideTab);
		componentsPane.removeAll();
		// TODO: Fix clearing not working.
	}

	private void reset() {
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to reset entity settings?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					nameField.setText("unnamed");
					((ExtensionEntities) FlounderEditor.getEditorType()).loadDefaultEntity();
					clearSideTab();
				}
			}
		});
		mainPanel.add(resetButton);
	}

	public static void componentAddRemove(JPanel panel, IEditorComponent editorComponent) {
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to remove this component.", "Any unsaved component data will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					editorComponents.remove(editorComponent);
					removeSideTab(editorComponent.getTabName());
					((ExtensionEntities) FlounderEditor.getEditorType()).focusEntity.removeComponent(editorComponent.getComponent());
				}
			}
		});
		panel.add(removeButton);
	}

	@Override
	public void update() {
		editorComponents.forEach(IEditorComponent::update);

		for (Pair<String, JPanel> p : IEditorComponent.ADD_SIDE_TAB) {
			addSideTab(p.getFirst(), p.getSecond());
		}

		for (String s : IEditorComponent.REMOVE_SIDE_TAB) {
			removeSideTab(s);
		}

		IEditorComponent.ADD_SIDE_TAB.clear();
		IEditorComponent.REMOVE_SIDE_TAB.clear();
	}

	@Override
	public void profile() {
	}

	private void save() {
		saveButton = new JButton("Save Entity");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((ExtensionEntities) FlounderEditor.getEditorType()).focusEntity != null) {
					EbonEntities.save(((ExtensionEntities) FlounderEditor.getEditorType()).focusEntity, editorComponents, ((ExtensionEntities) FlounderEditor.getEditorType()).entityName);
				}
			}
		});
		mainPanel.add(saveButton);
	}

	@Override
	public void dispose() {
		frame.setVisible(false);
		frame.dispose();
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
