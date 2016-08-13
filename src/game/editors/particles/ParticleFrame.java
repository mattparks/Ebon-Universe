package game.editors.particles;

import flounder.engine.*;
import flounder.events.*;
import flounder.particles.loading.*;
import flounder.resources.*;
import flounder.textures.*;

import javax.swing.*;
import javax.swing.event.*;
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
		addGravitySlider();
		saveParticle();

		jFrame.add(mainPanel);
		jFrame.setLocationByPlatform(true);
		jFrame.setVisible(true);
	}

	private void addNameField() {
		JTextField nameField = new JTextField(ParticleGame.particleTemplate.getName());
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
				if (ParticleGame.particleTemplate != null) {
					ParticleGame.particleTemplate.setName(nameField.getText());
				}
			}
		});

		mainPanel.add(nameField);
	}

	private void addTextureLoad() {
		JButton button = new JButton("Select File");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

					if (selectedFile.contains("res/particles")) {
						String[] filepath = selectedFile.split("/");
						Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "particles", filepath[filepath.length - 1])).createInSecondThread();

						if (ParticleGame.particleTemplate != null) {
							ParticleGame.particleTemplate.setTexture(texture);
						}
					} else {
						FlounderEngine.getLogger().error("The selected texture path is not inside the res folder!");
					}
				}
			}
		});
		mainPanel.add(button);
	}

	private void addTextureRowSlider() {
		JSlider rowSlider = new JSlider(JSlider.HORIZONTAL, 1, 17, 1);
		rowSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();

				if (!source.getValueIsAdjusting()) {
					int reading = source.getValue();

					if (ParticleGame.particleTemplate != null && ParticleGame.particleTemplate.getTexture() != null) {
						ParticleGame.particleTemplate.getTexture().setNumberOfRows(reading);
					}
				}
			}
		});

		FlounderEngine.getEvents().addEvent(new IEvent() {
			private Texture currentTexture = ParticleGame.particleTemplate != null ? null : ParticleGame.particleTemplate.getTexture();

			@Override
			public boolean eventTriggered() {
				if (ParticleGame.particleTemplate == null) {
					return false;
				}

				Texture newTexture = ParticleGame.particleTemplate.getTexture();
				boolean changed = currentTexture != newTexture;
				currentTexture = newTexture;
				return changed;
			}

			@Override
			public void onEvent() {
				int reading = rowSlider.getValue();

				if (ParticleGame.particleTemplate != null && ParticleGame.particleTemplate.getTexture() != null) {
					ParticleGame.particleTemplate.getTexture().setNumberOfRows(reading);
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
		JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
		scaleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();

				if (!source.getValueIsAdjusting()) {
					int reading = source.getValue();

					if (ParticleGame.particleTemplate != null) {
						ParticleGame.particleTemplate.setScale(reading / 100.0f);
					}
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
		JSlider lifeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		lifeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();

				if (!source.getValueIsAdjusting()) {
					int reading = source.getValue();

					if (ParticleGame.particleTemplate != null) {
						ParticleGame.particleTemplate.setLifeLength(reading / 10.0f);
					}
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

	private void addGravitySlider() {
		JSlider gravitySlider = new JSlider(JSlider.HORIZONTAL, -10, 10, 0);
		gravitySlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();

				if (!source.getValueIsAdjusting()) {
					int reading = source.getValue();

					if (ParticleGame.particleTemplate != null) {
						ParticleGame.particleTemplate.setGravityEffect((float) ((reading / 3.33f) * Math.sqrt(2)));
					}
				}
			}
		});

		//Turn on labels at major tick marks.
		gravitySlider.setMajorTickSpacing(5);
		gravitySlider.setMinorTickSpacing(0);
		gravitySlider.setPaintTicks(true);
		gravitySlider.setPaintLabels(true);
		mainPanel.add(gravitySlider);
	}

	private void saveParticle() {
		JButton saveButton = new JButton("Save Particle");
		saveButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (ParticleGame.particleTemplate != null) {
					ParticleSaver.save(ParticleGame.particleTemplate);
				}
			}
		});

		mainPanel.add(saveButton);
	}
}
