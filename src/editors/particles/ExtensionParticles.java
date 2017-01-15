package editors.particles;

import ebon.particles.*;
import ebon.particles.loading.*;
import ebon.particles.spawns.*;
import ebon.world.*;
import editors.editor.*;
import flounder.camera.*;
import flounder.devices.*;
import flounder.helpers.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;

import java.util.*;

public class ExtensionParticles extends IEditorType {
	private static boolean ACTIVE = false;

	public ParticleTemplate particleTemplate;
	public ParticleSystem particleSystem;
	public String loadFromParticle;

	public ExtensionParticles() {
		super(FlounderMouse.class, EbonParticles.class, EbonWorld.class);
		ACTIVE = true;
	}

	@Override
	public void init() {
		// Sets the engine up for the editor.
		// FlounderProfiler.toggle(true);
		FlounderMouse.setCursorHidden(false);
		OpenGlUtils.goWireframe(false);

		// Sets the world to constant fog and a sun.
		EbonWorld.addFog(new Fog(new Colour(1.0f, 1.0f, 1.0f), 0.003f, 2.0f, 0.0f, 50.0f));
		EbonWorld.addSun(new Light(new Colour(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 2000.0f, 2000.0f)));

		// The template to edit.
		particleTemplate = new ParticleTemplate("testing", null, 1.0f, 1.0f);

		// The testing particle system.
		particleSystem = new ParticleSystem(new ArrayList<>(), null, 376.0f, 10.0f, 1.0f);
		particleSystem.addParticleType(particleTemplate);
		particleSystem.randomizeRotation();
		particleSystem.setSpawn(new SpawnSphere(12));
		particleSystem.setSystemCentre(FlounderCamera.getPlayer().getPosition());
	}

	@Override
	public void update() {
		if (loadFromParticle != null) {
			ParticleTemplate template = EbonParticles.load(loadFromParticle);
			particleSystem.removeParticleType(particleTemplate);
			particleTemplate = template;
			particleSystem.addParticleType(particleTemplate);

			FrameParticles.nameField.setText(template == null ? "null" : template.getName());
			FrameParticles.rowSlider.setValue(template == null ? -1 : (template.getTexture() != null ? template.getTexture().getNumberOfRows() : 0));
			FrameParticles.scaleSlider.setValue((int) (template == null ? -1.0f : template.getScale() * 100.0f));
			FrameParticles.lifeSlider.setValue((int) (template == null ? 0.0f : template.getLifeLength() * 10.0f));

			loadFromParticle = null;
		}
	}

	@Override
	public void profile() {
	}

	@Override
	public void dispose() {
		ACTIVE = false;
	}

	@Override
	public boolean isActive() {
		return ACTIVE;
	}
}
