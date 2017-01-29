package ebon.entities.components;

import flounder.animation.*;
import flounder.collada.*;
import flounder.collada.geometry.*;
import flounder.collada.joints.*;
import flounder.entities.*;
import flounder.entities.components.*;
import flounder.entities.template.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;
import flounder.textures.*;

import java.util.*;

/**
 * Creates a animation used to set animation properties.
 */
public class ComponentAnimation extends IComponentEntity {
	public static final int ID = EntityIDAssigner.getId();

	private ModelAnimated model;
	private float scale;
	private Matrix4f modelMatrix;

	private Texture texture;
	private int textureIndex;

	private Animator animator;

	/**
	 * Creates a new ComponentAnimation.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The animated model to use when animating and rendering.
	 * @param scale The scale of the entity.
	 * @param texture The diffuse texture for the entity.
	 * @param textureIndex What texture index this entity should renderObjects from (0 default).
	 */
	public ComponentAnimation(Entity entity, ModelAnimated model, float scale, Texture texture, int textureIndex) {
		super(entity, ID);
		this.model = model;
		this.scale = scale;
		this.modelMatrix = new Matrix4f();

		this.texture = texture;
		this.textureIndex = textureIndex;

		if (model != null) {
			model.getHeadJoint().calculateInverseBindTransform(Matrix4f.rotate(new Matrix4f(), new Vector3f(1.0f, 0.0f, 0.0f), (float) Math.toRadians(-90.0f), null));
			this.animator = new Animator(model.getHeadJoint());
		}
	}

	/**
	 * Creates a new ComponentAnimation. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ComponentAnimation(Entity entity, EntityTemplate template) {
		super(entity, ID);

		{
			String[] jointsData = template.getSectionData(ComponentAnimation.this, "Joints");
			Pair<JointData, List<String>> headJoint = null;
			Map<JointData, List<String>> allJoints = new HashMap<>();

			boolean isHeadJoint = false;
			int index = 0;
			String name = "";
			float[] localBindTransform = new float[16];
			List<String> children = new ArrayList<>();

			int id = 0;

			for (int i = 0; i <= jointsData.length; i++) {
				switch (id) {
					case 0:
						isHeadJoint = Boolean.parseBoolean(jointsData[i]);
						break;
					case 1:
						index = Integer.parseInt(jointsData[i]);
						break;
					case 2:
						name = jointsData[i];
						break;
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
						localBindTransform[id - 3] = Float.parseFloat(jointsData[i]);
						break;
					case 19:
						if (i < jointsData.length) {
							Collections.addAll(children, jointsData[i].split("\\|"));
						}

						if (isHeadJoint) {
							headJoint = new Pair<>(new JointData(index, name, new Matrix4f(localBindTransform)), new ArrayList<>(children));
						} else {
							allJoints.put(new JointData(index, name, new Matrix4f(localBindTransform)), new ArrayList<>(children));
						}

						isHeadJoint = false;
						index = 0;
						name = "";
						localBindTransform = new float[16];
						children = new ArrayList<>();
						id = -1;
						break;
				}

				id++;
			}

			addChildren(headJoint.getFirst(), headJoint.getSecond(), allJoints);

			this.model = new ModelAnimated(
					new MeshData(
							EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "Vertices")),
							EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "TextureCoords")),
							EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "Normals")),
							EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "Tangents")),
							EntityTemplate.toIntArray(template.getSectionData(ComponentAnimation.this, "Indices")),
							EntityTemplate.toIntArray(template.getSectionData(ComponentAnimation.this, "JointIds")),
							EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "VertexWeights")),
							Float.parseFloat(template.getValue(this, "FurthestPoint"))
					),
					new JointsData(
							Integer.parseInt(template.getValue(this, "JointCount")),
							headJoint.getFirst()
					)
			);
		}

		{
			float animationLength = Float.parseFloat(template.getValue(this, "AnimationLength"));
			String[] animationData = template.getSectionData(ComponentAnimation.this, "Animation");

			List<KeyFrameJoints> keyFrameJoints = new ArrayList<>();

			float timeStamp = 0.0f;
			String name = "";
			Vector3f position = new Vector3f();
			Quaternion rotation = new Quaternion();

			int id = 0;

			for (int i = 0; i < animationData.length; i++) {
				switch (id) {
					case 0:
						timeStamp = Float.parseFloat(animationData[i]);
						break;
					case 1:
						name = animationData[i];
						break;
					case 2:
						position.x = Float.parseFloat(animationData[i]);
						break;
					case 3:
						position.y = Float.parseFloat(animationData[i]);
						break;
					case 4:
						position.z = Float.parseFloat(animationData[i]);
						break;
					case 5:
						rotation.x = Float.parseFloat(animationData[i]);
						break;
					case 6:
						rotation.y = Float.parseFloat(animationData[i]);
						break;
					case 7:
						rotation.z = Float.parseFloat(animationData[i]);
						break;
					case 8:
						rotation.w = Float.parseFloat(animationData[i]);
						boolean set = false;

						for (KeyFrameJoints frame : keyFrameJoints) {
							if (frame.getTimeStamp() == timeStamp) {
								frame.getJointKeyFrames().put(name, new JointTransform(new Vector3f(position), new Quaternion(rotation)));
								set = true;
							}
						}

						if (!set) {
							KeyFrameJoints newFrame = new KeyFrameJoints(timeStamp, new HashMap<>());
							newFrame.getJointKeyFrames().put(name, new JointTransform(new Vector3f(position), new Quaternion(rotation)));
							keyFrameJoints.add(newFrame);
						}

						timeStamp = 0.0f;
						name = "";
						position = new Vector3f();
						rotation = new Quaternion();
						id = -1;
						break;
				}

				id++;
			}

			keyFrameJoints.sort((KeyFrameJoints p1, KeyFrameJoints p2) -> (int) (p1.getTimeStamp() - p2.getTimeStamp()));
			KeyFrameJoints[] frames = new KeyFrameJoints[keyFrameJoints.size()];

			for (int i = 0; i < frames.length; i++) {
				frames[i] = keyFrameJoints.get(i);
			}

			model.getHeadJoint().calculateInverseBindTransform(Matrix4f.rotate(new Matrix4f(), new Vector3f(1.0f, 0.0f, 0.0f), (float) Math.toRadians(-90.0f), null));
			this.animator = new Animator(model.getHeadJoint());

			Animation animation = new Animation(animationLength, frames);
			doAnimation(animation);
		}

		this.scale = Float.parseFloat(template.getValue(this, "Scale"));

		if (!template.getValue(this, "Texture").equals("null")) {
			this.texture = Texture.newTexture(new MyFile(template.getValue(this, "Texture"))).create();
			this.texture.setNumberOfRows(Integer.parseInt(template.getValue(this, "TextureNumRows")));
		}
	}

	/**
	 * Adds children from a JointData/Children map.
	 *
	 * @param datasJoint The joint to add children too.
	 * @param dataChild The children to be searching for.
	 * @param allJoints The joint map to match the child's name with, and to get the JointData from.
	 */
	private void addChildren(JointData datasJoint, List<String> dataChild, Map<JointData, List<String>> allJoints) {
		for (JointData data : allJoints.keySet()) {
			if (dataChild.contains(data.getNameId())) {
				datasJoint.addChild(data);
			}
		}

		for (JointData child : datasJoint.getChildren()) {
			addChildren(child, allJoints.get(child), allJoints);
		}
	}

	@Override
	public void update() {
		if (animator != null) {
			animator.update();
		}
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
	 * Gets the entitys model matrix.
	 *
	 * @return The entitys model matrix.
	 */
	public Matrix4f getModelMatrix() {
		modelMatrix.setIdentity();
		float scale = 1.0f;
		scale = (super.getEntity().getComponent(ComponentAnimation.ID) != null) ? ((ComponentAnimation) super.getEntity().getComponent(ComponentAnimation.ID)).getScale() : scale;
		Matrix4f.transformationMatrix(super.getEntity().getPosition(), super.getEntity().getRotation(), scale, modelMatrix);
		return modelMatrix;
	}

	/**
	 * Gets the animated model for this entity.
	 *
	 * @return The animated model for this entity.
	 */
	public ModelAnimated getModel() {
		return model;
	}

	public void setModel(ModelAnimated model) {
		if (this.model != model) {
			this.model = model;
			this.model.getHeadJoint().calculateInverseBindTransform(Matrix4f.rotate(new Matrix4f(), new Vector3f(1.0f, 0.0f, 0.0f), (float) Math.toRadians(-90.0f), null));
			this.animator = new Animator(this.model.getHeadJoint());
		}
	}

	/**
	 * Gets an array of the model-space transforms of all the joints (with the current animation pose applied) in the entity.
	 * The joints are ordered in the array based on their joint index.
	 * The position of each joint's transform in the array is equal to the joint's index.
	 *
	 * @return The array of model-space transforms of the joints in the current animation pose.
	 */
	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[model.getJointsData().getJointCount()];
		addJointsToArray(model.getHeadJoint(), jointMatrices);
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
		jointMatrices[headJoint.getIndex()] = headJoint.getAnimatedTransform();

		for (Joint childJoint : headJoint.getChildren()) {
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

	public Animator getAnimator() {
		return animator;
	}

	@Override
	public IBounding getBounding() {
		return null;
	}

	@Override
	public void dispose() {
	}
}
