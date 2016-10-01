package editor.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.events.*;
import flounder.logger.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.resources.*;
import flounder.textures.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ParticlesFrame {
	private JFrame jFrame;
	private JPanel mainPanel;

	public JTextField nameField;
	public JButton loadButton;
	public JSlider rowSlider;
	public JSlider scaleSlider;
	public JSlider lifeSlider;
	public JButton resetButton;
	public JButton saveButton;

	public ParticlesFrame() {
		jFrame = new JFrame(FlounderDisplay.getTitle());
		jFrame.setSize(FlounderDisplay.getWidth(), FlounderDisplay.getHeight());
		jFrame.setResizable(true);
		jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
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

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());

		addNameField();
		addTextureLoad();
		addTextureRowSlider();
		addScaleSlider();
		addLifeSlider();
		reset();
		save();

		jFrame.add(mainPanel);
		jFrame.setLocationByPlatform(true);
		jFrame.setVisible(true);

		jFrame.repaint();
	}

	private void addNameField() {
		nameField = new JTextField(EditorParticles.instance.particleTemplate.getName());
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
				if (EditorParticles.instance.instance.particleTemplate != null) {
					EditorParticles.instance.particleTemplate.setName(nameField.getText());
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
							Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "particles", fileName)).createInSecondThread();

							if (EditorParticles.instance.particleTemplate != null) {
								EditorParticles.instance.particleTemplate.setTexture(texture);
							}
						} else {
							EditorParticles.instance.loadFromParticle = fileName.replace(".particle", "");
						}
					} else {
						FlounderLogger.error("The selected file path is not inside the res/particles folder!");
					}
				}
			}
		});
		mainPanel.add(loadButton);
	}

	private void addTextureRowSlider() {
		rowSlider = new JSlider(JSlider.HORIZONTAL, 1, 17, 1);
		rowSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (EditorParticles.instance.particleTemplate != null && EditorParticles.instance.particleTemplate.getTexture() != null) {
					EditorParticles.instance.particleTemplate.getTexture().setNumberOfRows(reading);
				}
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private Texture currentTexture = EditorParticles.instance.particleTemplate != null ? null : EditorParticles.instance.particleTemplate.getTexture();

			@Override
			public boolean eventTriggered() {
				if (EditorParticles.instance.particleTemplate == null) {
					return false;
				}

				Texture newTexture = EditorParticles.instance.particleTemplate.getTexture();
				boolean changed = currentTexture != newTexture;
				currentTexture = newTexture;
				return changed;
			}

			@Override
			public void onEvent() {
				int reading = rowSlider.getValue();

				if (EditorParticles.instance.particleTemplate != null && EditorParticles.instance.particleTemplate.getTexture() != null) {
					EditorParticles.instance.particleTemplate.getTexture().setNumberOfRows(reading);
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
		scaleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (EditorParticles.instance.particleTemplate != null) {
					EditorParticles.instance.particleTemplate.setScale(reading / 100.0f);
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
		lifeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (EditorParticles.instance.particleTemplate != null) {
					EditorParticles.instance.particleTemplate.setLifeLength(reading / 10.0f);
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
				if (JOptionPane.showConfirmDialog(jFrame,
						"Are you sure you want to reset particle settings?", "Any unsaved work will be lost!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					EditorParticles.instance.particleSystem.removeParticleType(EditorParticles.instance.particleTemplate);
					EditorParticles.instance.particleTemplate = new ParticleTemplate("testing", null, 1.0f, 1.0f);
					EditorParticles.instance.particleSystem.addParticleType(EditorParticles.instance.particleTemplate);
					FlounderParticles.clear();
					nameField.setText(EditorParticles.instance.particleTemplate.getName());
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
				if (EditorParticles.instance.particleTemplate != null) {
					ParticlesSaver.save(EditorParticles.instance.particleTemplate);
				}
			}
		});

		mainPanel.add(saveButton);
	}
}
