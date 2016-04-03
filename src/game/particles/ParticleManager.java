package game.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class ParticleManager {
	public static final MyFile PARTICLES_LOC = new MyFile(MyFile.RES_FOLDER, "particles");

	public static Map<ParticleType, List<Particle>> particles = new HashMap<>();

	public static void update(ICamera camera) {
		for (ParticleType p : particles.keySet()) {
			Iterator<Particle> iterator = particles.get(p).iterator();

			while (iterator.hasNext()) {
				Particle particle = iterator.next();
				particle.update(!ManagerDevices.getKeyboard().getKey(GLFW_KEY_Y), camera);

				if (!particle.isAlive()) {
					iterator.remove();

					if (particles.get(particle.getParticleType()).isEmpty()) {
						particles.remove(particle.getParticleType());
					}
				}
			}
		}
	}

	public static List<Particle> getParticles(ICamera camera) {
		List<Particle> viewableParticles = new ArrayList<>();

		for (ParticleType p : particles.keySet()) {
			for (Particle i : particles.get(p)) {
				float SIZE = 0.5f * p.getScale();

				if (camera.getViewFrustum().aabbInFrustum(new AABB(new Vector3f(i.getPosition().getX() - SIZE, i.getPosition().getY() - SIZE, i.getPosition().getZ() - SIZE), new Vector3f(i.getPosition().getX() + SIZE, i.getPosition().getY() + SIZE, i.getPosition().getZ() + SIZE)))) {
					viewableParticles.add(i);
				}
			}
		}

		// Added to engine.particles first -> last, so no initial reverse needed.
		viewableParticles = SortingAlgorithms.insertionSort(viewableParticles);
		Collections.reverse(viewableParticles); // Reverse as the sorted list is small -> big.
		return viewableParticles;
	}

	public static void addParticle(Particle particle) {
		ParticleType type = particle.getParticleType();
		List<Particle> batch = particles.get(type);

		if (batch != null) {
			batch.add(particle);
		} else {
			List<Particle> newBatch = new ArrayList<>();
			newBatch.add(particle);
			particles.put(type, newBatch);
		}
	}
}
