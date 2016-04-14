package game.entities.components;

import flounder.maths.vectors.*;
import game.entities.*;
import game.lights.*;

/**
 * Creates a light that can be rendered into the world.
 */
public class LightComponent extends IEntityComponent {
	public static final int ID = EntityAssignerID.getId();

	private final Vector3f offset;
	private final Light light;

	/**
	 * Creates a new LightComponent.
	 *
	 * @param entity The entity this component is attached to.
	 * @param offset How much the light position should be offset from the engine.entities position.
	 * @param light The light too be attached too the entity.
	 */
	public LightComponent(final Entity entity, final Vector3f offset, final Light light) {
		super(entity, ID);
		this.offset = offset;
		this.light = light;
	}

	public Vector3f getOffset() {
		return offset;
	}

	public void setOffset(final Vector3f offset) {
		this.offset.set(offset);
	}

	public Light getLight() {
		return new Light(light.getColour(), Vector3f.add(super.getEntity().getPosition(), offset, null), light.getAttenuation());
	}

	@Override
	public void update() {
	}
}
