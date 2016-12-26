package ebon.entities.components;

import ebon.entities.*;
import ebon.entities.loading.*;
import flounder.animation.*;
import flounder.collada.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.textures.*;

/**
 * Creates a animation used to set animation properties.
 */
public class ComponentAnimation extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private ModelAnimated modelAnimated;
	private float scale;

	private Texture texture;
	private int textureIndex;

	private Animator animator;

	/**
	 * Creates a new ComponentAnimation.
	 *
	 * @param entity The entity this component is attached to.
	 * @param modelAnimated The animated model to use when animating and rendering.
	 * @param scale The scale of the entity.
	 * @param texture The diffuse texture for the entity.
	 * @param textureIndex What texture index this entity should renderObjects from (0 default).
	 */
	public ComponentAnimation(Entity entity, ModelAnimated modelAnimated, float scale, Texture texture, int textureIndex) {
		super(entity, ID);
		this.modelAnimated = modelAnimated;
		this.scale = scale;

		this.texture = texture;
		this.textureIndex = textureIndex;

		modelAnimated.getHeadJoint().calculateInverseBindTransform(Matrix4f.rotate(new Matrix4f(), new Vector3f(1.0f, 0.0f, 0.0f), (float) Math.toRadians(-90.0f), null));
		this.animator = new Animator(modelAnimated.getHeadJoint());
	}

	/**
	 * Creates a new ComponentAnimation. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ComponentAnimation(Entity entity, EntityTemplate template) {
		super(entity, ID);

		/*this.animation = Animation.newAnimation(new AnimationBuilder.LoadManual() {
			@Override
			public String getModelName() {
				return template.getEntityName();
			}

		//	@Override
		//	public float[] getAnimations() {
		//		return EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "Animations"));
		//	}

			// TODO: Load manually like a model.
		}).create();*/
	}

	@Override
	public void update() {
		animator.update();
	}

	/**
	 * Instructs this entity to carry out a given animation.
	 *
	 * @param animation The animation to be carried out.
	 */
	public void doAnimation(Animation animation) {
		animator.doAnimation(animation);
	}

	/**
	 * Gets the scale for this model.
	 *
	 * @return The scale for this model.
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Sets the scale for this model.
	 *
	 * @param scale The new scale.
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Gets the animated model for this entity.
	 *
	 * @return The animated model for this entity.
	 */
	public ModelAnimated getModelAnimated() {
		return modelAnimated;
	}

	public void setModelAnimated(ModelAnimated modelAnimated) {
		this.modelAnimated = modelAnimated;
	}

	/**
	 * Gets an array of the model-space transforms of all the joints (with the current animation pose applied) in the entity.
	 * The joints are ordered in the array based on their joint index.
	 * The position of each joint's transform in the array is equal to the joint's index.
	 *
	 * @return The array of model-space transforms of the joints in the current animation pose.
	 */
	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[modelAnimated.getJointsData().jointCount];
		addJointsToArray(modelAnimated.getHeadJoint(), jointMatrices);
		return jointMatrices;
	}

	/**
	 * This adds the current model-space transform of a joint (and all of its descendants) into an array of transforms.
	 * The joint's transform is added into the array at the position equal to the joint's index.
	 *
	 * @param headJoint The head joint to add children to.
	 * @param jointMatrices The matrices transformation to add with.
	 */
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();

		for (Joint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

	/**
	 * Gets the diffuse texture for this entity.
	 *
	 * @return The diffuse texture for this entity.
	 */
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
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
		return new Vector2f((float) row / (float) texture.getNumberOfRows(), (float) column / (float) texture.getNumberOfRows());
	}

	@Override
	public void dispose() {
		modelAnimated.delete();
		texture.delete();
	}
}
