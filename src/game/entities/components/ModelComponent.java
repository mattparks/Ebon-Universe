package game.entities.components;

import flounder.engine.*;
import flounder.helpers.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.textures.*;
import game.editors.entity.*;
import game.entities.*;
import game.entities.loading.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
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
	 */
	public ModelComponent(Entity entity) {
		this(entity, null, null, null, 1.0f, 0);
	}

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
		super(entity, ID);

		this.model = Model.newModel(new ModelBuilder.LoadManual() {
			@Override
			public String getModelName() {
				return template.getEntityName();
			}

			@Override
			public float[] getVertices() {
				return EntityTemplate.toFloatArray(template.getSectionData(ModelComponent.this, "Vertices"));
			}

			@Override
			public float[] getTextureCoords() {
				return EntityTemplate.toFloatArray(template.getSectionData(ModelComponent.this, "TextureCoords"));
			}

			@Override
			public float[] getNormals() {
				return EntityTemplate.toFloatArray(template.getSectionData(ModelComponent.this, "Normals"));
			}

			@Override
			public float[] getTangents() {
				return EntityTemplate.toFloatArray(template.getSectionData(ModelComponent.this, "Tangents"));
			}

			@Override
			public int[] getIndices() {
				return EntityTemplate.toIntArray(template.getSectionData(ModelComponent.this, "Indices"));
			}
		}).create();

		this.texture = Texture.newTexture(new MyFile(template.getValue(this, "Texture"))).create();
		this.texture.setNumberOfRows(Integer.parseInt(template.getValue(this, "TextureNumRows")));

		if (!template.getValue(this, "NormalMap").equals("null")) {
			this.normalMap = Texture.newTexture(new MyFile(template.getValue(this, "NormalMap"))).create();
			this.normalMap.setNumberOfRows(Integer.parseInt(template.getValue(this, "NormalMapNumRows")));
		}

		this.scale = Float.parseFloat(template.getValue(this, "Scale"));
	}

	@Override
	public void addToPanel(JPanel panel) {
		// Load Texture.
		JButton loadModel = new JButton("Select Model");
		loadModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

					if (selectedFile.contains("res/entities")) {
						String[] filepath = selectedFile.split("/");
						EntityGame.PATH_MODEL = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1]);
					} else {
						FlounderEngine.getLogger().error("The selected texture path is not inside the res/entities folder!");
					}
				}
			}
		});
		panel.add(loadModel);

		// Load Texture.
		JButton loadTexture = new JButton("Select Texture");
		loadTexture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

					if (selectedFile.contains("res/entities")) {
						String[] filepath = selectedFile.split("/");
						EntityGame.PATH_TEXTURE = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1]);
					} else {
						FlounderEngine.getLogger().error("The selected texture path is not inside the res/entities folder!");
					}
				}
			}
		});
		panel.add(loadTexture);

		// Load Normal Map.
		JButton loadNormalMap = new JButton("Select Normal Map");
		loadNormalMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				File workingDirectory = new File(System.getProperty("user.dir"));
				fileChooser.setCurrentDirectory(workingDirectory);
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

					if (selectedFile.contains("res/entities")) {
						String[] filepath = selectedFile.split("/");
						EntityGame.PATH_NORMALMAP = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1]);
					} else {
						FlounderEngine.getLogger().error("The selected texture path is not inside the res/entities folder!");
					}
				}
			}
		});
		panel.add(loadNormalMap);

		// Scale Slider.
		JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
		scaleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				ModelComponent.this.scale = reading / 25.0f;
			}
		});
		//Turn on labels at major tick marks.
		scaleSlider.setMajorTickSpacing(10);
		scaleSlider.setMinorTickSpacing(2);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		panel.add(scaleSlider);
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
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		EntitySaverFunction saveVertices = new EntitySaverFunction("Vertices") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (float v : model.getVertices()) {
					String s = v + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		EntitySaverFunction saveTextureCoords = new EntitySaverFunction("TextureCoords") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (float v : model.getTextures()) {
					String s = v + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		EntitySaverFunction saveNormals = new EntitySaverFunction("Normals") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (float v : model.getNormals()) {
					String s = v + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		EntitySaverFunction saveTangents = new EntitySaverFunction("Tangents") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (float v : model.getTangents()) {
					String s = v + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};
		EntitySaverFunction saveIndices = new EntitySaverFunction("Indices") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				for (int i : model.getIndices()) {
					String s = i + ",";
					entityFileWriter.writeSegmentData(s);
				}
			}
		};

		String textureSave = "Texture: " + texture.getFile().getPath().substring(1, texture.getFile().getPath().length());
		String textureNumRowsSave = "TextureNumRows: " + texture.getNumberOfRows();

		String normalMapSave = "NormalMap: " + (normalMap == null ? null : normalMap.getFile().getPath().substring(1, normalMap.getFile().getPath().length()));
		String normalMapNumRowsSave = "NormalMapNumRows: " + (normalMap == null ? 1 : normalMap.getNumberOfRows());

		String scaleSave = "Scale: " + scale;


		return new Pair<>(
				new String[]{textureSave, textureNumRowsSave, normalMapSave, normalMapNumRowsSave, scaleSave},
				new EntitySaverFunction[]{saveVertices, saveTextureCoords, saveNormals, saveTangents, saveIndices}
		);
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}
