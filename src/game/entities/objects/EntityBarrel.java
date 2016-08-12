package game.entities.objects;

import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;
import game.entities.*;
import game.entities.components.*;
import game.entities.loading.*;

public class EntityBarrel {
	public static EntityTemplate TEMPLATE = null;
	public static boolean saved = false;

	public static Entity createEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		if (TEMPLATE == null) {
			if ((TEMPLATE = EntityLoader.load("barrel")) == null) {
				Entity entity = new EntityBarrelEntity(structure, position, rotation);

				if (!saved) {
					EntitySaver.save(entity, "barrel");
					saved = true;
				}

				return entity;
			}
		}
		return TEMPLATE.createEntity(structure, position, rotation);
	}

	public static class EntityBarrelEntity extends Entity {
		public EntityBarrelEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
			super(structure, position, rotation);
			Model model = Model.newModel(new MyFile(MyFile.RES_FOLDER, "entities", "barrel.obj")).create();
			Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "barrel.png")).create();
			Texture normalTexture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "barrelNormal.png")).create();

			//	model.setShineDamper(10.0f);
			//	model.setReflectivity(0.5f);

			new ColliderComponent(this);
			new CollisionComponent(this);
			new FadeRemove(this, 2.0);
			new ModelComponent(this, model, texture, normalTexture, 0.8f, 0);
		}
	}
}
