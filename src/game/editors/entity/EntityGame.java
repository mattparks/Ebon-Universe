package game.editors.entity;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.helpers.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.textures.*;
import game.*;
import game.entities.*;
import game.entities.components.*;
import game.entities.loading.*;
import org.lwjgl.glfw.*;

public class EntityGame extends IGame {
	public static boolean ENTITY_ROTATE = true;

	private Vector3f entityMove;
	private Vector3f entityRotate;

	public static Entity focusEntity;

	public EntityGame() {
		entityMove = new Vector3f();
		entityRotate = new Vector3f();
	}

	@Override
	public void init() {
		FlounderEngine.getProfiler().toggle(false);
		FlounderEngine.getCursor().show(true);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);

		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f, false), 0.001f, 2.0f, 0.0f, 50.0f), null);

	//	focusEntity = EntityLoader.load("crate").createEntity(Environment.getEntitys(), new Vector3f(), new Vector3f()); // 0, -5, 0

		focusEntity = new Entity(Environment.getEntitys(), new Vector3f(0, -3, 0), new Vector3f());
		Model model = Model.newModel(new MyFile(MyFile.RES_FOLDER, "entities", "spaceShip.obj")).create();
		Texture texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "crate.png")).create();
		Texture normalTexture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "entities", "crateNormal.png")).create();
		new ColliderComponent(focusEntity);
		new CollisionComponent(focusEntity);
		new FadeRemove(focusEntity, 1.0);
		new ModelComponent(focusEntity, model, texture, normalTexture, 1.7f, 0);

	//	EntityFrame frame = new EntityFrame();
	//	OpenGlUtils.goWireframe(true);
	}

	@Override
	public void update() {
		if (ENTITY_ROTATE || FlounderEngine.getDevices().getKeyboard().getKey(GLFW.GLFW_KEY_S)) {
			entityRotate.y = 20.0f * FlounderEngine.getDelta();
			focusEntity.move(entityMove, entityRotate);
		}

		update(focusPosition, focusRotation, false, 0.0f);
		Environment.update();
	}

	@Override
	public void dispose() {
		EntitySaver.save(focusEntity, "spaceShip");
	}
}
