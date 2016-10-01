package editor.entities.components;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import editor.entities.*;
import flounder.helpers.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class EditorRemoveFade extends IEditorComponent {
	public ComponentRemoveFade component;

	public EditorRemoveFade(Entity entity) {
		this.component = new ComponentRemoveFade(entity, 0.0f);
		component.setTestMode(true);
	}

	public EditorRemoveFade(IEntityComponent component) {
		this.component = (ComponentRemoveFade) component;
		this.component.setTestMode(true);
	}

	@Override
	public String getTabName() {
		return "Remove Fade";
	}

	@Override
	public ComponentRemoveFade getComponent() {
		return component;
	}

	@Override
	public void addToPanel(JPanel panel) {
		// Time Slider.
		//	panel.add(new JLabel("Time Slider: "));
		JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int) (component.getDuration() * 5.0f));
		timeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();

				if (!source.getValueIsAdjusting()) {
					int reading = source.getValue();
					component.setDuration(reading / 5.0f);
				}
			}
		});

		//Turn on labels at major tick marks.
		timeSlider.setMajorTickSpacing(10);
		timeSlider.setMinorTickSpacing(5);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(true);
		panel.add(timeSlider);

		// Test Button.
		JButton testButton = new JButton("Test Remove");
		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditorEntities.instance.focusEntity.remove();
				component.activate();
			}
		});
		panel.add(testButton);
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		String removeAfterSave = "RemovesAfterDuration: " + component.removesAfterDuration();
		String durationSave = "Duration: " + component.getDuration();
		return new Pair<>(new String[]{removeAfterSave, durationSave}, new EntitySaverFunction[]{});
	}
}
