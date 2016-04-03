package game.particles;

import flounder.textures.*;

public class ParticleType {
	private final Texture texture;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;

	public ParticleType(final Texture texture, final float gravityEffect, final float lifeLength, final float rotation, final float scale) {
		this.texture = texture;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Texture getTexture() {
		return texture;
	}

	public float getGravityEffect() {
		return gravityEffect;
	}

	public float getLifeLength() {
		return lifeLength;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
}
