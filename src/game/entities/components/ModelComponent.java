package game.entities.components;

import flounder.maths.vectors.*;
import flounder.physics.*;
import game.entities.*;
import game.models.*;

/**
 * Creates a model with a texture that can be rendered into the world.
 */
public class ModelComponent extends IEntityComponent {
	public static final int ID = EntityAssignerID.getId();

	private final ModelTextured model;
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
	public ModelComponent(final Entity entity, final ModelTextured model, final float scale) {
		this(entity, model, scale, 0);
	}

	/**
	 * Creates a new ModelComponent.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The model that will be attached too this entity.
	 * @param scale The scale of the entity.
	 * @param textureIndex What texture index this entity should renderObjects from (0 default).
	 */
	public ModelComponent(final Entity entity, final ModelTextured model, final float scale, final int textureIndex) {
		super(entity, ID);
		this.model = model;
		transparency = 1.0f;
		this.scale = scale;
		this.textureIndex = textureIndex;

		//AABBMesh gottenAABBMesh = model.getModel().getAABBMesh();
		AABB gottenAABB = model.getModel().getAABB().scale(new Vector3f(scale, scale, scale));

		ColliderComponent c = (ColliderComponent) getEntity().getComponent(ColliderComponent.ID);

		if (c != null) {
			c.fitAABB(gottenAABB); // gottenAABBMesh,
		}
	}

	/**
	 * Gets the textures coordinate offset that is used in rendering the model.
	 *
	 * @return The coordinate offset used in rendering.
	 */
	public Vector2f getTextureOffset() {
		int column = textureIndex % model.getModelTexture().getNumberOfRows();
		int row = textureIndex / model.getModelTexture().getNumberOfRows();
		return new Vector2f((float) row / (float) model.getModelTexture().getNumberOfRows(), (float) column / (float) model.getModelTexture().getNumberOfRows());
	}

	public ModelTextured getModel() {
		return model;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(final float transparency) {
		this.transparency = transparency;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(final float scale) {
		this.scale = scale;
	}

	public void setTextureIndex(final int index) {
		this.textureIndex = index;
	}

	@Override
	public void update() {
		// model.getModel().getAabbMesh().setPosition(super.createEntity().getPosition());
	}
}
