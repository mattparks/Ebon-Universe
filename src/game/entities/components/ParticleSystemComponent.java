package game.entities.components;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;
import game.editors.entity.*;
import game.entities.*;
import game.entities.loading.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class ParticleSystemComponent extends IEntityComponent {
	private String[] spawns = new String[]{
			SpawnCircle.class.getName(),
			SpawnLine.class.getName(),
			SpawnPoint.class.getName(),
			SpawnSphere.class.getName(),
	};

	public static final int ID = EntityIDAssigner.getId();

	private ParticleSystem particleSystem;
	private Vector3f centreOffset;
	private Vector3f lastPosition;

	public ParticleSystemComponent(Entity entity) {
		super(entity, ID);
		particleSystem = new ParticleSystem(new ArrayList<>(), null, 100.0f, 1.0f, 1.0f);
		particleSystem.setSystemCentre(new Vector3f());
		centreOffset = new Vector3f();
		lastPosition = new Vector3f();
	}

	public ParticleSystemComponent(Entity entity, List<ParticleTemplate> types, IParticleSpawn spawn, float pps, float speed, float gravityEffect) {
		super(entity, ID);
		particleSystem = new ParticleSystem(types, spawn, pps, speed, gravityEffect);
		particleSystem.setSystemCentre(new Vector3f());
		centreOffset = new Vector3f();
		lastPosition = new Vector3f();
	}

	public ParticleSystemComponent(Entity entity, EntityTemplate template) {
		super(entity, ID);
		String[] templates = EntityTemplate.toStringArray(template.getSectionData(this, "Templates"));
		List<ParticleTemplate> templateList = new ArrayList<>();

		for (int t = 0; t < templates.length; t++) {
			templateList.add(ParticleLoader.load(templates[t]));
		}

		String spawnClasspath = template.getValue(this, "Spawn");
		IParticleSpawn particleSpawn = null;

		String[] spawnValues = EntityTemplate.toStringArray(template.getSectionData(this, "SpawnValues"));

		try {
			Class componentClass = Class.forName(spawnClasspath);
			Class[] componentTypes = new Class[]{String[].class};
			@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
			Object[] componentParameters = new Object[]{spawnValues};
			Object object = componentConstructor.newInstance(componentParameters);
			particleSpawn = (IParticleSpawn) object;
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			FlounderEngine.getLogger().error("While loading a particle spawn type, " + spawnClasspath + ", constructor could not be found!");
			FlounderEngine.getLogger().exception(e);
		}

		particleSystem = new ParticleSystem(templateList, particleSpawn, Float.parseFloat(template.getValue(this, "PPS")), Float.parseFloat(template.getValue(this, "Speed")), Float.parseFloat(template.getValue(this, "GravityEffect")));
		particleSystem.randomizeRotation();
		particleSystem.setSystemCentre(new Vector3f());
		centreOffset = new Vector3f().set(template.getValue(this, "CentreOffset"));
		lastPosition = new Vector3f();
	}

	@Override
	public void addToPanel(JPanel panel) {
		// PPS Slider.
	//	panel.add(new JLabel("PPS Slider: "));
		JSlider ppsSlider = new JSlider(JSlider.HORIZONTAL, 0, 2500, (int) particleSystem.getPps());
		ppsSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				particleSystem.setPps(reading);
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
		JSlider gravityEffectSlider = new JSlider(JSlider.HORIZONTAL, -150, 150, (int) (particleSystem.getGravityEffect() * 100.0f));
		gravityEffectSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				particleSystem.setGravityEffect(reading / 100.0f);
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
		JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, (int) (particleSystem.getAverageSpeed() * 10.0f));
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();
				particleSystem.setAverageSpeed(reading / 10.0f);
			}
		});

		//Turn on labels at major tick marks.
		speedSlider.setMajorTickSpacing(30);
		speedSlider.setMinorTickSpacing(5);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		panel.add(speedSlider);

		// X Offset Field.
		JTextField xOffsetField = new JTextField("" + particleSystem.getSystemCentre().x);
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
				ParticleSystemComponent.this.centreOffset.x = Float.parseFloat(xOffsetField.getText());
			}
		});
		panel.add(xOffsetField);

		// Y Offset Field.
		JTextField yOffsetField = new JTextField("" + particleSystem.getSystemCentre().y);
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
				ParticleSystemComponent.this.centreOffset.y = Float.parseFloat(yOffsetField.getText());
			}
		});
		panel.add(yOffsetField);

		// Z Offset Field.
		JTextField zOffsetField = new JTextField("" + particleSystem.getSystemCentre().z);
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
				ParticleSystemComponent.this.centreOffset.z = Float.parseFloat(zOffsetField.getText());
			}
		});
		panel.add(zOffsetField);

		// Component Dropdown.
		JComboBox componentDropdown = new JComboBox();
		for (int i = 0; i < spawns.length; i++) {
			componentDropdown.addItem(spawns[i].split("\\.")[ByteWork.getCharCount(spawns[i], '.')].replace("Spawn", ""));
		}
		panel.add(componentDropdown);

		// Component Add Button.
		JButton componentAdd = new JButton("Set Spawn");
		componentAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String spawn = (String) componentDropdown.getSelectedItem();
				IParticleSpawn particleSpawn = null;

				for (int i = 0; i < spawns.length; i++) {
					if (spawns[i].split("\\.")[ByteWork.getCharCount(spawns[i], '.')].replace("Spawn", "").equals(spawn)) {
						try {
							FlounderEngine.getLogger().log("Adding component: " + spawn);
							Class componentClass = Class.forName(spawns[i]);
							Class[] componentTypes = new Class[]{};
							@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
							Object[] componentParameters = new Object[]{};
							particleSpawn = (IParticleSpawn) componentConstructor.newInstance(componentParameters);
						} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
							FlounderEngine.getLogger().error("While loading particle spawn" + spawns[i] + "'s constructor could not be found!");
							FlounderEngine.getLogger().exception(ex);
						}
					}
				}

				if (ParticleSystemComponent.this.particleSystem.getSpawn() != null) {
					String classname = particleSystem.getSpawn().getClass().getName();
					EntityFrame.removeSideTab(ParticleSystemComponent.class.getName().split("\\.")[ByteWork.getCharCount(ParticleSystemComponent.class.getName(), '.')].replace("Component", "") + " (" + classname.split("\\.")[ByteWork.getCharCount(classname, '.')].replace("Spawn", "") + ")");
				}

				if (particleSpawn != null) {
					String classname = particleSpawn.getClass().getName();
					ParticleSystemComponent.this.particleSystem.setSpawn(particleSpawn);

					JPanel panel = EntityFrame.makeTextPanel();
					particleSpawn.addToPanel(panel);
					EntityFrame.addSideTab(ParticleSystemComponent.class.getName().split("\\.")[ByteWork.getCharCount(ParticleSystemComponent.class.getName(), '.')].replace("Component", "") + " (" + classname.split("\\.")[ByteWork.getCharCount(classname, '.')].replace("Spawn", "") + ")", panel);
				}
			}
		});
		panel.add(componentAdd);
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		EntitySaverFunction saveTemplates = new EntitySaverFunction("Templates") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (ParticleTemplate template : particleSystem.getTypes()) {
					String s = template.getName() + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};

		EntitySaverFunction saveSpawnValues = new EntitySaverFunction("SpawnValues") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (particleSystem.getSpawn() != null) {
					for (String values : particleSystem.getSpawn().getSavableValues()) {
						String s = values + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};

		String particleSpawn = "Spawn: " + (particleSystem.getSpawn() == null ? null : particleSystem.getSpawn().getClass().getName());
		String particlePPS = "PPS: " + particleSystem.getPps();
		String particleSpeed = "Speed: " + particleSystem.getAverageSpeed();
		String particleGravity = "GravityEffect: " + particleSystem.getGravityEffect();
		String particleCentreOffset = "CentreOffset: " + centreOffset.toString();

		return new Pair<>(new String[]{particleSpawn, particlePPS, particleSpeed, particleGravity, particleCentreOffset}, new EntitySaverFunction[]{saveTemplates, saveSpawnValues});
	}

	@Override
	public void update() {
		if (particleSystem != null) {
			if (particleSystem.getTypes().isEmpty()) {
				particleSystem.addParticleType(ParticleLoader.load("cosmic"));
				particleSystem.addParticleType(ParticleLoader.load("cosmicHot"));
			}

		//	if(super.getEntity().hasMoved()) {
				Vector3f translated = new Vector3f(centreOffset);
				Vector3f.rotate(translated, super.getEntity().getRotation(), translated);
				Vector3f.add(translated, super.getEntity().getPosition(), translated);

				Vector3f diffrence = Vector3f.subtract(lastPosition, translated, null);
				lastPosition.set(translated);

				particleSystem.getSystemCentre().set(translated);
				particleSystem.getCentreVelocity().set(diffrence);
		//	}
		}
	}

	@Override
	public void dispose() {
		FlounderEngine.getParticles().removeSystem(particleSystem);
		particleSystem = null;
	}
}
