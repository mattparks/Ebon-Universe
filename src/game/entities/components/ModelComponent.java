package game.entities.components;

import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.textures.*;
import game.entities.*;

/**
 * Creates a model with a texture that can be rendered into the world.
 */
public class ModelComponent extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private Model model;
	private Texture texture;
	private Texture normalMap;
	private float transparency;
	private float scale;
	private int textureIndex;

	/**
	 * Creates a new ModelComponent.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The model that will be attached too this entity.
	 * @param scale The scale of the entity.
	 */
	public ModelComponent(Entity entity, Model model, Texture texture, float scale) {
		this(entity, model, texture, null, 1.0f, 0);
	}

	/**
	 * Creates a new ModelComponent.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The model that will be attached too this entity.
	 * @param scale The scale of the entity.
	 * @param textureIndex What texture index this entity should renderObjects from (0 default).
	 */
	public ModelComponent(Entity entity, Model model, Texture texture, Texture normalMap, float scale, int textureIndex) {
		super(entity, ID);
		this.model = model;
		this.texture = texture;
		this.normalMap = normalMap;
		this.transparency = 1.0f;
		this.scale = scale;
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
		return new Vector2f((float) row / (float) texture.getNumberOfRows(), (float) column / (float) texture.getNumberOfRows());
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

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setTextureIndex(int index) {
		this.textureIndex = index;
	}

	@Override
	public void update() {
	}
}
