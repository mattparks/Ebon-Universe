package game.entities.components;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;
import game.entities.*;
import game.entities.loading.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class ParticleSystemComponent extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private ParticleSystem particleSystem;

	public ParticleSystemComponent(Entity entity, List<ParticleTemplate> types, IParticleSpawn spawn, float pps, float speed) {
		super(entity, ID);
		particleSystem = new ParticleSystem(types, spawn, pps, speed);
		particleSystem.setSystemCentre(super.getEntity().getPosition());
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

		particleSystem = new ParticleSystem(templateList, particleSpawn, Float.parseFloat(template.getValue(this, "PPS")), Float.parseFloat(template.getValue(this, "Speed")));
		particleSystem.randomizeRotation();
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
				for (String values : particleSystem.getSpawn().getSavableValues()) {
					String s = values + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};

		String particleSpawn = "Spawn: " + particleSystem.getSpawn().getClass().getName();
		String particlePPS = "PPS: " + particleSystem.getPps();
		String particleSpeed = "Speed: " + particleSystem.getAverageSpeed();

		return new Pair<>(new String[]{particleSpawn, particlePPS, particleSpeed}, new EntitySaverFunction[]{saveTemplates, saveSpawnValues});
	}

	@Override
	public void update() {

	}
}
