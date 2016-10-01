package editor.entities.components;

import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.helpers.*;

import javax.swing.*;

public abstract class IEditorComponent {
	public abstract String getTabName();

	public abstract IEntityComponent getComponent();

	public abstract void addToPanel(JPanel panel);

	/**
	 * @return Returns a list of values that are saved with the component.
	 */
	public abstract Pair<String[], EntitySaverFunction[]> getSavableValues();
}
