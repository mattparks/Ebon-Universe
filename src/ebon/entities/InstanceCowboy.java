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

import static org.lwjgl.opengl.GL30.*;

public class InstanceCowboy extends Entity {
	public InstanceCowboy(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);

		MyFile colladaFile = new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.dae");
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(colladaFile, AnimationSettings.MAX_WEIGHTS);
		int vao = FlounderLoader.createVAO();
		FlounderLoader.createIndicesVBO(vao, entityData.getMeshData().getIndices());
		FlounderLoader.storeDataInVBO(vao, entityData.getMeshData().getVertices(), 0, 3);
		FlounderLoader.storeDataInVBO(vao, entityData.getMeshData().getTextureCoords(), 1, 2);
		FlounderLoader.storeDataInVBO(vao, entityData.getMeshData().getNormals(), 2, 3);
		FlounderLoader.storeIntDataInVBO(vao, entityData.getMeshData().getJointIds(), 3, 3);
		FlounderLoader.storeDataInVBO(vao, entityData.getMeshData().getVertexWeights(), 4, 3);
		glBindVertexArray(0);
		int indicies = entityData.getMeshData().getIndices().length;
		Joint headJoint = createJoints(entityData.getJointsData().headJoint);

		Animation animation = AnimationCreator.loadAnimation(colladaFile);

		/*System.out.print("ModelData[]{ ");
		for (int i = 0; i < entityData.getMeshData().getVertexWeights().length; i++) {
			if (i % 25 == 0) {
				System.out.print("\n    ");
			}
			System.out.print(entityData.getMeshData().getVertexWeights()[i] + ", ");
		}
		System.out.println(" \n};");*/

		Texture t = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.png")).create();
		t.setNumberOfRows(1);

		//new ComponentCollision(this);
		//new ComponentCollider(this, true);
		//new ComponentModel(this, m, t, null, 1.36f, 1);
		ComponentAnimation ean = new ComponentAnimation(this, vao, indicies, t, headJoint, entityData.getJointsData().jointCount, 1.0f);
		ean.doAnimation(animation);
	}

	private static Joint createJoints(JointData data) {
		Joint j = new Joint(data.index, data.nameId, data.bindLocalTransform);

		for (JointData child : data.children) {
			j.addChild(createJoints(child));
		}

		return j;
	}
}
