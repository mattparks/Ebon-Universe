package game.entities.components;

import flounder.engine.*;
import flounder.helpers.*;
import game.editors.entity.*;
import game.entities.*;
import game.entities.loading.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * A RemoveComponent that fades out until the entity disappears.
 */
public class FadeRemove extends RemoveComponent {
	public boolean removesAfterDuration;
	private double timer;
	private double duration;
	private boolean testMode;

	/**
	 * Creates a new FadeRemove
	 *
	 * @param entity The entity this component is attached to.
	 */
	public FadeRemove(Entity entity) {
		super(entity);
		timer = 0.0;
		this.duration = 0.0f;
		this.removesAfterDuration = true;
		this.testMode = false;
	}

	/**
	 * Creates a new FadeRemove
	 *
	 * @param entity The entity this component is attached to.
	 * @param duration How long the fade out takes.
	 */
	public FadeRemove(Entity entity, double duration) {
		super(entity);
		timer = 0.0;
		this.duration = duration;
		this.removesAfterDuration = true;
		this.testMode = false;
	}

	/**
	 * Creates a new FadeRemove. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public FadeRemove(Entity entity, EntityTemplate template) {
		super(entity);
		this.duration = Double.parseDouble(template.getValue(this, "Duration"));
		this.removesAfterDuration = Boolean.parseBoolean(template.getValue(this, "RemovesAfterDuration"));
		this.testMode = false;
	}

	@Override
	public void addToPanel(JPanel panel) {
		// Time Slider.
	//	panel.add(new JLabel("Time Slider: "));
		JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, (int) (duration * 5.0f));
		timeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();

				if (!source.getValueIsAdjusting()) {
					int reading = source.getValue();

					FadeRemove.this.duration = reading / 5.0f;
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
				FadeRemove.this.testMode = true;
				EntityGame.focusEntity.remove();
				FadeRemove.super.activate();
			}
		});
		panel.add(testButton);
	}

	@Override
	public void deactivate() {
		timer = 0.0;
	}

	@Override
	public void onActivate() {
	}

	@Override
	public void removeUpdate() {
		timer += FlounderEngine.getDelta();
		double fadeAmount = (duration - timer) / duration;

		ModelComponent mc = (ModelComponent) super.getEntity().getComponent(ModelComponent.ID);

		if (timer >= duration) {
			if (testMode) {
				if (mc != null) {
					fadeAmount = 1.0f;
					timer = 0.0f;
					super.deactivate();
				}
			} else if (removesAfterDuration) {
				getEntity().removeComponent(CollisionComponent.ID);
				getEntity().forceRemove();
			}
		}

		if (mc != null) {
			mc.setTransparency((float) fadeAmount);
		}
	}

	@Override
	public Pair<String[], EntitySaverFunction[]> getSavableValues() {
		String removeAfterSave = "RemovesAfterDuration: " + removesAfterDuration;
		String durationSave = "Duration: " + duration;
		return new Pair<>(new String[]{removeAfterSave, durationSave}, new EntitySaverFunction[]{});
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public void setRemovesAfterDuration(boolean removesAfterDuration) {
		this.removesAfterDuration = removesAfterDuration;
	}

	@Override
	public void dispose() {
	}
}
