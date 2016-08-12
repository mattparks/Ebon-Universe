package game.entities.objects;

import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;
import game.entities.*;
import game.entities.components.*;

public class EntitySphere {
	public static Entity createEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		return new EntitySphereEntity(structure, position, rotation);
	}

	public static class EntitySphereEntity extends Entity {
		public EntitySphereEntity(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
			super(structure, position, rotation);
			Model model = Model.newModel(new MyFile(MyFile.RES_FOLDER, "models", "sphere.obj")).create();
			Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "sphere.png")).create();
			
		//	model.setShineDamper(10.0f);
		//	model.setReflectivity(0.5f);

			new ColliderComponent(this);
			new CollisionComponent(this);
			new FadeRemove(this, 1.0);
			new ModelComponent(this, model, texture, 0.5f);
		}
	}
}
