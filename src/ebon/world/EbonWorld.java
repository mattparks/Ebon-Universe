package ebon.world;

import ebon.entities.*;
import ebon.particles.*;
import ebon.universe.galaxies.*;
import flounder.framework.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.space.*;

public class EbonWorld extends IModule {
	private static final EbonWorld instance = new EbonWorld();

	private StructureBasic<Light> lights;
	private Fog fog;

	public EbonWorld() {
		super(ModuleUpdate.UPDATE_POST, EbonParticles.class, EbonEntities.class, EbonGalaxies.class);
	}

	@Override
	public void init() {
		this.fog = new Fog(new Colour(0.0f, 0.0f, 0.0f), 0.003f, 2.0f, 0.0f, 50.0f);
		this.lights = new StructureBasic<>();
	}

	@Override
	public void run() {

	}

	@Override
	public void profile() {

	}

	public static void clear() {
		EbonParticles.clear();
		EbonEntities.clear();
		EbonGalaxies.clear();

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
