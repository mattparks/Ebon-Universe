package editor.entities.particles;

import flounder.particles.spawns.*;

import javax.swing.*;

public class EditorPoint extends IEditorParticleSpawn {
	private SpawnPoint spawn;

	public EditorPoint() {
		spawn = new SpawnPoint();
	}

	@Override
	public String getTabName() {
		return "Point";
	}

	@Override
	public SpawnPoint getComponent() {
		return spawn;
	}

	@Override
	public void addToPanel(JPanel panel) {
	}

	@Override
	public String[] getSavableValues() {
		return new String[]{};
	}
}
