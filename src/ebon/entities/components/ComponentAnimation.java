package ebon.entities.components;

import ebon.entities.*;
import ebon.entities.loading.*;
import flounder.animation.*;

/**
 * Creates a animation used to set animation properties.
 */
public class ComponentAnimation extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private Animation animation;

	/**
	 * Creates a new ComponentAnimation.
	 *
	 * @param entity The entity this component is attached to.
	 * @param animation The animation that will be attached to this entity.
	 */
	public ComponentAnimation(Entity entity, Animation animation) {
		super(entity, ID);
		this.animation = animation;
	}

	/**
	 * Creates a new ComponentAnimation. From strings loaded from entity files.
	 *
	 * @param entity The entity this component is attached to.
	 * @param template The entity template to load data from.
	 */
	public ComponentAnimation(Entity entity, EntityTemplate template) {
		super(entity, ID);

		/*this.animation = Animation.newAnimation(new AnimationBuilder.LoadManual() {
			@Override
			public String getModelName() {
				return template.getEntityName();
			}

		//	@Override
		//	public float[] getAnimations() {
		//		return EntityTemplate.toFloatArray(template.getSectionData(ComponentAnimation.this, "Animations"));
		//	}

			// TODO: Load manually like a model.
		}).create();*/
	}

	@Override
	public void update() {
		//if (animation != null) {
		//	animation.update();
		//}
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	@Override
	public void dispose() {
	}
}
