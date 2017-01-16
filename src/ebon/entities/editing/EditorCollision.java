package ebon.entities.editing;

import ebon.entities.components.*;
import flounder.entities.*;
import flounder.entities.components.*;
import flounder.entities.template.*;
import flounder.helpers.*;

import javax.swing.*;

public class EditorCollision extends IComponentEditor {
	public ComponentCollision component;

	public EditorCollision(Entity entity) {
		this.component = new ComponentCollision(entity);
	}

	public EditorCollision(IComponentEntity component) {
		this.component = (ComponentCollision) component;
	}

	@Override
	public String getTabName() {
		return "Collision";
	}

	@Override
	public ComponentCollision getComponent() {
		return component;
	}

	@Override
	public void addToPanel(JPanel panel) {
	}

	@Override
	public void update() {
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues(String entityName) {
		return new Pair<>(new String[]{}, new EntitySaverFunction[]{});
	}
}
