package ebon.entities;

import ebon.entities.components.*;
import flounder.animation.*;
import flounder.collada.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;

public class InstanceCowboy extends Entity {
	public InstanceCowboy(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);

		MyFile colladaFile = new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.dae");

		ModelAnimated entityData = ColladaLoader.loadColladaModel(colladaFile);

		Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.png")).create();

		Animation animation = AnimationCreator.loadAnimation(colladaFile);

		new ComponentCollision(this);
		new ComponentCollider(this, true);
		ComponentAnimation componentAnimation = new ComponentAnimation(this, entityData, 1.0f, texture, 1);
		componentAnimation.doAnimation(animation);
	}
}
