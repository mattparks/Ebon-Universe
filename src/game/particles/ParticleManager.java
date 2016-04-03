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

	public static List<List<Particle>> particles;
	private static AABB reusableAABB;
	private static ProfileTab profileTab;

	public static void init() {
		particles = new ArrayList<>();
		reusableAABB = new AABB(new Vector3f(), new Vector3f());
		profileTab = new ProfileTab("Particles");
		FlounderProfiler.addTab(profileTab);
	}

	public static void update() {
		int totalParticles = 0;

		for (List<Particle> list : particles) {
			final Iterator<Particle> iterator = list.iterator();

			while (iterator.hasNext()) {
				final Particle particle = iterator.next();
				particle.update(!ManagerDevices.getKeyboard().getKey(GLFW_KEY_Y));

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

		profileTab.addLabel("Types", particles.size());
		profileTab.addLabel("Particles", totalParticles);
	}

	public static List<List<Particle>> getParticles(List<List<Particle>> viewableParticles, final ICamera camera) {
		viewableParticles.clear();

		for (List<Particle> particleList : particles) {
			List<Particle> viewableParticleList = new ArrayList<>();

			for (final Particle particle : particleList) {
				float SIZE = 0.5f * particle.getParticleType().getScale();
				reusableAABB.getMinExtents().set(particle.getPosition().getX() - SIZE, particle.getPosition().getY() - SIZE, particle.getPosition().getZ() - SIZE);
				reusableAABB.getMaxExtents().set(particle.getPosition().getX() + SIZE, particle.getPosition().getY() + SIZE, particle.getPosition().getZ() + SIZE);

				if (camera.getViewFrustum().aabbInFrustum(reusableAABB)) {
					viewableParticleList.add(particle);
				}
			}

			// Added to engine.particles first -> last, so no initial reverse needed.
			viewableParticleList = SortingAlgorithms.insertionSort(viewableParticleList);
			Collections.reverse(viewableParticles); // Reverse as the sorted list should be close(small) -> far(big).

			viewableParticles.add(viewableParticleList);
		}

		return viewableParticles;
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
