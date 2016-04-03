package game.particles;

import flounder.engine.*;
import flounder.maths.vectors.*;
import game.*;

public class Particle implements Comparable<Particle> {
	private final ParticleType particleType;
	public Vector3f reusableChange;
	private Vector3f position;
	private Vector3f velocity;
	private float elapsedTime;
	private float transparency;
	private Vector2f textureOffset1;
	private Vector2f textureOffset2;
	private float textureBlendFactor;
	private float distanceToCamera;

	public Particle(ParticleType particleType, Vector3f position, Vector3f velocity) {
		this.particleType = particleType;
		this.position = position;
		this.velocity = velocity;
		elapsedTime = 0;
		transparency = 0;
		reusableChange = new Vector3f();
		ParticleManager.addParticle(this);
	}

	protected void update(boolean moveParticle, ICamera camera) {
		if (moveParticle) {
			velocity.y += Environment.GRAVITY * particleType.getGravityEffect() * FlounderEngine.getDelta();
			reusableChange.set(velocity);
			reusableChange.scale(FlounderEngine.getDelta());

			//float groundHeight = World.getTerrainHeight(position.getX() + change.getX(), position.getZ() + change.getZ()) + 1;

			//if (position.getY() + change.getY() < groundHeight) {
			//	change.y = groundHeight - position.getY(); // Move back too the surface.
			//}

			Vector3f.add(reusableChange, position, position);
			elapsedTime += FlounderEngine.getDelta();

			if (elapsedTime > particleType.getLifeLength()) {
				transparency += 1.0f * FlounderEngine.getDelta();
			}
		}

		distanceToCamera = Vector3f.subtract(camera.getPosition(), position, null).lengthSquared(); // FIXME: Could be improved!

		float lifeFactor = elapsedTime / particleType.getLifeLength();
		int stageCount = (int) Math.pow(particleType.getTexture().getNumberOfRows(), 2);
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;

		this.textureBlendFactor = atlasProgression % 1;
		this.textureOffset1 = createTextureOffset(index1);
		this.textureOffset2 = createTextureOffset(index2);
	}

	private Vector2f createTextureOffset(int index) {
		Vector2f offset = new Vector2f();
		int column = index % particleType.getTexture().getNumberOfRows();
		int row = index / particleType.getTexture().getNumberOfRows();
		offset.x = (float) column / particleType.getTexture().getNumberOfRows();
		offset.y = (float) row / particleType.getTexture().getNumberOfRows();
		return offset;
	}

	public ParticleType getParticleType() {
		return particleType;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getTransparency() {
		return transparency;
	}

	public boolean isAlive() {
		return transparency < 1.0;
	}

	public Vector2f getTextureOffset1() {
		return textureOffset1;
	}

	public Vector2f getTextureOffset2() {
		return textureOffset2;
	}

	public float getTextureBlendFactor() {
		return textureBlendFactor;
	}

	@Override
	public int compareTo(Particle other) {
		return ((Float) distanceToCamera).compareTo(other.getDistance());
	}

	public float getDistance() {
		return distanceToCamera;
	}
}
