package ebon.entities;

import ebon.entities.components.*;
import flounder.animation.*;
import flounder.collada.*;
import flounder.collada.joints.*;
import flounder.loaders.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;

public class InstanceCowboy extends Entity {
	public InstanceCowboy(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);

		MyFile colladaFile = new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.dae");
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(colladaFile, AnimationSettings.MAX_WEIGHTS);
		int vaoId = FlounderLoader.createVAO();
		int vaoLength = entityData.getMeshData().getIndices().length;
		FlounderLoader.createIndicesVBO(vaoId, entityData.getMeshData().getIndices());
		FlounderLoader.storeDataInVBO(vaoId, entityData.getMeshData().getVertices(), 0, 3);
		FlounderLoader.storeDataInVBO(vaoId, entityData.getMeshData().getTextures(), 1, 2);
		FlounderLoader.storeDataInVBO(vaoId, entityData.getMeshData().getNormals(), 2, 3);
		FlounderLoader.storeDataInVBO(vaoId, entityData.getMeshData().getJointIds(), 3, 3);
		FlounderLoader.storeDataInVBO(vaoId, entityData.getMeshData().getVertexWeights(), 4, 3);

		Joint headJoint = entityData.getHeadJoint();

		Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.png")).create();
		texture.setNumberOfRows(1);

		Animation animation = AnimationCreator.loadAnimation(colladaFile);

		new ComponentCollision(this);
		new ComponentCollider(this, true);
		//new ComponentModel(this, m, t, null, 1.36f, 1);
		ComponentAnimation componentAnimation = new ComponentAnimation(this, vaoId, vaoLength, headJoint, entityData.getJointsData().jointCount, texture, 1.0f, 1);
		componentAnimation.doAnimation(animation);
	}
}
