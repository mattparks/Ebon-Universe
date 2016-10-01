package editor.entities;

import ebon.entities.*;
import ebon.entities.components.*;
import editor.entities.components.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.particles.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;

import javax.swing.*;
import java.lang.reflect.*;

public class EditorEntities extends FlounderEntrance {
	public static EditorEntities instance;

	public static StructureBasic<Entity> entityStructure;

	public static boolean ENTITY_ROTATE = true;
	public static boolean POLYGON_MODE = false;
	public static String ENTITY_NAME = "unnamed";
	public static String LOAD_FROM_ENTITY;

	public static MyFile PATH_MODEL = null;
	public static MyFile PATH_TEXTURE = null;
	public static MyFile PATH_NORMALMAP = null;

	public Vector3f entityMove;
	public Vector3f entityRotate;

	public static Entity focusEntity;

	public EntitiesFrame frame;

	public EditorEntities(ICamera camera, IRendererMaster renderer, IManagerGUI managerGUI) {
		super(
				camera, renderer, managerGUI,
				FlounderDisplay.class, FlounderMouse.class, FlounderFonts.class, FlounderGuis.class, FlounderParticles.class, EbonEntities.class
		);
		FlounderDisplay.setup(1080, 720, "Ebon Entities Editor", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "flounder.png")}, true, true, 8, false);
		instance = this;
	}

	@Override
	public void init() {
		FlounderMouse.setCursorHidden(false);

		entityStructure = new StructureBasic<>();

		frame = new EntitiesFrame();
		frame.create();

		generateBlankEntity();

		entityMove = new Vector3f();
		entityRotate = new Vector3f();
	}

	public static void generateBlankEntity() {
		if (focusEntity != null) {
			focusEntity.forceRemove();
		}

		focusEntity = new Entity(entityStructure, new Vector3f(0, -3, 0), new Vector3f());
	}

	@Override
	public void update() {
		if (LOAD_FROM_ENTITY != null) {
			if (focusEntity != null) {
				focusEntity.forceRemove();
			}

			focusEntity = EbonEntities.load(LOAD_FROM_ENTITY).createEntity(entityStructure, new Vector3f(0, -3, 0), new Vector3f());

			ComponentModel modelComponent = (ComponentModel) focusEntity.getComponent(ComponentModel.ID);

			if (modelComponent != null) {
				PATH_MODEL = modelComponent.getModel() != null ? new MyFile(modelComponent.getModel().getFile()) : null;
				PATH_TEXTURE = modelComponent.getTexture() != null ? modelComponent.getTexture().getFile() : null;
				PATH_NORMALMAP = modelComponent.getNormalMap() != null ? modelComponent.getNormalMap().getFile() : null;
			} else {
				PATH_MODEL = null;
				PATH_TEXTURE = null;
				PATH_NORMALMAP = null;
			}

			for (IEntityComponent component : focusEntity.getComponents()) {
				IEditorComponent editorComponent = null;

				for (int i = 0; i < frame.components.length; i++) {
					if (frame.components[i].getComponent().getId() == component.getId()) {
						try {
							FlounderLogger.log("Adding component: " + component);
							Class componentClass = Class.forName(frame.components[i].getClass().getName());
							Class[] componentTypes = new Class[]{IEntityComponent.class};
							@SuppressWarnings("unchecked") Constructor componentConstructor = componentClass.getConstructor(componentTypes);
							Object[] componentParameters = new Object[]{component};
							editorComponent = (IEditorComponent) componentConstructor.newInstance(componentParameters);
						} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
							FlounderLogger.error("While loading component" + frame.components[i] + "'s constructor could not be found!");
							FlounderLogger.exception(ex);
						}

						break;
					}
				}

				if (editorComponent != null) {
					JPanel panel = frame.makeTextPanel();
					editorComponent.addToPanel(panel);
					frame.componentAddRemove(panel, editorComponent);
					frame.addSideTab(editorComponent.getTabName(), panel);
				}
			}

			frame.nameField.setText(LOAD_FROM_ENTITY);

			frame.addSidePane();
			LOAD_FROM_ENTITY = null;
		}

		if (ENTITY_ROTATE) {
			entityRotate.y = 20.0f * FlounderEngine.getDelta();
			focusEntity.move(entityMove, entityRotate);
		}

		ComponentModel modelComponent = (ComponentModel) focusEntity.getComponent(ComponentModel.ID);

		if (modelComponent != null) {
			if (PATH_MODEL != null && (modelComponent.getModel() == null || !modelComponent.getModel().getFile().equals(PATH_MODEL.getPath()))) {
				if (PATH_MODEL.getPath().contains(".obj")) {
					Model model = Model.newModel(PATH_MODEL).create();
					modelComponent.setModel(model);
				}
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

		update(focusPosition, focusRotation);
		OpenGlUtils.goWireframe(POLYGON_MODE);
	}

	@Override
	public void profile() {
	}

	@Override
	public void dispose() {
		instance = null;
	}
}
