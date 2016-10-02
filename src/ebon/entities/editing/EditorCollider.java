package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.helpers.*;
import flounder.physics.bounding.*;

import javax.swing.*;
import java.awt.event.*;

public class EditorCollider extends IEditorComponent {
	public ComponentCollider component;

	public EditorCollider(Entity entity) {
		this.component = new ComponentCollider(entity, true);
	}

	public EditorCollider(IEntityComponent component) {
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
		renderAABB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				component.setRenderAABB(renderAABB.isSelected());
			}
		});
		panel.add(renderAABB);
	}

	@Override
	public void update() {
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		String renderAABBSave = "RenderAABB: " + component.renderAABB();
		return new Pair<>(new String[]{renderAABBSave}, new EntitySaverFunction[]{});
	}
}
