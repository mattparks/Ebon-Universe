package ebon.world;

import ebon.entities.*;
import ebon.particles.*;
import flounder.animation.*;
import flounder.devices.*;
import flounder.entities.*;
import flounder.framework.*;
import flounder.lights.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.space.*;

public class EbonWorld extends IModule {
	private static final EbonWorld INSTANCE = new EbonWorld();
	public static final String PROFILE_TAB_NAME = "Ebon World";

	private StructureBasic<Light> lights;
	private Fog fog;

	private EbonTesting ebonTesting;

	public EbonWorld() {
		super(ModuleUpdate.UPDATE_POST, PROFILE_TAB_NAME, FlounderLogger.class, FlounderProfiler.class, FlounderEntities.class, FlounderAnimation.class, EbonParticles.class);
	}

	@Override
	public void init() {
		this.fog = new Fog(new Colour(1.0f, 1.0f, 1.0f), 0.003f, 2.0f, 0.0f, 50.0f);
		this.lights = new StructureBasic<>();

		if (FlounderDisplay.getTitle().equals("Ebon Universe")) {
			//	this.ebonTesting = new EbonTesting();
			//	 FlounderEntities.load("dragon").createEntity(FlounderEntities.getEntities(), new Vector3f(0.0f, -5.0f, 10.0f), new Vector3f());
			InstanceCowboy e = new InstanceCowboy(FlounderEntities.getEntities(), new Vector3f(0.0f, -5.0f, 10.0f), new Vector3f(0, 180, 0));
			// EbonEntities.load("cowboy").createEntity(EbonEntities.getEntities(), new Vector3f(0.0f, -5.0f, 10.0f), new Vector3f(0, 180, 0));
		}

		System.out.println("Helvete!");
	}

	@Override
	public void update() {
		if (ebonTesting != null) {
			ebonTesting.update();
		}
	}

	@Override
	public void profile() {
	}

	public static void clear() {
		System.gc();
	}

	public static void addFog(Fog fog) {
		INSTANCE.fog = fog;
	}

	public static void addSun(Light sun) {
		INSTANCE.lights.add(sun);
	}

	public static StructureBasic<Light> getLights() {
		return INSTANCE.lights;
	}

	public static Fog getFog() {
		return INSTANCE.fog;
	}

	@Override
	public IModule getInstance() {
		return INSTANCE;
	}

	@Override
	public void dispose() {
		fog = null;

		if (lights != null) {
			lights.clear();
			lights = null;
		}
	}
}
