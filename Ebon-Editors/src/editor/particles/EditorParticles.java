package editor.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.particles.*;
import flounder.particles.loading.*;
import flounder.particles.spawns.*;
import flounder.profiling.*;
import flounder.resources.*;

import java.util.*;

public class EditorParticles extends FlounderEntrance {
	public static EditorParticles instance;

	public ParticleTemplate particleTemplate;
	public ParticleSystem particleSystem;
	public String loadFromParticle;

	public ParticlesFrame frame;

	public EditorParticles(ICamera camera, IRendererMaster renderer, IManagerGUI managerGUI) {
		super(
				camera, renderer, managerGUI,
				FlounderDisplay.class, FlounderMouse.class, FlounderFonts.class, FlounderGuis.class, FlounderParticles.class
		);
		FlounderDisplay.setup(1080, 720, "Ebon Particle Editor", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "flounder.png")}, true, true, 8, false);
		instance = this;
	}

	@Override
	public void init() {
		FlounderProfiler.toggle(false);
		FlounderMouse.setCursorHidden(false);

		// The template to edit.
		particleTemplate = new ParticleTemplate("testing", null, 1.0f, 1.0f);

		// The testing particle system.
		particleSystem = new ParticleSystem(new ArrayList<>(), null, 376.0f, 10.0f, 1.0f);
		particleSystem.addParticleType(particleTemplate);
		particleSystem.randomizeRotation();
		particleSystem.setSpawn(new SpawnSphere(12));
		particleSystem.setSystemCentre(focusPosition);

		frame = new ParticlesFrame();
	}

	@Override
	public void update() {
		if (loadFromParticle != null) {
			ParticleTemplate template = FlounderParticles.load(loadFromParticle);
			particleSystem.removeParticleType(particleTemplate);
			particleTemplate = template;
			particleSystem.addParticleType(particleTemplate);

			FlounderParticles.clear();

			frame.nameField.setText(template.getName());
			frame.rowSlider.setValue(template.getTexture() != null ? template.getTexture().getNumberOfRows() : 0);
			frame.scaleSlider.setValue((int) (template.getScale() * 100.0f));
			frame.lifeSlider.setValue((int) (template.getLifeLength() * 10.0f));

			loadFromParticle = null;
		}

		update(focusPosition, focusRotation);
	}

	@Override
	public void profile() {
	}

	@Override
	public void dispose() {
		instance = null;
	}
}
