package game.editors.particles;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.particles.spawns.*;
import flounder.resources.*;
import flounder.textures.*;
import game.*;

import java.util.*;

public class ParticleGame extends IGame {
	public ParticleSystem particleSystem;

	public ParticleGame() {
	}

	@Override
	public void init() {
		FlounderEngine.getProfiler().toggle(false);
		FlounderEngine.getCursor().show(true);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);

		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f, false), 0.001f, 2.0f, 0.0f, 50.0f), null);

		// The testing particle system.
		particleSystem = new ParticleSystem(new ArrayList<>(), null, 1000.0f, 10.0f);

		// Creates a new smoke particle type.
		Texture smokeTexture = Texture.newTexture(new MyFile(FlounderParticles.PARTICLES_LOC, "smoke.png")).createInBackground();
		smokeTexture.setNumberOfRows(8);
		ParticleType smokeParticleType = new ParticleType(smokeTexture, 0.1f, 1.5f, 5.0f);
		particleSystem.addParticleType(smokeParticleType);

		// Creates a new fire particle type.
		Texture fireTexture = Texture.newTexture(new MyFile(FlounderParticles.PARTICLES_LOC, "fire.png")).createInBackground();
		fireTexture.setNumberOfRows(8);
		ParticleType fireParticleType = new ParticleType(fireTexture, 0.1f, 1.5f, 5.0f);
		particleSystem.addParticleType(fireParticleType);

		// Adds everything to the system.
		particleSystem.setSpawn(new SpawnCone(new Vector3f(0, 1, 1), 150));
		particleSystem.setSystemCentre(focusPosition);

		ParticleFrame frame = new ParticleFrame();
	}

	@Override
	public void update() {
		update(focusPosition, focusRotation, false, 0.0f);
		Environment.update();
	}

	@Override
	public void dispose() {
	}
}
