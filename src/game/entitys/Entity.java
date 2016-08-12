package game.entitys;

import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.textures.*;

public class Entity {
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private Matrix4f modelMatrix;

	private Model model;
	private Texture texture;
	private Texture normalMap;
	private int textureIndex;
	private Vector2f textureOffset;

	protected Entity() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.scale = 1.0f;
		this.modelMatrix = new Matrix4f();
		this.model = null;
		this.texture = null;
		this.normalMap = null;
		this.textureIndex = 0;
		this.textureOffset = new Vector2f();
	}

	protected Entity(Vector3f position, Vector3f rotation, float scale, Model model, Texture texture, Texture normalMap) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.modelMatrix = new Matrix4f();
		this.model = model;
		this.texture = texture;
		this.normalMap = normalMap;
		this.textureIndex = 0;
		this.textureOffset = new Vector2f();
	}

	/**
	 * Creates a new Entity Builder.
	 *
	 * @param model The entity model file to be loaded.
	 * @param texture The entity texture file to be loaded.
	 *
	 * @return A new Entity Builder.
	 */
	public static EntityBuilder newEntity(Model model, Texture texture) {
		return new EntityBuilder(model, texture);
	}

	/**
	 * Creates a new empty Entity.
	 *
	 * @return A new empty Entity.
	 */
	public static Entity getEmptyEntity() {
		return new Entity();
	}

	public Matrix4f getModelMatrix() {
		modelMatrix.setIdentity();
		Matrix4f.transformationMatrix(position, rotation, scale, modelMatrix);
		return modelMatrix;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.set(x, y, z);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
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

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	/**
	 * Gets the textures coordinate offset that is used in rendering the model.
	 *
	 * @return The coordinate offset used in rendering.
	 */
	public Vector2f getTextureOffset() {
		int column = textureIndex % texture.getNumberOfRows();
		int row = textureIndex / texture.getNumberOfRows();
		return textureOffset.set((float) row / (float) texture.getNumberOfRows(), (float) column / (float) texture.getNumberOfRows());
	}
}
