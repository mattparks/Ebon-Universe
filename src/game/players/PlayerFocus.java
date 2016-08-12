package game.players;

import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import game.*;
import game.entities.*;
import game.entities.objects.*;
import game.options.*;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerFocus implements IPlayer {
	private static final float SPEED_BOOST_SCALE = 2.75f;
	private static final float FRONT_SPEED = 30;
	private static final float UP_SPEED = 20;
	private static final float SIDE_SPEED = 30;

	private IAxis inputForward;
	private IAxis inputUp;
	private IAxis inputSide;
	private KeyButton inputSpeedBoost;

	private Vector3f velocity;

	private Vector3f position;
	private Vector3f rotation;

	private Entity focusEntity;

	@Override
	public void init() {
		IButton leftKeyButtons = new KeyButton(GLFW_KEY_A, GLFW_KEY_LEFT);
		IButton rightKeyButtons = new KeyButton(GLFW_KEY_D, GLFW_KEY_RIGHT);
		IButton forwardsKeyButtons = new KeyButton(GLFW_KEY_W, GLFW_KEY_UP);
		IButton backwardsKeyButtons = new KeyButton(GLFW_KEY_S, GLFW_KEY_DOWN);
		IButton upKeyButtons = new KeyButton(GLFW_KEY_SPACE);
		IButton downKeyButtons = new KeyButton(GLFW_KEY_LEFT_CONTROL);

		this.inputForward = new CompoundAxis(new ButtonAxis(forwardsKeyButtons, backwardsKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_Y));
		this.inputUp = new CompoundAxis(new ButtonAxis(downKeyButtons, upKeyButtons));
		this.inputSide = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_AXIS_X));
		this.inputSpeedBoost = new KeyButton(GLFW_KEY_LEFT_SHIFT);

		this.velocity = new Vector3f(0, 0, 0);

		this.position = new Vector3f(0, 5, 0);
		this.rotation = new Vector3f(0, 0, 0);

		focusEntity = EntityBarrel.createEntity(Environment.getEntitys(), new Vector3f(position), new Vector3f(rotation));
	}

	@Override
	public void update(boolean paused) {
		if (!paused) {
			float speedBoost = inputSpeedBoost.isDown() ? SPEED_BOOST_SCALE : 1.0f;
			velocity.z = speedBoost * -FRONT_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputForward.getAmount());
			velocity.y = speedBoost * UP_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputUp.getAmount());
			velocity.x = speedBoost * -SIDE_SPEED * FlounderEngine.getDelta() * Maths.deadband(0.05f, inputSide.getAmount());

			if (Math.abs(velocity.x) > 0 || Math.abs(velocity.y) > 0 || Math.abs(velocity.z) > 0) {
				focusEntity.move(velocity, rotation);
				// Vector3f.add(position, velocity, position);
				position.set(focusEntity.getPosition());
			}
		}
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}
}
