package editors.entities;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.editing.*;
import ebon.particles.*;
import ebon.world.*;
import editors.editor.*;
import flounder.camera.*;
import flounder.devices.*;
import flounder.framework.*;
import flounder.helpers.*;
import flounder.lights.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;

import javax.swing.*;
import java.lang.reflect.*;

public class ExtensionEntities extends IEditorType {
	private static boolean ACTIVE = false;

	public boolean entityRotate;
	public boolean polygonMode;
	public String entityName;
	public String loadFromEntity;

	public Entity focusEntity;

	public ExtensionEntities() {
		super(FlounderMouse.class, EbonParticles.class, EbonWorld.class);
		ACTIVE = true;
	}

	@Override
	public void init() {
		// Sets the engine up for the editor.
		// FlounderProfiler.toggle(true);
		FlounderMouse.setCursorHidden(false);
		OpenGlUtils.goWireframe(false);
		FlounderBounding.toggle(true);

		// Sets the world to constant fog and a sun.
		EbonWorld.addFog(new Fog(new Colour(1.0f, 1.0f, 1.0f), 0.003f, 2.0f, 0.0f, 50.0f));
		EbonWorld.addSun(new Light(new Colour(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 2000.0f, 2000.0f)));

		// Default editor values.
		entityRotate = false;
		polygonMode = false;
		entityName = "unnamed";
		loadDefaultEntity();
	}

	public void loadDefaultEntity() {
		if (focusEntity != null) {
			focusEntity.forceRemove();
		}

		focusEntity = new Entity(EbonEntities.getEntities(), new Vector3f(), new Vector3f());
		//focusEntity = EbonEntities.load("dragon").createEntity(EbonEntities.getEntities(), new Vector3f(), new Vector3f());
	}

	@Override
	public void update() {
		// Updates wireframe modes.
		OpenGlUtils.goWireframe(polygonMode);

		// Used to load a entity from a .entity file.
		if (loadFromEntity != null) {
			if (focusEntity != null) {
				focusEntity.forceRemove();
			}

			focusEntity = EbonEntities.load(loadFromEntity).createEntity(EbonEntities.getEntities(), new Vector3f(), new Vector3f());

			for (IEntityComponent component : focusEntity.getComponents()) {
				IEditorComponent editorComponent = null;

				for (int i = 0; i < IEditorComponent.EDITOR_COMPONENTS.length; i++) {
					if (IEditorComponent.EDITOR_COMPONENTS[i].getComponent().getId() == component.getId()) {
						try {
							FlounderLogger.log("Adding component: " + component);
							Class componentClass = Class.forName(IEditorComponent.EDITOR_COMPONENTS[i].getClass().getName());
							Class[] componentTypes = new Class[]{IEntityComponent.class};
							@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
							Object[] componentParameters = new Object[]{component};
							editorComponent = (IEditorComponent) componentConstructor.newInstance(componentParameters);
						} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
							FlounderLogger.error("While loading component" + IEditorComponent.EDITOR_COMPONENTS[i] + "'s constructor could not be found!");
							FlounderLogger.exception(ex);
						}

						break;
					}
				}

				FrameEntities.editorComponents.add(editorComponent);

				if (editorComponent != null) {
					JPanel panel = IEditorComponent.makeTextPanel();
					editorComponent.addToPanel(panel);
					FrameEntities.componentAddRemove(panel, editorComponent);
					FrameEntities.addSideTab(editorComponent.getTabName(), panel);
				}
			}

			FrameEntities.nameField.setText(loadFromEntity);
			//	FrameEntities.addSidePane();
			loadFromEntity = null;
		}

		// Updates the model to be focused, and rotated.
		if (focusEntity != null) {
			ComponentModel componentModel = (ComponentModel) focusEntity.getComponent(ComponentModel.ID);

			if (componentModel != null && componentModel.getModel() != null && componentModel.getModel().getMeshData() != null && componentModel.getModel().getMeshData().getAABB() != null) {
				double height = componentModel.getModel().getMeshData().getAABB().getCentreY() * componentModel.getScale();

				if (entityRotate) {
					FlounderCamera.getPlayer().getRotation().y = 20.0f * FlounderFramework.getDelta();
				}

				focusEntity.move(componentModel.getEntity().getPosition().set(0.0f, (float) height / -2.0f, 0.0f), FlounderCamera.getPlayer().getRotation());
			}
		}
	}

	@Override
	public void profile() {
	}

	@Override
	public void dispose() {
		ACTIVE = false;
	}

	@Override
	public boolean isActive() {
		return ACTIVE;
	}
}
