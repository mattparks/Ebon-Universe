package game.particles;

import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.textures.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class SystemPlayer implements IParticleSystem {
	private final ParticleType type;

	private float pps, averageSpeed;
	private float speedError;
	private boolean randomRotation;
	private Vector3f direction;
	private Vector3f systemCenter;
	private float directionDeviation;

	private Random random = new Random();

	public SystemPlayer(Texture texture, float pps, float speed, float gravityCompliant, float lifeLength, float scale) {
		this.pps = pps;
		averageSpeed = speed;
		type = new ParticleType(texture, gravityCompliant, lifeLength, generateRotation(), scale);
	}

	private float generateRotation() {
		if (randomRotation) {
			return random.nextFloat() * 360f;
		} else {
			return 0;
		}
	}

	/**
	 * @param direction The average direction in which engine.particles are emitted.
	 * @param deviation A value between 0 and 1 indicating how far from the chosen direction engine.particles can deviate.
	 */
	public void setDirection(Vector3f direction, float deviation) {
		this.direction = direction;
		directionDeviation = (float) (deviation * Math.PI);
	}

	public void randomizeRotation() {
		randomRotation = true;
	}

	/**
	 * @param error A number between 0 and 1, where 0 means no error margin.
	 */
	public void setSpeedError(float error) {
		speedError = error * averageSpeed;
	}

	public void setSystemCenter(Vector3f systemCenter) {
		this.systemCenter = systemCenter;
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
		new Particle(type, new Vector3f(systemCenter), velocity);
	}

	private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		Random random = new Random();
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = cosAngle + random.nextFloat() * (1 - cosAngle);
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));

		Vector4f direction = new Vector4f(x, y, z, 1);

		if (coneDirection.x != 0 || coneDirection.y != 0 || coneDirection.z != 1 && coneDirection.z != -1) {
			Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
			rotateAxis.normalize();
			float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
			Matrix4f rotationMatrix = new Matrix4f();
			Matrix4f.rotate(rotationMatrix, rotateAxis, -rotateAngle, rotationMatrix);
			Matrix4f.transform(rotationMatrix, direction, direction);
		} else if (coneDirection.z == -1) {
			direction.z *= -1;
		}

		return new Vector3f(direction);
	}

	private Vector3f generateRandomUnitVector() {
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = random.nextFloat() * 2 - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		return new Vector3f(x, y, z);
	}

	private float generateValue(float average, float errorMargin) {
		float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
		return average + offset;
	}
}