package ebon.entities.editing;

import ebon.entities.components.*;
import flounder.entities.*;
import flounder.entities.components.*;
import flounder.entities.template.*;
import flounder.helpers.*;
import flounder.physics.bounding.*;

import javax.swing.*;
import java.awt.event.*;

public class EditorCollider extends IComponentEditor {
	public ComponentCollider component;

	public EditorCollider(Entity entity) {
		this.component = new ComponentCollider(entity);
	}

	public EditorCollider(IComponentEntity component) {
		this.component = (ComponentCollider) component;
	}

	@Override
	public String getTabName() {
		return "Collider";
	}

	@Override
	public ComponentCollider getComponent() {
		return component;
	}

	@Override
	public void addToPanel(JPanel panel) {
		JCheckBox renderAABB = new JCheckBox("Render AABB");
		renderAABB.setSelected(FlounderBounding.renders());
		renderAABB.addItemListener((ItemEvent e) -> {
			component.setRenderAABB(renderAABB.isSelected());
		});
		panel.add(renderAABB);
	}

	@Override
	public void update() {
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues(String entityName) {
		return new Pair<>(new String[]{}, new EntitySaverFunction[]{});
	}
}
