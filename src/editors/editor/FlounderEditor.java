package editors.editor;

import flounder.framework.*;
import flounder.logger.*;
import flounder.profiling.*;

public class FlounderEditor extends IModule {
	private static final FlounderEditor instance = new FlounderEditor();

	private IEditorType editorType;

	public FlounderEditor() {
		super(ModuleUpdate.UPDATE_POST, FlounderLogger.class, FlounderProfiler.class);
	}

	@Override
	public void init() {
		this.editorType = null;
	}

	@Override
	public void update() {
		// Gets a new editor, if available.
		IEditorType newManager = (IEditorType) getExtensionMatch(editorType, IEditorType.class, true);

		// If there is a editor, disable the old one and start to use the new one.
		if (newManager != null) {
			if (editorType != null) {
				editorType.dispose();
				editorType.setInitialized(false);
			}

			if (!newManager.isInitialized()) {
				newManager.init();
				newManager.setInitialized(true);
			}

			editorType = newManager;
		}

		// Runs updates for the editor.
		if (editorType != null) {
			editorType.update();
		}
	}

	@Override
	public void profile() {
		if (editorType != null) {
			editorType.profile();
		}

		FlounderProfiler.add("Editor", "Selected", editorType == null ? "NULL" : editorType.getClass());
	}

	/**
	 * Gets the current editor extension.
	 *
	 * @return The current editor.
	 */
	public static IEditorType getEditorType() {
		return instance.editorType;
	}

	@Override
	public IModule getInstance() {
		return instance;
	}

	@Override
	public void dispose() {
		// Disposes the editor with the module.
		if (editorType != null) {
			editorType.dispose();
			editorType.setInitialized(false);
		}
	}
}
