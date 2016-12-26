package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.animation.*;
import flounder.collada.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class EditorAnimation extends IEditorComponent {
	public ComponentAnimation component;

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
		JButton loadCollada = new JButton("Select Collada");
		loadCollada.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getAbsolutePath().replace("\\", "/");
				EditorAnimation.this.pathCollada = new MyFile(selectedFile.split("/"));
			}
		});
		panel.add(loadCollada);

		// Load Texture.
		JButton loadTexture = new JButton("Select Texture");
		loadTexture.addActionListener((ActionEvent ae) -> {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getAbsolutePath().replace("\\", "/");
				EditorAnimation.this.pathTexture = new MyFile(selectedFile.split("/"));
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
			if (pathCollada != null/*  && (component.getModelAnimated() == null|| !component.getModelAnimated().getFile().equals(pathCollada.getPath()))*/) {
				if (pathCollada.getPath().contains(".dae")) {
					ModelAnimated modelAnimated = ColladaLoader.loadColladaModel(pathCollada);
					Animation animation = AnimationCreator.loadAnimation(pathCollada);
					component.setModelAnimated(modelAnimated);
					component.doAnimation(animation);
				}

				pathCollada = null;
			}

			if (pathTexture != null && (component.getTexture() == null || !component.getTexture().getFile().getPath().equals(pathTexture.getPath()))) {
				if (pathTexture.getPath().contains(".png")) {
					Texture texture = Texture.newTexture(pathTexture).create();
					component.setTexture(texture);
				}

				pathTexture = null;
			}
		}
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues(String entityName) {
		if (component.getTexture() != null) {
			try {
				File file = new File("entities/" + entityName + "/" + entityName + "Diffuse.png");

				if (file.exists()) {
					file.delete();
				}

				file.createNewFile();

				InputStream input = component.getTexture().getFile().getInputStream();
				OutputStream output = new FileOutputStream(file);
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
		EntitySaverFunction saveJoints = new EntitySaverFunction("Joints") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getModelAnimated() != null && component.getModelAnimated().getJointsData() != null) {
					Joint headJoint = component.getModelAnimated().getHeadJoint();
					List<Joint> joints = new ArrayList<>();
					headJoint.addSelfAndChildren(joints);

					for (Joint joint : joints) {
						String s = (joint == headJoint ? "true," : "false,") + joint.getIndex() + "," + joint.getName() + ",";

						for (float v : Matrix4f.toArray(joint.getLocalBindTransform())) {
							s += v + ",";
						}

						for (Joint c : joint.getChildren()) {
							s += c.getName() + "|";
						}

						entityFileWriter.writeSegmentData(s + ",");
					}

					//public final int index;
					//public final String name;
					//public final List<Joint> children;
					//private final Matrix4f localBindTransform;
				}
			}
		};
		EntitySaverFunction saveAnimation = new EntitySaverFunction("Animation") {
			@Override
			public void writeIntoSection(FileWriterHelper entityFileWriter) throws IOException {
				if (component.getAnimator() != null && component.getAnimator().getCurrentAnimation() != null) {
					Animation animation = component.getAnimator().getCurrentAnimation();

					for (KeyFrame frame : animation.getKeyFrames()) {
						for (String name : frame.getJointKeyFrames().keySet()) {
							JointTransform joint = frame.getJointKeyFrames().get(name);
							Vector3f position = joint.getPosition();
							Quaternion rotation = joint.getRotation();
							String s = frame.getTimeStamp() + "," + name + "," + position.x + "," + position.y + "," + position.z + "," + rotation.x + "," + rotation.y + "," + rotation.z + "," + rotation.w + ",";
							entityFileWriter.writeSegmentData(s);
						}
					}
				}
			}
		};
		// TODO: Save AABB and Hull.

		String saveScale = "Scale: " + component.getScale();
		String saveFurthestPoint = "FurthestPoint: " + component.getModelAnimated().getMeshData().getFurthestPoint();

		String saveTexture = "Texture: " + (component.getTexture() == null ? null : "res/entities/" + entityName + "/" + entityName + "Diffuse.png");
		String saveTextureNumRows = "TextureNumRows: " + (component.getTexture() == null ? 1 : component.getTexture().getNumberOfRows());

		String saveAnimationLength = "AnimationLength: " + (component.getAnimator() != null && component.getAnimator().getCurrentAnimation() != null ? component.getAnimator().getCurrentAnimation().getLength() : null);
		String saveJointCount = "JointCount: " + (component.getModelAnimated() != null && component.getModelAnimated().getJointsData() != null ? component.getModelAnimated().getJointsData().jointCount : null);

		return new Pair<>(
				new String[]{saveScale, saveFurthestPoint, saveTexture, saveTextureNumRows, saveAnimationLength, saveJointCount},
				new EntitySaverFunction[]{saveVertices, saveTextureCoords, saveNormals, saveTangents, saveIndices, saveJointIds, saveVertexWeights, saveJoints, saveAnimation}
		);
	}
}
