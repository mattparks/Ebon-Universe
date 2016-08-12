package game.entities.objects;

import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;
import game.entities.*;
import game.entities.components.*;

public class EntityCrate {
	public static Entity createEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		return new EntityCrateEntity(structure, position, rotation);
	}

	public static class EntityCrateEntity extends Entity {
		public EntityCrateEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
			super(structure, position, rotation);
			Model model = Model.newModel(new MyFile(MyFile.RES_FOLDER, "entities", "crate.obj")).create();
			Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "crate.png")).create();
			Texture normalTexture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "crateNormal.png")).create();
			
		//	model.setShineDamper(10.0f);
		//	model.setReflectivity(0.5f);

			new ColliderComponent(this);
			new CollisionComponent(this);
			new FadeRemove(this, 5.0);
			new ModelComponent(this, model, texture, normalTexture, 0.025f, 0);
		}
	}
}
