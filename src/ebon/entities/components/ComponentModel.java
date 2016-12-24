package ebon.entities.components;

import ebon.entities.*;
import ebon.entities.loading.*;
import flounder.materials.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.textures.*;

/**
 * Creates a model with a texture that can be rendered into the world.
 */
public class ComponentModel extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private Model model;
	private Texture texture;
	private Texture normalMap;
	private float transparency;
	private float scale;
	private int textureIndex;

	/**
	 * Creates a new ComponentModel.
	 *
	 * @param entity The entity this component is attached to.
	 * @param model The model that will be attached to this entity.
	 * @param scale The scale of the entity.
	 * @param textureIndex What texture index this entity should renderObjects from (0 default).
	 */
	public ComponentModel(Entity entity, Model model, Texture texture, Texture normalMap, float scale, int textureIndex) {
		super(entity, ID);
		this.model = model;
		this.texture = texture;
		this.normalMap = normalMap;
		this.transparency = 1.0f;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}

	/**
	 * Creates a new ComponentModel. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ComponentModel(Entity entity, EntityTemplate template) {
		super(entity, ID);

		this.model = Model.newModel(new ModelBuilder.LoadManual() {
			@Override
			public String getModelName() {
				return template.getEntityName();
			}

			@Override
			public float[] getVertices() {
				return EntityTemplate.toFloatArray(template.getSectionData(ComponentModel.this, "Vertices"));
			}

			@Override
			public float[] getTextureCoords() {
				return EntityTemplate.toFloatArray(template.getSectionData(ComponentModel.this, "TextureCoords"));
			}

			@Override
			public float[] getNormals() {
				return EntityTemplate.toFloatArray(template.getSectionData(ComponentModel.this, "Normals"));
			}

			@Override
			public float[] getTangents() {
				return EntityTemplate.toFloatArray(template.getSectionData(ComponentModel.this, "Tangents"));
			}

			@Override
			public int[] getIndices() {
				return EntityTemplate.toIntArray(template.getSectionData(ComponentModel.this, "Indices"));
			}

			@Override
			public Material[] getMaterials() {
				return new Material[]{}; // TODO: Save and load materials!
			}
		}).create();

		if (!template.getValue(this, "Texture").equals("null")) {
			this.texture = Texture.newTexture(new MyFile(template.getValue(this, "Texture"))).create();
			this.texture.setNumberOfRows(Integer.parseInt(template.getValue(this, "TextureNumRows")));
		}

		if (!template.getValue(this, "NormalMap").equals("null")) {
			this.normalMap = Texture.newTexture(new MyFile(template.getValue(this, "NormalMap"))).create();
			this.normalMap.setNumberOfRows(Integer.parseInt(template.getValue(this, "NormalMapNumRows")));
		}

		this.transparency = 1.0f;
		this.scale = Float.parseFloat(template.getValue(this, "Scale"));
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

	public void setModel(Model model) {
		this.model = model;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Texture getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
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
	public void update() {
	}

	@Override
	public void dispose() {
	}
}
