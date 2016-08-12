package game.entities.components;

import flounder.engine.*;
import flounder.helpers.*;
import game.entities.*;
import game.entities.loading.*;

/**
 * A RemoveComponent that fades out until the entity disappears.
 */
public class FadeRemove extends RemoveComponent {
	public boolean removesAfterDuration;
	private double timer;
	private double duration;

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
		removesAfterDuration = true;
	}

	/**
	 * Creates a new FadeRemove. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public FadeRemove(Entity entity, EntityTemplate template) {
		this(entity, 0);
		this.duration = Double.parseDouble(template.getValue(this, "Duration"));
		this.removesAfterDuration = Boolean.parseBoolean(template.getValue(this, "RemovesAfterDuration"));
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

		if (timer >= duration && removesAfterDuration) {
			getEntity().removeComponent(CollisionComponent.ID);
			getEntity().forceRemove();
		}

		ModelComponent mc = (ModelComponent) super.getEntity().getComponent(ModelComponent.ID);

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
}
