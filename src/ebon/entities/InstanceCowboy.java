package ebon.entities;

import ebon.entities.components.*;
import flounder.animation.*;
import flounder.collada.*;
import flounder.collada.animation.*;
import flounder.entities.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;

public class InstanceCowboy extends Entity {
	public InstanceCowboy(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);

		MyFile colladaFile = new MyFile(FlounderEntities.ENTITIES_FOLDER, "cowboy", "cowboy.dae");

		ModelAnimated modelAnimated = FlounderCollada.loadCollada(colladaFile);

		AnimationData animationData = FlounderCollada.loadAnimation(colladaFile);
		Animation animation = FlounderAnimation.loadAnimation(animationData);

	//	Texture texture = Texture.newTexture(new MyFile(FlounderEntities.ENTITIES_FOLDER, "cowboy", "cowboy.png")).create();

	//	new ComponentCollision(this);
	//	new ComponentCollider(this);
		ComponentAnimation componentAnimation = new ComponentAnimation(this, modelAnimated, 1.0f, null, 1);
		componentAnimation.doAnimation(animation);
	}
}
