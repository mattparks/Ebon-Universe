package game.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.profiling.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class ParticleManager {
	public static final MyFile PARTICLES_LOC = new MyFile(MyFile.RES_FOLDER, "particles");

	public static List<Particle> particles;
	private static AABB reusableAABB;
	private static ProfileTab profileTab;

	public static void init() {
		particles = new ArrayList<>();
		reusableAABB = new AABB(new Vector3f(), new Vector3f());
		profileTab = new ProfileTab("Particles");
		FlounderProfiler.addTab(profileTab);
	}

	public static void update() {
		final Iterator<Particle> iterator = particles.iterator();

		while (iterator.hasNext()) {
			final Particle particle = iterator.next();
			particle.update(!ManagerDevices.getKeyboard().getKey(GLFW_KEY_Y));

			if (!particle.isAlive()) {
				iterator.remove();
			}
		}

		profileTab.addLabel("Total", particles.size());
	}

	public static List<Particle> getParticles(List<Particle> viewableParticles, final ICamera camera) {
		viewableParticles.clear();

		for (final Particle particle : particles) {
			float SIZE = 0.5f * particle.getParticleType().getScale();
			reusableAABB.getMinExtents().set(particle.getPosition().getX() - SIZE, particle.getPosition().getY() - SIZE, particle.getPosition().getZ() - SIZE);
			reusableAABB.getMaxExtents().set(particle.getPosition().getX() + SIZE, particle.getPosition().getY() + SIZE, particle.getPosition().getZ() + SIZE);

			if (camera.getViewFrustum().aabbInFrustum(reusableAABB)) {
				viewableParticles.add(particle);
			}
		}

		// Added to engine.particles first -> last, so no initial reverse needed.
		viewableParticles = SortingAlgorithms.insertionSort(viewableParticles);
		Collections.reverse(viewableParticles); // Reverse as the sorted list should be close(small) -> far(big).
		return viewableParticles;
	}

	public static void addParticle(final Particle particle) {
		particles.add(particle);
	}
}
