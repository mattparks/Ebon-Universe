package game.entities;

import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.textures.*;

public class EntityBuilder {
	private Vector3f position;
	private Vector3f rotation;
	private float scale;

	private Model model;
	private Texture texture;
	private Texture normalMap;
	private int textureIndex;

	public EntityBuilder(Model model, Texture texture) {
		this.scale = 1.0f;
		this.model = model;
		this.texture = texture;
		this.textureIndex = 0;
	}

	/**
	 * Creates a new entity, carries out the CPU loading.
	 *
	 * @return The entity that has been created.
	 */
	public Entity create() {
		return new Entity(position, rotation, scale, model, texture, normalMap);
	}

	public EntityBuilder setPosition(Vector3f position) {
		this.position = position;
		return this;
	}

	public EntityBuilder setRotation(Vector3f rotation) {
		this.rotation = rotation;
		return this;
	}

	public EntityBuilder setScale(float scale) {
		this.scale = scale;
		return this;
	}

	public EntityBuilder setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
		return this;
	}

	public EntityBuilder setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
		return this;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public Model getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

	public Texture getNormalMap() {
		return normalMap;
	}

	public int getTextureIndex() {
		return textureIndex;
	}
}
