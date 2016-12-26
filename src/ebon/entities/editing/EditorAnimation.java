package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.resources.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class EditorAnimation extends IEditorComponent {
	public ComponentAnimation component;

	private MyFile pathCollada;

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
	}

	@Override
	public void update() {
		if (component != null) {
		/*	if (pathCollada != null && (component.getAnimation() == null || !component.getAnimation().getFile().equals(pathCollada.getPath()))) {
				if (pathCollada.getPath().contains(".dae")) {
					Animation animation = Animation.newAnimation(pathCollada).create();
					component.setAnimation(animation);
				}
			}*/
		}
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		return new Pair<>(new String[]{}, new EntitySaverFunction[]{});
	}
}
