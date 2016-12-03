package ebon.world;

import ebon.entities.*;
import ebon.particles.*;
import ebon.universe.galaxies.*;
import flounder.camera.*;
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

	public EbonWorld() {
		super(ModuleUpdate.UPDATE_POST, FlounderLogger.class, FlounderProfiler.class, FlounderCamera.class, EbonEntities.class, EbonParticles.class, EbonGalaxies.class);
	}

	@Override
	public void init() {
		this.fog = new Fog(new Colour(0.0f, 0.0f, 0.0f), 0.003f, 2.0f, 0.0f, 50.0f);
		this.lights = new StructureBasic<>();
	}

	@Override
	public void update() {
	}

	@Override
	public void profile() {
	}

	public static void generateWorlds() {
		EbonWorld.addFog(new Fog(new Colour(0.0f, 0.0f, 0.0f), 0.003f, 2.0f, 0.0f, 50.0f));
		EbonWorld.addSun(new Light(new Colour(0.85f, 0.85f, 0.85f), new Vector3f(0.0f, 2000.0f, 2000.0f)));
		EbonGalaxies.generateGalaxy();
		// EbonEntities.load("barrel").createEntity(EbonEntities.getEntities(), new Vector3f(), new Vector3f());

		// EntityLoader.load("dragon").createEntity(Environment.getEntitys(), new Vector3f(30, 0, 0), new Vector3f());
		/*EntityLoader.load("pane").createEntity(Environment.getEntities(), new Vector3f(), new Vector3f());
		EntityLoader.load("sphere").createEntity(Environment.getEntities(), Environment.getLights().get(0).position, new Vector3f());
		for (int n = 0; n < 32; n++) {
			for (int p = 0; p < 32; p++) {
				for (int q = 0; q < 32; q++) {
					if (Maths.RANDOM.nextInt(10) == 1) {
						EntityLoader.load("crate").createEntity(Environment.getEntities(), new Vector3f((n * 5) + 10, (p * 5) + 10, (q * 5) + 10), new Vector3f(0, Maths.RANDOM.nextInt(360), 0));
					}
				}
			}
		}*/
	}

	public static void clear() {
		if (FlounderCamera.getPlayer() != null) {
			FlounderCamera.getPlayer().init();
		}

		if (FlounderCamera.getCamera() != null) {
			FlounderCamera.getCamera().init();
		}

		EbonParticles.clear();
		EbonEntities.clear();
		EbonGalaxies.clear();
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
