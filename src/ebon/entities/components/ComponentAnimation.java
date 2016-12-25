package ebon.entities.components;

import ebon.entities.*;
import ebon.entities.loading.*;
import flounder.animation.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.textures.*;

/**
 * Creates a animation used to set animation properties.
 */
public class ComponentAnimation extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private int modelVao;
	private int indexCount;
	private Texture texture;

	private Joint rootJoint;
	private int jointCount;

	private Animator animator;
	private float scale;

	/**
	 * Creates a new ComponentAnimation.
	 *
	 * @param entity The entity this component is attached to.
	 * @param modelVao The VAO containing the mesh data for this entity. This
	 * includes vertex positions, normals, texture coords, IDs of
	 * joints that affect each vertex, and their corresponding
	 * weights.
	 * @param texture The diffuse texture for the entity.
	 * @param rootJoint The root joint of the joint hierarchy which makes up the
	 * "skeleton" of the entity.
	 * @param jointCount The number of joints in the joint hierarchy for this entity.
	 */
	public ComponentAnimation(Entity entity, int modelVao, int indexCount, Texture texture, Joint rootJoint, int jointCount, float scale) {
		super(entity, ID);
		this.modelVao = modelVao;
		this.indexCount = indexCount;
		this.texture = texture;
		this.rootJoint = rootJoint;
		rootJoint.calculateInverseBindTransform(Matrix4f.rotate(new Matrix4f(), new Vector3f(1.0f, 0.0f, 0.0f), (float) Math.toRadians(-90.0f), null));
		this.jointCount = jointCount;
		this.animator = new Animator(rootJoint);
		this.scale = scale;
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
	 * Gets an array of the model-space transforms of all the joints (with the
	 * current animation pose applied) in the entity. The joints are ordered in
	 * the array based on their joint index. The position of each joint's
	 * transform in the array is equal to the joint's index.
	 *
	 * @return The array of model-space transforms of the joints in the current animation pose.
	 */
	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}

	/**
	 * This adds the current model-space transform of a joint (and all of its
	 * descendants) into an array of transforms. The joint's transform is added
	 * into the array at the position equal to the joint's index.
	 *
	 * @param headJoint
	 * @param jointMatrices
	 */
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();

		for (Joint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

	/**
	 * @return The VAO containing all the mesh data for this entity.
	 */
	public int getModel() {
		return modelVao;
	}

	public int getIndexCount() {
		return indexCount;
	}

	/**
	 * @return The diffuse texture for this entity.
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * @return The root joint of the joint hierarchy. This joint has no parent,
	 * and every other joint in the skeleton is a descendant of this
	 * joint.
	 */
	public Joint getRootJoint() {
		return rootJoint;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void dispose() {
		//	model.delete();
		texture.delete();
	}
}
