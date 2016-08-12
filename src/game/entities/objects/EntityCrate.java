package game.entities.objects;

import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;
import game.entities.*;
import game.entities.components.*;
import game.entities.loading.*;

public class EntityCrate {
	public static EntityTemplate TEMPLATE = null;
	public static boolean saved = false;

	public static Entity createEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		if (TEMPLATE == null) {
			if ((TEMPLATE = EntityLoader.load("crate")) == null) {
				Entity entity = new EntityCrateEntity(structure, position, rotation);

				if (!saved) {
					EntitySaver.save(entity, "crate");
					saved = true;
				}

				return entity;
			}
		}
		return TEMPLATE.createEntity(structure, position, rotation);
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
