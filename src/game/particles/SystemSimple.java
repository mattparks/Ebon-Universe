package game.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class SystemSimple implements IParticleSystem {
	private final List<ParticleType> types;
	private final Vector3f direction;
	private final Vector3f systemCenter;
	private float pps, averageSpeed;
	private float speedError;
	private boolean randomRotation;
	private float directionDeviation;

	public SystemSimple(final float pps, final float speed, List<ParticleType> types) {
		this.pps = pps;
		averageSpeed = speed;
		this.types = types;
		direction = new Vector3f();
		systemCenter = new Vector3f();
	}

	private float generateRotation() {
		if (randomRotation) {
			return Maths.RANDOM.nextFloat() * 360f;
		} else {
			return 0;
		}
	}

	/**
	 * @param direction The average direction in which engine.particles are emitted.
	 * @param deviation A value between 0 and 1 indicating how far from the chosen direction engine.particles can deviate.
	 */
	public void setDirection(final Vector3f direction, final float deviation) {
		this.direction.set(direction);
		directionDeviation = (float) (deviation * Math.PI);
	}

	public void randomizeRotation() {
		randomRotation = true;
	}

	/**
	 * @param error A number between 0 and 1, where 0 means no error margin.
	 */
	public void setSpeedError(final float error) {
		speedError = error * averageSpeed;
	}

	public void setSystemCenter(final Vector3f systemCenter) {
		this.systemCenter.set(systemCenter);
	}

	@Override
	public void update() {
		if (!ManagerDevices.getKeyboard().getKey(GLFW_KEY_Y)) {
			float delta = FlounderEngine.getDelta();
			float particlesToCreate = pps * delta;
			int count = (int) Math.floor(particlesToCreate);
			float partialParticle = particlesToCreate % 1;

			for (int i = 0; i < count; i++) {
				emitParticle();
			}

			if (Math.random() < partialParticle) {
				emitParticle();
			}
		}
	}

	private void emitParticle() {
		Vector3f velocity;

		if (direction != null) {
			velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
		} else {
			velocity = generateRandomUnitVector();
		}

		velocity.normalize().scale(generateValue(averageSpeed, speedError));
		new Particle(types.get((int) Math.floor(Maths.randomInRange(0, types.size()))), new Vector3f(systemCenter), velocity); // TODO: Use second value for chance!
	}

	private static Vector3f generateRandomUnitVectorWithinCone(final Vector3f coneDirection, final float angle) {
		float cosAngle = (float) Math.cos(angle);
		float theta = (float) (Maths.RANDOM.nextFloat() * 2.0f * Math.PI);
		float z = cosAngle + Maths.RANDOM.nextFloat() * (1.0f - cosAngle);
		float rootOneMinusZSquared = (float) Math.sqrt(1.0f - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));

		Vector4f direction = new Vector4f(x, y, z, 1.0f);

		if (coneDirection.x != 0 || coneDirection.y != 0.0f || coneDirection.z != 1.0f && coneDirection.z != -1.0f) {
			Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0.0f, 0.0f, 1.0f), null);
			rotateAxis.normalize();
			float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0.0f, 0.0f, 1.0f)));
			Matrix4f rotationMatrix = new Matrix4f();
			Matrix4f.rotate(rotationMatrix, rotateAxis, -rotateAngle, rotationMatrix);
			Matrix4f.transform(rotationMatrix, direction, direction);
		} else if (coneDirection.z == -1.0f) {
			direction.z *= -1.0f;
		}

		return new Vector3f(direction);
	}

	private Vector3f generateRandomUnitVector() {
		float theta = (float) (Maths.RANDOM.nextFloat() * 2.0f * Math.PI);
		float z = Maths.RANDOM.nextFloat() * 2 - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		return new Vector3f(x, y, z);
	}

	private float generateValue(final float average, final float errorMargin) {
		float offset = (Maths.RANDOM.nextFloat() - 0.5f) * 2.0f * errorMargin;
		return average + offset;
	}
}