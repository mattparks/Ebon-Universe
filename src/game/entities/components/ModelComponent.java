package game.entities.components;

import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.textures.*;
import game.entities.*;
import game.entities.loading.*;

import java.io.*;

/**
 * Creates a model with a texture that can be rendered into the world.
 */
public class ModelComponent extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private Model model;
	private Texture texture;
	private Texture normalMap;
	private float transparency;
	private float scale;
	private int textureIndex;

	/**
	 * Creates a new ModelComponent.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The model that will be attached too this entity.
	 * @param scale The scale of the entity.
	 */
	public ModelComponent(Entity entity, Model model, Texture texture, float scale) {
		this(entity, model, texture, null, 1.0f, 0);
	}

	/**
	 * Creates a new ModelComponent.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The model that will be attached too this entity.
	 * @param scale The scale of the entity.
	 * @param textureIndex What texture index this entity should renderObjects from (0 default).
	 */
	public ModelComponent(Entity entity, Model model, Texture texture, Texture normalMap, float scale, int textureIndex) {
		super(entity, ID);
		this.model = model;
		this.texture = texture;
		this.normalMap = normalMap;
		this.transparency = 1.0f;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}

	/**
	 * Creates a new ModelComponent. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ModelComponent(Entity entity, EntityTemplate template) {
		this(entity, null, null, null, 1.0f, 0);

		float[] vertices = template.toFloatArray(template.toOneLine(template.getSectionData(getClass().getName(), "Vertices")));
		float[] textureCoords = template.toFloatArray(template.toOneLine(template.getSectionData(getClass().getName(), "TextureCoords")));
		float[] normals = template.toFloatArray(template.toOneLine(template.getSectionData(getClass().getName(), "Normals")));
		float[] tangents = template.toFloatArray(template.toOneLine(template.getSectionData(getClass().getName(), "Tangents")));
		int[] indices = template.toIntArray(template.toOneLine(template.getSectionData(getClass().getName(), "Indices")));
		this.model = new Model(vertices, textureCoords, normals, tangents, indices);

		this.texture = Texture.newTexture(new MyFile(template.getValue(getClass().getName(), "Texture"))).create();
		this.texture.setNumberOfRows(Integer.parseInt(template.getValue(getClass().getName(), "TextureNumRows")));

		if (Boolean.parseBoolean(template.getValue(getClass().getName(), "UseNormalMap"))) {
			this.normalMap = Texture.newTexture(new MyFile(template.getValue(getClass().getName(), "NormalMap"))).create();
			this.normalMap.setNumberOfRows(Integer.parseInt(template.getValue(getClass().getName(), "NormalMapNumRows")));
		}

		this.transparency = Float.parseFloat(template.getValue(getClass().getName(), "Transparency"));
		this.scale = Float.parseFloat(template.getValue(getClass().getName(), "Scale"));
		this.textureIndex = Integer.parseInt(template.getValue(getClass().getName(), "TextureIndex"));
	}

	/**
	 * Gets the textures coordinate offset that is used in rendering the model.
	 *
	 * @return The coordinate offset used in rendering.
	 */
	public Vector2f getTextureOffset() {
		int column = textureIndex % texture.getNumberOfRows();
		int row = textureIndex / texture.getNumberOfRows();
		return new Vector2f((float) row / (float) texture.getNumberOfRows(), (float) column / (float) texture.getNumberOfRows());
	}

	public Model getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

	public Texture getNormalMap() {
		return normalMap;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setTextureIndex(int index) {
		this.textureIndex = index;
	}

	@Override
	public Pair<String[], SaveFunction[]> getSavableValues() {
		SaveFunction saveVertices = new SaveFunction("Vertices") {
			@Override
			public void writeIntoSection(EntityFileWriter entityFileWriter) throws IOException {
				for (Vector3f v : entityFileWriter.getVec3List(model.getVertices())) {
					String s = entityFileWriter.vec3String(v) + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		SaveFunction saveTextureCoords = new SaveFunction("TextureCoords") {
			@Override
			public void writeIntoSection(EntityFileWriter entityFileWriter) throws IOException {
				for (Vector2f v : entityFileWriter.getVec2List(model.getTextures())) {
					String s = entityFileWriter.vec2String(v) + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		SaveFunction saveNormals = new SaveFunction("Normals") {
			@Override
			public void writeIntoSection(EntityFileWriter entityFileWriter) throws IOException {
				for (Vector3f v : entityFileWriter.getVec3List(model.getNormals())) {
					String s = entityFileWriter.vec3String(v) + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		SaveFunction saveTangents = new SaveFunction("Tangents") {
			@Override
			public void writeIntoSection(EntityFileWriter entityFileWriter) throws IOException {
				for (Vector3f v : entityFileWriter.getVec3List(model.getTangents())) {
					String s = entityFileWriter.vec3String(v) + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		SaveFunction saveIndices = new SaveFunction("Indices") {
			@Override
			public void writeIntoSection(EntityFileWriter entityFileWriter) throws IOException {
				for (int i : model.getIndices()) {
					String s = i + "/";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};

		String textureSave = "Texture: " + texture.getFile().getPath().substring(1, texture.getFile().getPath().length());
		String textureNumRowsSave = "TextureNumRows: " + texture.getNumberOfRows();

		String useNormalMapSave = "UseNormalMap: " + (normalMap == null ? "false" : "true");
		String normalMapSave = "NormalMap: " + (normalMap == null ? null : normalMap.getFile().getPath().substring(1, normalMap.getFile().getPath().length()));
		String normalMapNumRowsSave = "NormalMapNumRows: " + (normalMap == null ? 1 : normalMap.getNumberOfRows());

		String transparencySave = "Transparency: " + transparency;
		String scaleSave = "Scale: " + scale;
		String indexSave = "TextureIndex: " + textureIndex;


		return new Pair<>(
				new String[]{textureSave, textureNumRowsSave, useNormalMapSave, normalMapSave, normalMapNumRowsSave, transparencySave, scaleSave, indexSave},
				new SaveFunction[]{saveVertices, saveTextureCoords, saveNormals, saveTangents, saveIndices}
		);
	}

	@Override
	public void update() {
	}
}
