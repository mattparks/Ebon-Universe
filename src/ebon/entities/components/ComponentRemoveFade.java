package ebon.entities.components;

import flounder.entities.*;
import flounder.entities.template.*;
import flounder.framework.*;

/**
 * A ComponentRemove that fades out until the entity disappears.
 */
public class ComponentRemoveFade extends ComponentRemove {
	private boolean removesAfterDuration;
	private double timer;
	private double duration;
	private boolean testMode;

	/**
	 * Creates a new ComponentRemoveFade
	 *
	 * @param entity The entity this component is attached to.
	 * @param duration How long the fade out takes.
	 */
	public ComponentRemoveFade(Entity entity, double duration) {
		super(entity);
		timer = 0.0;
		this.duration = duration;
		this.removesAfterDuration = true;
		this.testMode = false;
	}

	/**
	 * Creates a new ComponentRemoveFade. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ComponentRemoveFade(Entity entity, EntityTemplate template) {
		super(entity);
		this.duration = Double.parseDouble(template.getValue(this, "Duration"));
		this.removesAfterDuration = Boolean.parseBoolean(template.getValue(this, "RemovesAfterDuration"));
		this.testMode = false;
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
		timer += FlounderFramework.getDelta();
		double fadeAmount = (duration - timer) / duration;

		ComponentModel mc = (ComponentModel) super.getEntity().getComponent(ComponentModel.ID);

		if (timer >= duration) {
			if (testMode) {
				if (mc != null) {
					fadeAmount = 1.0f;
					timer = 0.0f;
					super.deactivate();
				}
			} else if (removesAfterDuration) {
				getEntity().removeComponent(ComponentCollision.ID);
				getEntity().forceRemove();
			}
		}

		if (mc != null) {
			mc.setTransparency((float) fadeAmount);
		}
	}

	public boolean removesAfterDuration() {
		return removesAfterDuration;
	}

	public void setRemovesAfterDuration(boolean removesAfterDuration) {
		this.removesAfterDuration = removesAfterDuration;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@Override
	public void dispose() {
	}
}
