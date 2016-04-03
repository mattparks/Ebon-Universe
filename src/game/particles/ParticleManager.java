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

	public static Map<ParticleType, List<Particle>> particles;
	private static AABB reusableAABB;
	private static ProfileTab profileTab;

	public static void init() {
		particles = new HashMap<>();
		reusableAABB = new AABB(new Vector3f(), new Vector3f());
		profileTab = new ProfileTab("Particles");
		FlounderProfiler.addTab(profileTab);
	}

	public static void update() {
		for (ParticleType p : particles.keySet()) {
			final Iterator<Particle> iterator = particles.get(p).iterator();

			while (iterator.hasNext()) {
				final Particle particle = iterator.next();
				particle.update(!ManagerDevices.getKeyboard().getKey(GLFW_KEY_Y));

				if (!particle.isAlive()) {
					iterator.remove();

					if (particles.get(particle.getParticleType()).isEmpty()) {
						particles.remove(particle.getParticleType());
					}
				}
			}
		}

		int particlesTotal = 0;
		for (ParticleType t : particles.keySet()) {
			for (Particle p : particles.get(t)) {
				particlesTotal++;
			}
		}
		profileTab.addLabel("Total Particles", "" + particlesTotal);
	}

	public static List<Particle> getParticles(List<Particle> viewableParticles, final ICamera camera) {
		viewableParticles.clear();

		for (final ParticleType p : particles.keySet()) {
			for (final Particle i : particles.get(p)) {
				float SIZE = 0.5f * p.getScale();

				reusableAABB.getMinExtents().set(i.getPosition().getX() - SIZE, i.getPosition().getY() - SIZE, i.getPosition().getZ() - SIZE);
				reusableAABB.getMaxExtents().set(i.getPosition().getX() + SIZE, i.getPosition().getY() + SIZE, i.getPosition().getZ() + SIZE);

				if (camera.getViewFrustum().aabbInFrustum(reusableAABB)) {
					viewableParticles.add(i);
				}
			}
		}

		// Added to engine.particles first -> last, so no initial reverse needed.
		viewableParticles = SortingAlgorithms.insertionSort(viewableParticles);
		Collections.reverse(viewableParticles); // Reverse as the sorted list should be close(small) -> far(big).
		return viewableParticles;
	}

	public static void addParticle(final Particle particle) {
		final ParticleType type = particle.getParticleType();
		final List<Particle> batch = particles.get(type);

		if (batch != null) {
			batch.add(particle);
		} else {
			final List<Particle> newBatch = new ArrayList<>();
			newBatch.add(particle);
			particles.put(type, newBatch);
		}
	}
}
