package ebon.entities.editing;

import ebon.entities.*;
import ebon.entities.components.*;
import ebon.entities.loading.*;
import flounder.helpers.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class EditorRemoveFade extends IEditorComponent {
	public ComponentRemoveFade component;
	private boolean testRemove;

	public EditorRemoveFade(Entity entity) {
		this.component = new ComponentRemoveFade(entity, 0.0f);
		this.component.setTestMode(true);
		this.testRemove = false;
	}

	public EditorRemoveFade(IEntityComponent component) {
		this.component = (ComponentRemoveFade) component;
		this.component.setTestMode(true);
		this.testRemove = false;
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
		timeSlider.setToolTipText("Remove Time Length");
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

		// Turn on labels at major tick marks.
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
				EditorRemoveFade.this.testRemove = true;
			}
		});
		panel.add(testButton);
	}

	@Override
	public void update() {
		if (component.getEntity() != null) {
			component.getEntity().remove();
			component.activate();
			testRemove = false;
		}
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		String saveRemoveAfter = "RemovesAfterDuration: " + component.removesAfterDuration();
		String saveDuration = "Duration: " + component.getDuration();
		return new Pair<>(new String[]{saveRemoveAfter, saveDuration}, new EntitySaverFunction[]{});
	}
}
