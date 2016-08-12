package game.editors.entity;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import game.*;
import game.entities.*;
import game.entities.objects.*;

public class EntityGame extends IGame {
	public static boolean ENTITY_ROTATE = true;

	private Vector3f entityMove;
	private Vector3f entityRotate;
	private Entity focusEntity;

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

		focusEntity = EntityBarrel.createEntity(Environment.getEntitys(), new Vector3f(), new Vector3f());

		EntityFrame frame = new EntityFrame();
	}

	@Override
	public void update() {
		if (ENTITY_ROTATE) {
			entityRotate.y = 20.0f * FlounderEngine.getDelta();
			focusEntity.move(entityMove, entityRotate);
		}

		update(focusPosition, focusRotation, false, 0.0f);
		Environment.update();
	}

	@Override
	public void dispose() {
	}
}
