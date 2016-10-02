package ebon.entities.editing;


import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.editing.particles.*;
import ebon.entities.loading.*;
import ebon.particles.loading.*;
import flounder.helpers.*;
import flounder.logger.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class EditorParticleSystem extends IEditorComponent {
	private IEditorParticleSpawn[] spawns = new IEditorParticleSpawn[]{
			new EditorParticleCircle(),
			new EditorParticleLine(),
			new EditorParticlePoint(),
			new EditorParticleSphere(),
	};

	public ComponentParticleSystem component;
	public IEditorParticleSpawn systemSpawn;

	public EditorParticleSystem(Entity entity) {
		this.component = new ComponentParticleSystem(entity, new ArrayList<>(), null, 100.0f, 1.0f, 1.0f);
	}

	public EditorParticleSystem(IEntityComponent component) {
		this.component = (ComponentParticleSystem) component;
	}

	@Override
	public String getTabName() {
		return "Particle System";
	}

	@Override
	public ComponentParticleSystem getComponent() {
		return component;
	}

	@Override
	public void addToPanel(JPanel panel) {
		// PPS Slider.
		//	panel.add(new JLabel("PPS Slider: "));
		JSlider ppsSlider = new JSlider(JSlider.HORIZONTAL, 0, 2500, (int) component.getParticleSystem().getPps());
		ppsSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				component.getParticleSystem().setPps(reading);
			}
		});

		//Turn on labels at major tick marks.
		ppsSlider.setMajorTickSpacing(500);
		ppsSlider.setMinorTickSpacing(100);
		ppsSlider.setPaintTicks(true);
		ppsSlider.setPaintLabels(true);
		panel.add(ppsSlider);

		// Gravity Effect Slider.
		//	panel.add(new JLabel("Gravity Slider: "));
		JSlider gravityEffectSlider = new JSlider(JSlider.HORIZONTAL, -150, 150, (int) (component.getParticleSystem().getGravityEffect() * 100.0f));
		gravityEffectSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				component.getParticleSystem().setGravityEffect(reading / 100.0f);
			}
		});

		//Turn on labels at major tick marks.
		gravityEffectSlider.setMajorTickSpacing(50);
		gravityEffectSlider.setMinorTickSpacing(10);
		gravityEffectSlider.setPaintTicks(true);
		gravityEffectSlider.setPaintLabels(true);
		panel.add(gravityEffectSlider);

		// Speed Slider.
		//	panel.add(new JLabel("Speed Slider: "));
		JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, (int) (component.getParticleSystem().getAverageSpeed() * 10.0f));
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				component.getParticleSystem().setAverageSpeed(reading / 10.0f);
			}
		});

		//Turn on labels at major tick marks.
		speedSlider.setMajorTickSpacing(30);
		speedSlider.setMinorTickSpacing(5);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		panel.add(speedSlider);

		// X Offset Field.
		JTextField xOffsetField = new JTextField("" + component.getCentreOffset().x);
		xOffsetField.getDocument().addDocumentListener(new DocumentListener() {
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
				component.getCentreOffset().x = Float.parseFloat(xOffsetField.getText());
			}
		});
		panel.add(xOffsetField);

		// Y Offset Field.
		JTextField yOffsetField = new JTextField("" + component.getCentreOffset().y);
		yOffsetField.getDocument().addDocumentListener(new DocumentListener() {
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
				component.getCentreOffset().y = Float.parseFloat(yOffsetField.getText());
			}
		});
		panel.add(yOffsetField);

		// Z Offset Field.
		JTextField zOffsetField = new JTextField("" + component.getCentreOffset().z);
		zOffsetField.getDocument().addDocumentListener(new DocumentListener() {
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
				component.getCentreOffset().z = Float.parseFloat(zOffsetField.getText());
			}
		});
		panel.add(zOffsetField);

		// Component Dropdown.
		JComboBox componentDropdown = new JComboBox();
		for (int i = 0; i < spawns.length; i++) {
			componentDropdown.addItem(spawns[i].getTabName());
		}
		panel.add(componentDropdown);

		// Component Add Button.
		JButton componentAdd = new JButton("Set Spawn");
		componentAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String spawn = (String) componentDropdown.getSelectedItem();
				IEditorParticleSpawn particleSpawn = null;

				for (int i = 0; i < spawns.length; i++) {
					if (spawns[i].getTabName().equals(spawn)) {
						try {
							FlounderLogger.log("Adding component: " + spawn);
							Class componentClass = Class.forName(spawns[i].getClass().getName());
							Class[] componentTypes = new Class[]{};
							@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
							Object[] componentParameters = new Object[]{};
							particleSpawn = (IEditorParticleSpawn) componentConstructor.newInstance(componentParameters);
						} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
							FlounderLogger.error("While loading particle spawn" + spawns[i] + "'s constructor could not be found!");
							FlounderLogger.exception(ex);
						}
					}
				}

				if (component.getParticleSystem().getSpawn() != null) {
					String classname = component.getParticleSystem().getSpawn().getClass().getName();
				//	EditorEntities.instance.frame.removeSideTab(ComponentParticleSystem.class.getName().split("\\.")[ByteWork.getCharCount(ComponentParticleSystem.class.getName(), '.')].replace("Component", "") + " (" + classname.split("\\.")[ByteWork.getCharCount(classname, '.')].replace("Spawn", "") + ")");
				}

				if (particleSpawn != null) {
					component.getParticleSystem().setSpawn(particleSpawn.getComponent());
					systemSpawn = particleSpawn;

					JPanel panel = IEditorComponent.makeTextPanel();
					particleSpawn.addToPanel(panel);
				//	EditorEntities.instance.frame.addSideTab(getTabName() + " (" + particleSpawn.getTabName() + ")", panel);
				}
			}
		});
		panel.add(componentAdd);
	}

	@Override
	public void update(Entity testEntity) {

	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		EntitySaverFunction saveTemplates = new EntitySaverFunction("Templates") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (ParticleTemplate template : component.getParticleSystem().getTypes()) {
					String s = template.getName() + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};

		EntitySaverFunction saveSpawnValues = new EntitySaverFunction("SpawnValues") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (component.getParticleSystem().getSpawn() != null) {
					for (String values : systemSpawn.getSavableValues()) {
						String s = values + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};

		String particleSpawn = "Spawn: " + (component.getParticleSystem().getSpawn() == null ? null : component.getParticleSystem().getSpawn().getClass().getName());
		String particlePPS = "PPS: " + component.getParticleSystem().getPps();
		String particleSpeed = "Speed: " + component.getParticleSystem().getAverageSpeed();
		String particleGravity = "GravityEffect: " + component.getParticleSystem().getGravityEffect();
		String particleCentreOffset = "CentreOffset: " + component.getParticleSystem().toString();

		return new Pair<>(
				new String[]{particleSpawn, particlePPS, particleSpeed, particleGravity, particleCentreOffset},
				new EntitySaverFunction[]{saveTemplates, saveSpawnValues}
		);
	}
}
