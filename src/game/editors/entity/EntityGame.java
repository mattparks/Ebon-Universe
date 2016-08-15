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

public class EntityGame extends IGame {
	public static boolean ENTITY_ROTATE = true;
	public static boolean POLYGON_MODE = false;
	public static String ENTITY_NAME = "unnamed";

	public static MyFile PATH_MODEL = null;
	public static MyFile PATH_TEXTURE = null;
	public static MyFile PATH_NORMALMAP = null;

	public static EntityGame instance;

	private Vector3f entityMove;
	private Vector3f entityRotate;

	public static Entity focusEntity;

	public EntityGame() {
		entityMove = new Vector3f();
		entityRotate = new Vector3f();
		instance = this;
	}

	@Override
	public void init() {
		FlounderEngine.getProfiler().toggle(false);
		FlounderEngine.getCursor().show(true);
		FlounderEngine.getDevices().getDisplay().setCursorHidden(true);

		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f, false), 0.001f, 2.0f, 0.0f, 50.0f), null);

		generateBlankEntity();

		EntityFrame frame = new EntityFrame();
		frame.create();
	}

	public static void generateBlankEntity() {
		if (focusEntity != null) {
			focusEntity.forceRemove();
		}

		focusEntity = new Entity(Environment.getEntitys(), new Vector3f(0, -3, 0), new Vector3f());
	}

	@Override
	public void update() {
		if (ENTITY_ROTATE) {
			entityRotate.y = 20.0f * FlounderEngine.getDelta();
			focusEntity.move(entityMove, entityRotate);
		}

		ModelComponent modelComponent = (ModelComponent) focusEntity.getComponent(ModelComponent.ID);

		if (modelComponent != null) {
			if (PATH_MODEL != null && (modelComponent.getModel() == null || !modelComponent.getModel().getFile().equals(PATH_MODEL.getPath()))) {
				Model model = Model.newModel(PATH_MODEL).create();
				modelComponent.setModel(model);
			}

			if (PATH_TEXTURE != null && (modelComponent.getTexture() == null || !modelComponent.getTexture().getFile().getPath().equals(PATH_TEXTURE.getPath()))) {
				Texture texture = Texture.newTexture(PATH_TEXTURE).create();
				modelComponent.setTexture(texture);
			}

			if (PATH_NORMALMAP != null && (modelComponent.getNormalMap() == null || !modelComponent.getNormalMap().getFile().getPath().equals(PATH_NORMALMAP.getPath()))) {
				Texture normalTexture = Texture.newTexture(PATH_NORMALMAP).create();
				modelComponent.setNormalMap(normalTexture);
			}
		}

		update(focusPosition, focusRotation, false, 0.0f);
		OpenGlUtils.goWireframe(POLYGON_MODE);
		Environment.update();
	}

	@Override
	public void dispose() {
	}
}
