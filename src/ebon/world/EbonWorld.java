package ebon.world;

import ebon.entities.*;
import ebon.particles.*;
import flounder.devices.*;
import flounder.framework.*;
import flounder.lights.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.space.*;

public class EbonWorld extends IModule {
	private static final EbonWorld instance = new EbonWorld();

	private StructureBasic<Light> lights;
	private Fog fog;

	private EbonTesting ebonTesting;

	public EbonWorld() {
		super(ModuleUpdate.UPDATE_POST, FlounderLogger.class, FlounderProfiler.class, EbonEntities.class, EbonParticles.class);
	}

	@Override
	public void init() {
		this.fog = new Fog(new Colour(0.0f, 0.0f, 0.0f), 0.003f, 2.0f, 0.0f, 50.0f);
		this.lights = new StructureBasic<>();

		if (FlounderDisplay.getTitle().equals("Ebon Universe")) {
			this.ebonTesting = new EbonTesting();
			EbonEntities.load("dragon").createEntity(EbonEntities.getEntities(), new Vector3f(0.0f, 0.0f, 5.0f), new Vector3f());
		}
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
		instance.fog = fog;
	}

	public static void addSun(Light sun) {
		instance.lights.add(sun);
	}

	public static StructureBasic<Light> getLights() {
		return instance.lights;
	}

	public static Fog getFog() {
		return instance.fog;
	}

	@Override
	public IModule getInstance() {
		return instance;
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
