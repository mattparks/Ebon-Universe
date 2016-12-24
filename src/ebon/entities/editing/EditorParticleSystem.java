package ebon.entities.editing;


import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.editing.particles.*;
import ebon.entities.loading.*;
import ebon.particles.loading.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.physics.bounding.*;

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
	public Sphere spawnCentre;
	public boolean renderCentre;

	public EditorParticleSystem(Entity entity) {
		this.component = new ComponentParticleSystem(entity, new ArrayList<>(), null, 100.0f, 1.0f, 1.0f);
		this.spawnCentre = new Sphere(0.5f);
		this.renderCentre = true;
	}

	public EditorParticleSystem(IEntityComponent component) {
		this.component = (ComponentParticleSystem) component;
		this.spawnCentre = new Sphere(0.5f);
		this.renderCentre = true;
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
		JSlider ppsSlider = new JSlider(JSlider.HORIZONTAL, 0, 2500, (int) component.getParticleSystem().getPPS());
		ppsSlider.setToolTipText("Particles Per Second");
		ppsSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				component.getParticleSystem().setPps(reading);
			}
		});

		// Turn on labels at major tick marks.
		ppsSlider.setMajorTickSpacing(500);
		ppsSlider.setMinorTickSpacing(100);
		ppsSlider.setPaintTicks(true);
		ppsSlider.setPaintLabels(true);
		panel.add(ppsSlider);

		// Gravity Effect Slider.
		//	panel.add(new JLabel("Gravity Slider: "));
		JSlider gravityEffectSlider = new JSlider(JSlider.HORIZONTAL, -150, 150, (int) (component.getParticleSystem().getGravityEffect() * 100.0f));
		gravityEffectSlider.setToolTipText("Gravity Effect");
		gravityEffectSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				component.getParticleSystem().setGravityEffect(reading / 100.0f);
			}
		});

		// Turn on labels at major tick marks.
		gravityEffectSlider.setMajorTickSpacing(50);
		gravityEffectSlider.setMinorTickSpacing(10);
		gravityEffectSlider.setPaintTicks(true);
		gravityEffectSlider.setPaintLabels(true);
		panel.add(gravityEffectSlider);

		// Speed Slider.
		//	panel.add(new JLabel("Speed Slider: "));
		JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, (int) (component.getParticleSystem().getAverageSpeed() * 10.0f));
		speedSlider.setToolTipText("Speed Slider");
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				component.getParticleSystem().setAverageSpeed(reading / 10.0f);
			}
		});

		// Turn on labels at major tick marks.
		speedSlider.setMajorTickSpacing(30);
		speedSlider.setMinorTickSpacing(5);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		panel.add(speedSlider);

		// X Offset Field.
		JSpinner xOffsetField = new JSpinner(new SpinnerNumberModel((double) component.getCentreOffset().x, Double.NEGATIVE_INFINITY + 1.0, Double.POSITIVE_INFINITY - 1.0, 0.1));
		xOffsetField.setToolTipText("Particle System X Offset");
		xOffsetField.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				component.getCentreOffset().x = (float) (double) ((JSpinner) e.getSource()).getValue();
			}
		});
		panel.add(xOffsetField);

		// Y Offset Field.
		JSpinner yOffsetField = new JSpinner(new SpinnerNumberModel((double) component.getCentreOffset().x, Double.NEGATIVE_INFINITY + 1.0, Double.POSITIVE_INFINITY - 1.0, 0.1));
		yOffsetField.setToolTipText("Particle System Y Offset");
		yOffsetField.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				component.getCentreOffset().y = (float) (double) ((JSpinner) e.getSource()).getValue();
			}
		});
		panel.add(yOffsetField);

		// Z Offset Field.
		JSpinner zOffsetField = new JSpinner(new SpinnerNumberModel((double) component.getCentreOffset().x, Double.NEGATIVE_INFINITY + 1.0, Double.POSITIVE_INFINITY - 1.0, 0.1));
		yOffsetField.setToolTipText("Particle System Z Offset");
		zOffsetField.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				component.getCentreOffset().z = (float) (double) ((JSpinner) e.getSource()).getValue();
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
					IEditorComponent.REMOVE_SIDE_TAB.add(getTabName() + " (" + classname.split("\\.")[ByteWork.getCharCount(classname, '.')].replace("Spawn", "") + ")");
				}

				if (particleSpawn != null) {
					component.getParticleSystem().setSpawn(particleSpawn.getComponent());
					systemSpawn = particleSpawn;

					JPanel panel = IEditorComponent.makeTextPanel();
					particleSpawn.addToPanel(panel);
					IEditorComponent.ADD_SIDE_TAB.add(new Pair<>(getTabName() + " (" + particleSpawn.getTabName() + ")", panel));
				}
			}
		});
		panel.add(componentAdd);

		// TODO: Add selection list for particle templates to be used in the types list.
	}

	@Override
	public void update() {
		if (renderCentre) {
			Sphere.recalculate(new Sphere(0.5f), component.getCentreOffset(), 1.0f, spawnCentre); // TODO: Fix offset!
			FlounderBounding.addShapeRender(spawnCentre);
		}
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		EntitySaverFunction saveTemplates = new EntitySaverFunction("Templates") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				for (ParticleTemplate template : component.getParticleSystem().getTypes()) {
					String s = template.getName() + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};

		EntitySaverFunction saveSpawnValues = new EntitySaverFunction("SpawnValues") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getParticleSystem().getSpawn() != null) {
					for (String values : systemSpawn.getSavableValues()) {
						String s = values + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};

		String particleSpawn = "Spawn: " + (component.getParticleSystem().getSpawn() == null ? null : component.getParticleSystem().getSpawn().getClass().getName());
		String particlePPS = "PPS: " + component.getParticleSystem().getPPS();
		String particleSpeed = "Speed: " + component.getParticleSystem().getAverageSpeed();
		String particleGravity = "GravityEffect: " + component.getParticleSystem().getGravityEffect();
		String particleCentreOffset = "CentreOffset: " + ParticleTemplate.saveVector3f(component.getCentreOffset());

		return new Pair<>(
				new String[]{particleSpawn, particlePPS, particleSpeed, particleGravity, particleCentreOffset},
				new EntitySaverFunction[]{saveTemplates, saveSpawnValues}
		);
	}
}
