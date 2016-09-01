package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.exceptions.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.textures.*;
import game.cameras.*;
import game.options.*;
import game.players.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame extends IGame {
	public static final Config CONFIG = new Config(new MyFile("configs", "settings.conf"));
	public static final Config POST_CONFIG = new Config(new MyFile("configs", "post.conf"));
	public static final Config CONTROLS_CONFIG = new Config(new MyFile("configs", "controls_joystick.conf"));

	private KeyButton screenshot;
	private KeyButton fullscreen;
	private KeyButton polygons;
	private CompoundButton toggleMusic;
	private CompoundButton skipMusic;

	private IPlayer player;

	private boolean stillLoading;

	@Override
	public void init() {
		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.toggleMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));
		this.stillLoading = true;
	}

	public void generateWorlds() {
	}

	public void generatePlayer() {
		if (FlounderEngine.getCamera() instanceof CameraFocus) {
			this.player = new PlayerFocus();
		} else if (FlounderEngine.getCamera() instanceof CameraFPS) {
			this.player = new PlayerFPS();
		} else {
			throw new FlounderRuntimeException("Could not find IPlayer implementation for ICamera!");
		}

		this.player.init();
	}

	public void destroyWorld() {
		player = null;
		Environment.destroy();
		System.gc();
	}

	@Override
	public void update() {
		if (screenshot.wasDown()) {
			FlounderEngine.getDevices().getDisplay().screenshot();
		}

		if (fullscreen.wasDown()) {
			FlounderEngine.getDevices().getDisplay().setFullscreen(!FlounderEngine.getDevices().getDisplay().isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenGlUtils.goWireframe(!OpenGlUtils.isInWireframe());
		}

		if (toggleMusic.wasDown()) {
			if (FlounderEngine.getDevices().getSound().getMusicPlayer().isPaused()) {
				FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
			} else {
				FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
			}
		}

		if (skipMusic.wasDown()) {
			MainSeed.randomize();
			FlounderEngine.getDevices().getSound().getMusicPlayer().skipTrack();
		}

		if (FlounderEngine.getManagerGUI().isMenuIsOpen()) {
			// Pause the music for the start screen.
			FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
		} else if (!FlounderEngine.getManagerGUI().isMenuIsOpen() && stillLoading) {
			// Unpause the music for the main menu.
			stillLoading = false;
			//	FlounderEngine.getLogger().log("Starting main menu music.");
			//	FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
		}

		if (player != null) {
			player.update(FlounderEngine.getManagerGUI().isMenuIsOpen());
			update(player.getPosition(), player.getRotation());
		}

		Environment.update();
	}

	@Override
	public void dispose() {
		CONTROLS_CONFIG.dispose();
		POST_CONFIG.dispose();
		CONFIG.dispose();
	}
}

