package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.textures.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;

public class EditorModel extends IEditorComponent {
	public ComponentModel component;

	private String overrideTextureName;
	private String overrideNormalsName;

	private MyFile pathModel = null;
	private MyFile pathTexture = null;
	private MyFile pathNormalMap = null;

	public EditorModel(Entity entity) {
		this.component = new ComponentModel(entity, null, Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "undefined.png")).create(), null, 1.0f, 0);
	}

	public EditorModel(IEntityComponent component) {
		this.component = (ComponentModel) component;
	}

	@Override
	public String getTabName() {
		return "Model";
	}

	@Override
	public ComponentModel getComponent() {
		return component;
	}

	@Override
	public void addToPanel(JPanel panel) {
		// Load Texture.
		JButton loadModel = new JButton("Select Model");
		loadModel.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

				if (selectedFile.contains("res/entities")) {
					String[] filepath = selectedFile.split("/");
					EditorModel.this.pathModel = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1].replace(".obj", ""), filepath[filepath.length - 1]);
				} else {
					FlounderLogger.error("The selected texture path is not inside the res/entities folder!");
				}
			}
		});
		panel.add(loadModel);

		// Load Texture.
		JButton loadTexture = new JButton("Select Texture");
		loadModel.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

				if (selectedFile.contains("res/entities")) {
					String[] filepath = selectedFile.split("/");
					EditorModel.this.pathTexture = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1].replace(".png", ""), filepath[filepath.length - 1]);
				} else {
					FlounderLogger.error("The selected texture path is not inside the res/entities folder!");
				}
			}
		});
		panel.add(loadTexture);

		// Load Normal Map.
		JButton loadNormalMap = new JButton("Select Normal Map");
		loadNormalMap.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getPath().replace("\\", "/");

				if (selectedFile.contains("res/entities")) {
					String[] filepath = selectedFile.split("/");
					EditorModel.this.pathNormalMap = new MyFile(MyFile.RES_FOLDER, "entities", filepath[filepath.length - 1].replace("Normals.png", ""), filepath[filepath.length - 1]);
				} else {
					FlounderLogger.error("The selected texture path is not inside the res/entities folder!");
				}
			}
		});
		panel.add(loadNormalMap);

		// Scale Slider.
		//	panel.add(new JLabel("Scale Slider: "));
		JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 0, 150, (int) (component.getScale() * 25.0f));
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
			if (pathModel != null && (component.getModel() == null || !component.getModel().getFile().equals(pathModel.getPath()))) {
				if (pathModel.getPath().contains(".obj")) {
					Model model = Model.newModel(pathModel).create();
					component.setModel(model);
				}
			}

			if (pathTexture != null && (component.getTexture() == null || !component.getTexture().getFile().getPath().equals(pathTexture.getPath()))) {
				Texture texture = Texture.newTexture(pathTexture).create();
				component.setTexture(texture);
			}

			if (pathNormalMap != null && (component.getNormalMap() == null || !component.getNormalMap().getFile().getPath().equals(pathNormalMap.getPath()))) {
				Texture normalTexture = Texture.newTexture(pathNormalMap).create();
				component.setNormalMap(normalTexture);
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

		if (component.getNormalMap() != null) {
			try {
				InputStream input = component.getNormalMap().getFile().getInputStream();
				OutputStream output = new FileOutputStream(new File("entities/" + component.getNormalMap().getFile().getName()));
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
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (component.getModel() != null) {
					for (float v : component.getModel().getVertices()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveTextureCoords = new EntitySaverFunction("TextureCoords") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (component.getModel() != null) {
					for (float v : component.getModel().getTextures()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveNormals = new EntitySaverFunction("Normals") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (component.getModel() != null) {
					for (float v : component.getModel().getNormals()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveTangents = new EntitySaverFunction("Tangents") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (component.getModel() != null) {
					for (float v : component.getModel().getTangents()) {
						String s = v + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};
		EntitySaverFunction saveIndices = new EntitySaverFunction("Indices") {
			@Override
			public void writeIntoSection(FlounderFileWriter entityFileWriter) throws IOException {
				if (component.getModel() != null) {
					for (int i : component.getModel().getIndices()) {
						String s = i + ",";
						entityFileWriter.writeSegmentData(s);
					}
				}
			}
		};

		String textureSave;
		String textureNumRowsSave;

		if (overrideTextureName != null) {
			MyFile tempFile = new MyFile(MyFile.RES_FOLDER, "entities", overrideTextureName.replace(".png", ""), overrideTextureName);
			textureSave = "Texture: " + tempFile.getPath().substring(1, tempFile.getPath().length());
			textureNumRowsSave = "TextureNumRows: " + 1;
		} else {
			textureSave = "Texture: " + (component.getTexture() == null ? null : component.getTexture().getFile().getPath().substring(1, component.getTexture().getFile().getPath().length()));
			textureNumRowsSave = "TextureNumRows: " + (component.getTexture() == null ? 1 : component.getTexture().getNumberOfRows());
		}

		String normalMapSave;
		String normalMapNumRowsSave;

		if (overrideNormalsName != null) {
			MyFile tempFile = new MyFile(MyFile.RES_FOLDER, "entities", overrideTextureName.replace("Normals.png", ""), overrideNormalsName);
			normalMapSave = "NormalMap: " + tempFile.getPath().substring(1, tempFile.getPath().length());
			normalMapNumRowsSave = "NormalMapNumRows: " + 1;
		} else {
			normalMapSave = "NormalMap: " + (component.getNormalMap() == null ? null : component.getNormalMap().getFile().getPath().substring(1, component.getNormalMap().getFile().getPath().length()));
			normalMapNumRowsSave = "NormalMapNumRows: " + (component.getNormalMap() == null ? 1 : component.getNormalMap().getNumberOfRows());
		}

		String scaleSave = "Scale: " + component.getScale();


		return new Pair<>(
				new String[]{textureSave, textureNumRowsSave, normalMapSave, normalMapNumRowsSave, scaleSave},
				new EntitySaverFunction[]{saveVertices, saveTextureCoords, saveNormals, saveTangents, saveIndices}
		);
	}

	public void setOverrideTexture(String overrideTextureName) {
		this.overrideTextureName = overrideTextureName;
	}

	public void setOverrideNormals(String overrideNormalsName) {
		this.overrideNormalsName = overrideNormalsName;
	}
}
