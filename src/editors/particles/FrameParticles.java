package editors.particles;

import ebon.particles.*;
import ebon.particles.loading.*;
import editors.editor.*;
import editors.particles.*;
import flounder.devices.*;
import flounder.events.*;
import flounder.framework.*;
import flounder.logger.*;
import flounder.resources.*;
import flounder.standard.*;
import flounder.textures.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FrameParticles extends IStandard {
	private static JFrame frame;
	private static JMenuBar menuBar;
	private static JPanel mainPanel;
	private static JPanel renderPanel;

	public static JTextField nameField;
	public static JButton loadButton;
	public static JSlider rowSlider;
	public static JSlider scaleSlider;
	public static JSlider lifeSlider;
	public static JButton resetButton;
	public static JButton saveButton;

	public FrameParticles() {
		super(FlounderDisplayJPanel.class);
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

		menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem menuFileHelp = new JMenuItem("Help");
		menuFileHelp.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Use the bottom bar to select a existing particle, or to change views, and values. Changes are previewed in the top display.");
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

		mainPanel = new JPanel();
		addNameField();
		addTextureLoad();
		addTextureRowBox();
		addScaleSlider();
		addLifeSlider();
		reset();
		save();
		frame.add(mainPanel, BorderLayout.SOUTH);

		renderPanel = FlounderDisplayJPanel.createPanel();
		frame.add(renderPanel, BorderLayout.CENTER);

		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();
	}

	@Override
	public void update() {
	}

	@Override
	public void profile() {
	}

	private void addNameField() {
		nameField = new JTextField("testing");
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
				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null) {
					((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.setName(nameField.getText());
				}
			}
		});

		mainPanel.add(nameField);
	}

	private void addTextureLoad() {
		loadButton = new JButton("Select .png/.particle");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

					if (selectedFile.contains("res/particles")) {
						String[] filepath = selectedFile.split("/");
						String fileName = filepath[filepath.length - 1];

						if (fileName.contains(".png")) {
							Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "particles", fileName)).create();

							if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null) {
								((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.setTexture(texture);
							}
						} else {
							((ExtensionParticles) FlounderEditor.getEditorType()).loadFromParticle = fileName.replace(".particle", "");
						}
					} else {
						FlounderLogger.error("The selected file path is not inside the res/particles folder!");
					}
				}
			}
		});
		mainPanel.add(loadButton);
	}

	private void addTextureRowBox() {
		rowSlider = new JSlider(JSlider.HORIZONTAL, 1, 17, 1);
		rowSlider.setToolTipText("Texture Rows");
		rowSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null && ((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.getTexture() != null) {
					((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.getTexture().setNumberOfRows(reading);
				}
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private Texture currentTexture = null;

			@Override
			public boolean eventTriggered() {
				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate == null) {
					return false;
				}

				Texture newTexture = ((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.getTexture();
				boolean changed = currentTexture != newTexture;
				currentTexture = newTexture;
				return changed;
			}

			@Override
			public void onEvent() {
				int reading = rowSlider.getValue();

				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null && ((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.getTexture() != null) {
					((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.getTexture().setNumberOfRows(reading);
				}
			}
		});

		//Turn on labels at major tick marks.
		rowSlider.setMajorTickSpacing(4);
		rowSlider.setMinorTickSpacing(1);
		rowSlider.setPaintTicks(true);
		rowSlider.setPaintLabels(true);
		mainPanel.add(rowSlider);
	}

	private void addScaleSlider() {
		scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
		scaleSlider.setToolTipText("Particle Size Scale");
		scaleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null) {
					((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.setScale(reading / 100.0f);
				}
			}
		});

		//Turn on labels at major tick marks.
		scaleSlider.setMajorTickSpacing(30);
		scaleSlider.setMinorTickSpacing(15);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		mainPanel.add(scaleSlider);
	}

	private void addLifeSlider() {
		lifeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		lifeSlider.setToolTipText("Particle Life Length");
		lifeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null) {
					((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.setLifeLength(reading / 10.0f);
				}
			}
		});

		//Turn on labels at major tick marks.
		lifeSlider.setMajorTickSpacing(10);
		lifeSlider.setMinorTickSpacing(5);
		lifeSlider.setPaintTicks(true);
		lifeSlider.setPaintLabels(true);
		mainPanel.add(lifeSlider);
	}

	private void reset() {
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to reset particle settings?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					((ExtensionParticles) FlounderEditor.getEditorType()).particleSystem.removeParticleType(((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate);
					((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate = new ParticleTemplate("testing", null, 1.0f, 1.0f);
					((ExtensionParticles) FlounderEditor.getEditorType()).particleSystem.addParticleType(((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate);
					EbonParticles.clear();
					nameField.setText(((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate.getName());
					rowSlider.setValue(0);
					scaleSlider.setValue(100);
					lifeSlider.setValue(10);
				}
			}
		});

		mainPanel.add(resetButton);
	}

	private void save() {
		saveButton = new JButton("Save Particle");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate != null) {
					EbonParticles.save(((ExtensionParticles) FlounderEditor.getEditorType()).particleTemplate);
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
