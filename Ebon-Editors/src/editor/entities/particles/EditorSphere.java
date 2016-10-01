package editor.entities.particles;

import flounder.particles.spawns.*;

import javax.swing.*;
import javax.swing.event.*;

public class EditorSphere extends IEditorParticleSpawn {
	private SpawnSphere spawn;

	public EditorSphere() {
		spawn = new SpawnSphere(1.0f);
	}

	@Override
	public String getTabName() {
		return "Sphere";
	}

	@Override
	public SpawnSphere getComponent() {
		return spawn;
	}

	@Override
	public void addToPanel(JPanel panel) {
		// Radius Slider.
		JSlider radiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int) spawn.getRadius());
		radiusSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int reading = source.getValue();

				if (reading > 1) {
					spawn.setRadius(reading);
				}
			}
		});
		//Turn on labels at major tick marks.
		radiusSlider.setMajorTickSpacing(10);
		radiusSlider.setMinorTickSpacing(2);
		radiusSlider.setPaintTicks(true);
		radiusSlider.setPaintLabels(true);
		panel.add(radiusSlider);
	}

	@Override
	public String[] getSavableValues() {
		return new String[]{"" + spawn.getRadius()};
	}
}
