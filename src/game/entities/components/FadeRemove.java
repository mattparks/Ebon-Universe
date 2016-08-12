package game.entities.components;

import flounder.engine.*;
import game.entities.*;

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

	public void setDuration(double duration) {
		this.duration = duration;
	}
}
