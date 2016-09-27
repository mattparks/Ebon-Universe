package ebon.entities.components;

import ebon.entities.*;
import ebon.entities.loading.*;
import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;

import java.lang.reflect.*;
import java.util.*;

public class ComponentParticleSystem extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private ParticleSystem particleSystem;
	private Vector3f centreOffset;
	private Vector3f lastPosition;

	public ComponentParticleSystem(Entity entity, List<ParticleTemplate> types, IParticleSpawn spawn, float pps, float speed, float gravityEffect) {
		super(entity, ID);
		particleSystem = new ParticleSystem(types, spawn, pps, speed, gravityEffect);
		particleSystem.setSystemCentre(new Vector3f());
		centreOffset = new Vector3f();
		lastPosition = new Vector3f();
	}

	public ComponentParticleSystem(Entity entity, EntityTemplate template) {
		super(entity, ID);
		String[] templates = template.getSectionData(this, "Templates");
		List<ParticleTemplate> templateList = new ArrayList<>();

		for (int t = 0; t < templates.length; t++) {
			templateList.add(ParticleLoader.load(templates[t]));
		}

		String spawnClasspath = template.getValue(this, "Spawn");
		IParticleSpawn particleSpawn = null;

		String[] spawnValues = template.getSectionData(this, "SpawnValues");

		try {
			Class componentClass = Class.forName(spawnClasspath);
			Class[] componentTypes = new Class[]{String[].class};
			@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
			Object[] componentParameters = new Object[]{spawnValues};
			Object object = componentConstructor.newInstance(componentParameters);
			particleSpawn = (IParticleSpawn) object;
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
			FlounderLogger.error("While loading a particle spawn type, " + spawnClasspath + ", constructor could not be found!");
			FlounderLogger.exception(e);
		}

		particleSystem = new ParticleSystem(templateList, particleSpawn, Float.parseFloat(template.getValue(this, "PPS")), Float.parseFloat(template.getValue(this, "Speed")), Float.parseFloat(template.getValue(this, "GravityEffect")));
		particleSystem.randomizeRotation();
		particleSystem.setSystemCentre(new Vector3f());
		centreOffset = new Vector3f().set(template.getValue(this, "CentreOffset"));
		lastPosition = new Vector3f();
	}

	@Override
	public void update() {
		if (particleSystem != null) {
			if (particleSystem.getTypes().isEmpty()) {
				particleSystem.addParticleType(ParticleLoader.load("cosmic"));
				particleSystem.addParticleType(ParticleLoader.load("cosmicHot"));
			}

			if (super.getEntity().hasMoved()) {
				Vector3f translated = new Vector3f(centreOffset);
				Vector3f.rotate(translated, super.getEntity().getRotation(), translated);
				Vector3f.add(translated, super.getEntity().getPosition(), translated);

				Vector3f diffrence = Vector3f.subtract(lastPosition, translated, null);
				lastPosition.set(translated);

				particleSystem.getSystemCentre().set(translated);
				particleSystem.getCentreVelocity().set(diffrence);
			}
		}
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	public Vector3f getCentreOffset() {
		return centreOffset;
	}

	@Override
	public void dispose() {
		FlounderParticles.removeSystem(particleSystem);
		particleSystem = null;
	}
}
