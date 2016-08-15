package game.editors.particles;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;
import flounder.resources.*;
import game.*;

import java.util.*;

public class ParticleGame extends IGame {
	public static ParticleTemplate particleTemplate;
	public static ParticleSystem particleSystem;
	public static String LOAD_FROM_PARTICLE;

	public ParticleGame() {
	}

	@Override
	public void init() {
		//	FlounderEngine.getProfiler().toggle(false);
		FlounderEngine.getCursor().show(true);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);

		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f, false), 0.001f, 2.0f, 0.0f, 50.0f), null);

		// The template to edit.
		particleTemplate = new ParticleTemplate("testing", null, 1.0f, 1.0f);

		// The testing particle system.
		particleSystem = new ParticleSystem(new ArrayList<>(), null, 376.0f, 10.0f, 1.0f);
		particleSystem.addParticleType(particleTemplate);
		particleSystem.randomizeRotation();
		particleSystem.setSpawn(new SpawnSphere(12));
		particleSystem.setSystemCentre(focusPosition);

		ParticleFrame frame = new ParticleFrame();
	}

	@Override
	public void update() {
		if (LOAD_FROM_PARTICLE != null) {
			ParticleTemplate template = ParticleLoader.load(LOAD_FROM_PARTICLE);
			ParticleGame.particleSystem.removeParticleType(ParticleGame.particleTemplate);
			ParticleGame.particleTemplate = template;
			ParticleGame.particleSystem.addParticleType(ParticleGame.particleTemplate);
			FlounderEngine.getParticles().clearAllParticles();
			ParticleFrame.nameField.setText(template.getName());
			ParticleFrame.rowSlider.setValue(template.getTexture() != null ? template.getTexture().getNumberOfRows() : 0);
			ParticleFrame.scaleSlider.setValue((int) (template.getScale() * 100.0f));
			ParticleFrame.lifeSlider.setValue((int) (template.getLifeLength() * 10.0f));
			LOAD_FROM_PARTICLE = null;
		}

		update(focusPosition, focusRotation, false, 0.0f);
		Environment.update();
	}

	@Override
	public void dispose() {
	}
}
