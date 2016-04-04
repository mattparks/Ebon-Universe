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

	private static List<IParticleSystem> particleSystems;

	private static List<List<Particle>> particles;
	private static AABB reusableAABB;

	public static void init() {
		particleSystems = new ArrayList<>();
		particles = new ArrayList<>();
		reusableAABB = new AABB(new Vector3f(), new Vector3f());
	}

	public static void update() {
		particleSystems.forEach(IParticleSystem::update);

		int totalParticles = 0;
		int visibleParticles = 0;

		for (final List<Particle> list : particles) {
			final Iterator<Particle> iterator = list.iterator();

			while (iterator.hasNext()) {
				// Iterate and update the particles.
				final Particle particle = iterator.next();
				particle.update(!ManagerDevices.getKeyboard().getKey(GLFW_KEY_Y));

				// Update particle visibility.
				float SIZE = 0.5f * particle.getParticleType().getScale();
				reusableAABB.getMinExtents().set(particle.getPosition().getX() - SIZE, particle.getPosition().getY() - SIZE, particle.getPosition().getZ() - SIZE);
				reusableAABB.getMaxExtents().set(particle.getPosition().getX() + SIZE, particle.getPosition().getY() + SIZE, particle.getPosition().getZ() + SIZE);
				particle.setVisable(FlounderEngine.getCamera().getViewFrustum().aabbInFrustum(reusableAABB));
				visibleParticles += particle.isVisable() ? 1 : 0;

				// Remove particles that are not alive.
				if (!particle.isAlive()) {
					iterator.remove();

					if (list.isEmpty()) {
						particles.remove(list);
					}
				} else {
					totalParticles++;
				}
			}
		}

		for (final List<Particle> list : particles) {
			// Added to engine.particles first -> last, so no initial reverse needed.
			SortingAlgorithms.insertionSort(list);
			Collections.reverse(list); // Reverse as the sorted list should be close(small) -> far(big).
		}

		if (FlounderProfiler.isOpen()) {
			FlounderProfiler.add("Particles", "Systems", particleSystems.size());
			FlounderProfiler.add("Particles", "Types", particles.size());
			FlounderProfiler.add("Particles", "Particles", totalParticles);
			FlounderProfiler.add("Particles", "Visible", visibleParticles);
		}
	}

	public static List<List<Particle>> getParticles() {
		return particles;
	}

	public static void addSystem(final IParticleSystem system) {
		particleSystems.add(system);
	}

	public static void addParticle(final Particle particle) {
		for (List<Particle> list : particles) {
			if (list.get(0).getParticleType().equals(particle.getParticleType())) {
				list.add(particle);
				return;
			}
		}

		List<Particle> list = new ArrayList<>();
		list.add(particle);
		particles.add(list);
	}
}
