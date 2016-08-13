package game.editors.particles;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;
import game.*;

import java.util.*;

public class ParticleGame extends IGame {
	public static ParticleSystem particleSystem;

	public ParticleGame() {
	}

	@Override
	public void init() {
		FlounderEngine.getProfiler().toggle(false);
		FlounderEngine.getCursor().show(true);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);

		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f, false), 0.001f, 2.0f, 0.0f, 50.0f), null);

		// The testing particle system.
		particleSystem = new ParticleSystem(new ArrayList<>(), null, 376.0f, 10.0f);
		particleSystem.addParticleType(new ParticleTemplate("loading", null, 1.0f, 1.0f, 1.0f));
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
