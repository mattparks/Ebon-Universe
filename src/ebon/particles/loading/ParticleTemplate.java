package ebon.particles.loading;

import flounder.maths.vectors.*;
import flounder.textures.*;

/**
 * A definition for what a particle should act and look like.
 */
public class ParticleTemplate {
	private String name;
	private Texture texture;
	private float lifeLength;
	private float scale;

	/**
	 * Creates a new particle type.
	 *
	 * @param name The name for the particle type.
	 * @param texture The particles texture.
	 * @param lifeLength The averaged life length for the particle.
	 * @param scale The averaged scale for the particle.
	 */
	public ParticleTemplate(String name, Texture texture, float lifeLength, float scale) {
		this.name = name;
		this.texture = texture;
		this.lifeLength = lifeLength;
		this.scale = scale;
	}

	public static Vector3f createVector3f(String source) {
		String reduced = source.replace("Vector3f(", "").replace(")", "").trim();
		String[] split = reduced.split("\\|");
		float x = Float.parseFloat(split[0].substring(2, split[0].length()));
		float y = Float.parseFloat(split[1].substring(2, split[0].length()));
		float z = Float.parseFloat(split[2].substring(2, split[0].length()));
		return new Vector3f(x, y, z);
	}

	public static String saveVector3f(Vector3f source) {
		return "Vector3f(" + "x=" + source.x + "| y=" + source.y + "| z=" + source.z + ")";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void setLifeLength(float lifeLength) {
		this.lifeLength = lifeLength;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public String getName() {
		return name;
	}

	public Texture getTexture() {
		return texture;
	}

	public float getLifeLength() {
		return lifeLength;
	}

	public float getScale() {
		return scale;
	}
}
