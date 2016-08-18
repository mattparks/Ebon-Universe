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
import game.cameras.*;
import game.entities.loading.*;
import game.options.*;
import game.players.*;

import java.util.*;

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

	private MainGuis guis;
	private IPlayer player;

	private boolean stillLoading;

	@Override
	public void init() {
		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.toggleMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));

		this.guis = new MainGuis();

		if (FlounderEngine.getCamera() instanceof CameraFocus) {
			this.player = new PlayerFocus();
		} else if (FlounderEngine.getCamera() instanceof CameraFPS) {
			this.player = new PlayerFPS();
		} else {
			throw new FlounderRuntimeException("Could not find IPlayer implementation for ICamera!");
		}

		Environment.init(new Fog(new Colour(1.0f, 1.0f, 1.0f), 0.003f, 2.0f, 0.0f, 50.0f), new Light(new Colour(0.85f, 0.85f, 0.85f), new Vector3f(0.0f, 2000.0f, 2000.0f)));
		this.player.init();
		this.stillLoading = true;

		// EntityLoader.load("dragon").createEntity(Environment.getEntitys(), new Vector3f(30, 0, 0), new Vector3f());
		EntityLoader.load("pane").createEntity(Environment.getEntitys(), new Vector3f(), new Vector3f());
		EntityLoader.load("sphere").createEntity(Environment.getEntitys(), Environment.getLights().get(0).position, new Vector3f());

		Random ran = new Random();

		for (int n = 0; n < 32; n++) {
			for (int p = 0; p < 32; p++) {
				for (int q = 0; q < 32; q++) {
					if (ran.nextInt(10) == 1) {
						EntityLoader.load("crate").createEntity(Environment.getEntitys(), new Vector3f((n * 5) + 10, (p * 5) + 10, (q * 5) + 10), new Vector3f(0, ran.nextInt(360), 0));
					}
				}
			}
		}

		//	Playlist playlist = new Playlist();
		//	playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "era-of-space.wav"), 0.80f));
		//	playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "pyrosanical.wav"), 0.50f));
		//	playlist.addMusic(Sound.loadSoundInBackground(new MyFile(MyFile.RES_FOLDER, "music", "spacey-ambient.wav"), 0.60f));
		//	FlounderEngine.getDevices().getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 4.0f, 10.0f);

		for (int i = 0; i < 100; i++) {
			FlounderEngine.getLogger().log("Random Word" + i + ": " + FauxGenerator.getFauxSentance(2, 4, 12));
		}
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

		if (MainGuis.isStartingGame()) {
			// Pause the music for the start screen.
			FlounderEngine.getDevices().getSound().getMusicPlayer().pauseTrack();
		} else if (!MainGuis.isStartingGame() && stillLoading) {
			// Unpause the music for the main menu.
			stillLoading = false;
			//	FlounderEngine.getLogger().log("Starting main menu music.");
			//	FlounderEngine.getDevices().getSound().getMusicPlayer().unpauseTrack();
		}

		guis.update();
		player.update(guis.isMenuOpen());
		Environment.update();
		update(player.getPosition(), player.getRotation(), guis.isMenuOpen(), guis.getBlurFactor());
	}

	@Override
	public void dispose() {
		CONTROLS_CONFIG.dispose();
		POST_CONFIG.dispose();
		CONFIG.dispose();
	}
}

