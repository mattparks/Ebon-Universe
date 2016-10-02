package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.helpers.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class IEditorComponent {
	public static final IEditorComponent[] EDITOR_COMPONENTS = new IEditorComponent[]{
			new EditorCollider((Entity) null),
			new EditorCollision((Entity) null),
			new EditorRemoveFade((Entity) null),
			new EditorModel((Entity) null),
			new EditorParticleSystem((Entity) null)
	};
	public static final List<Pair<String, JPanel>> ADD_SIDE_TAB = new ArrayList<>();
	public static final List<String> REMOVE_SIDE_TAB = new ArrayList<>();

	public abstract String getTabName();

	public abstract IEntityComponent getComponent();

	public abstract void addToPanel(JPanel panel);

	public abstract void update();

	/**
	 * @return Returns a list of values that are saved with the component.
	 */
	public abstract Pair<String[], EntitySaverFunction[]> getSavableValues();

	public static JPanel makeTextPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 1));
		return panel;
	}
}
