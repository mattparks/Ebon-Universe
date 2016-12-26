package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.animation.*;
import flounder.collada.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.resources.*;
import flounder.textures.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;

public class EditorAnimation extends IEditorComponent {
	public ComponentAnimation component;

	private String overrideTextureName;

	private MyFile pathCollada;
	private MyFile pathTexture;

	public EditorAnimation(Entity entity) {
		this.component = new ComponentAnimation(entity, null, 1.0f, null, 1);
	}

	public EditorAnimation(IEntityComponent component) {
		this.component = (ComponentAnimation) component;
	}

	@Override
	public String getTabName() {
		return "Animation";
	}

	@Override
	public IEntityComponent getComponent() {
		return component;
	}

	@Override
	public void addToPanel(JPanel panel) {
		// Load Collada.
		JButton loadModel = new JButton("Select Collada");
		loadModel.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

				if (selectedFile.contains("res/entities")) {
					String[] filepath = selectedFile.split("/");
					EditorAnimation.this.pathCollada = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1].replace(".dae", ""), filepath[filepath.length - 1]);
				} else {
					FlounderLogger.error("The selected collada path is not inside the res/entities folder!");
				}
			}
		});
		panel.add(loadModel);

		// Load Texture.
		JButton loadTexture = new JButton("Select Texture");
		loadTexture.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

				if (selectedFile.contains("res/entities")) {
					String[] filepath = selectedFile.split("/");
					EditorAnimation.this.pathTexture = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1].replace(".png", ""), filepath[filepath.length - 1]);
				} else {
					FlounderLogger.error("The selected texture path is not inside the res/entities folder!");
				}
			}
		});
		panel.add(loadTexture);

		// Scale Slider.
		//	panel.add(new JLabel("Scale Slider: "));
		JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, (int) (component.getScale() * 25.0f));
		scaleSlider.setToolTipText("Model Scale");
		scaleSlider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider) e.getSource();
			int reading = source.getValue();
			component.setScale(reading / 25.0f);
		});
		scaleSlider.setMajorTickSpacing(25);
		scaleSlider.setMinorTickSpacing(10);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		panel.add(scaleSlider);
	}

	@Override
	public void update() {
		if (component != null) {
			if (pathCollada != null && (component.getModelAnimated() == null/* || !component.getModelAnimated().getFile().equals(pathCollada.getPath())*/)) {
				if (pathCollada.getPath().contains(".dae")) {
					MyFile colladaFile = new MyFile(MyFile.RES_FOLDER, "entities", "cowboy", "cowboy.dae");
					ModelAnimated modelAnimated = ColladaLoader.loadColladaModel(colladaFile);
					Animation animation = AnimationCreator.loadAnimation(colladaFile);
					component.setModelAnimated(modelAnimated);
					component.doAnimation(animation);
				}
			}

			if (pathTexture != null && (component.getTexture() == null || !component.getTexture().getFile().getPath().equals(pathTexture.getPath()))) {
				Texture texture = Texture.newTexture(pathTexture).create();
				component.setTexture(texture);
			}
		}
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		if (component.getTexture() != null) {
			try {
				InputStream input = component.getTexture().getFile().getInputStream();
				OutputStream output = new FileOutputStream(new File("entities/" + component.getTexture().getFile().getName()));
				byte[] buf = new byte[1024];
				int bytesRead;

				while ((bytesRead = input.read(buf)) > 0) {
					output.write(buf, 0, bytesRead);
				}

				input.close();
				output.close();
			} catch (IOException e) {
				FlounderLogger.exception(e);
			}
		}

		EntitySaverFunction saveVertices = new EntitySaverFunction("Vertices") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (float v : component.getModelAnimated().getMeshData().getVertices()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveTextureCoords = new EntitySaverFunction("TextureCoords") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (float v : component.getModelAnimated().getMeshData().getTextures()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveNormals = new EntitySaverFunction("Normals") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (float v : component.getModelAnimated().getMeshData().getNormals()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveTangents = new EntitySaverFunction("Tangents") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (float v : component.getModelAnimated().getMeshData().getTangents()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveIndices = new EntitySaverFunction("Indices") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (int i : component.getModelAnimated().getMeshData().getIndices()) {
						String s = i + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveJointIds = new EntitySaverFunction("JointIds") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (int i : component.getModelAnimated().getMeshData().getJointIds()) {
						String s = i + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveVertexWeights = new EntitySaverFunction("VertexWeights") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null) {
					for (float v : component.getModelAnimated().getMeshData().getVertexWeights()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		// TODO: Save AABB and Hull.

		String saveScale = "Scale: " + component.getScale();
		String saveFurthestPoint = "FurthestPoint: " + component.getModelAnimated().getMeshData().getFurthestPoint();

		String saveTexture;
		String saveTextureNumRows;

		if (overrideTextureName != null) {
			MyFile tempFile = new MyFile(MyFile.RES_FOLDER, "entities", overrideTextureName.replace(".png", ""), overrideTextureName);
			saveTexture = "Texture: " + tempFile.getPath().substring(1, tempFile.getPath().length());
			saveTextureNumRows = "TextureNumRows: " + 1;
		} else {
			saveTexture = "Texture: " + (component.getTexture() == null ? null : component.getTexture().getFile().getPath().substring(1, component.getTexture().getFile().getPath().length()));
			saveTextureNumRows = "TextureNumRows: " + (component.getTexture() == null ? 1 : component.getTexture().getNumberOfRows());
		}

		return new Pair<>(
				new String[]{saveScale, saveFurthestPoint, saveTexture, saveTextureNumRows},
				new EntitySaverFunction[]{saveVertices, saveTextureCoords, saveNormals, saveTangents, saveIndices, saveJointIds, saveVertexWeights}
		);
	}
}
