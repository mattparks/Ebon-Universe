package ebon;

import ebon.cameras.*;
import ebon.options.*;
import ebon.players.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.exceptions.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.inputs.*;
import flounder.lights.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.parsing.*;
import flounder.particles.*;
import flounder.profiling.*;
import flounder.resources.*;
import flounder.shaders.*;
import flounder.sounds.*;
import flounder.textures.*;

import static org.lwjgl.glfw.GLFW.*;

public class Ebon extends FlounderEntrance {
	public static Config configMain;
	public static Config configPost;
	public static Config configControls;
	public static Ebon instance;

	private KeyButton screenshot;
	private KeyButton fullscreen;
	private KeyButton polygons;
	private CompoundButton toggleMusic;
	private CompoundButton skipMusic;
	private CompoundButton switchCamera;
	private IPlayer player;
	private boolean stillLoading;

	public static void main(String[] args) {
		FlounderEngine.loadEngineStatics("Ebon Universe");
		configMain = new Config(new MyFile(FlounderEngine.getRoamingFolder(), "configs", "settings.conf"));
		configPost = new Config(new MyFile(FlounderEngine.getRoamingFolder(), "configs", "post.conf"));
		configControls = new Config(new MyFile(FlounderEngine.getRoamingFolder(), "configs", "controls_joystick.conf"));
		MusicPlayer.SOUND_VOLUME = (float) configMain.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);

		instance = new Ebon(
				new CameraFPS(),
				new EbonRenderer(),
				new EbonGuis()
		);
		instance.startEngine(FlounderFonts.FFF_FORWARD);
		System.exit(1);
	}

	private Ebon(ICamera camera, IRendererMaster renderer, IManagerGUI managerGUI) {
		super(camera, renderer, managerGUI,
				configMain.getIntWithDefault("width", 1080, FlounderDisplay::getWindowWidth),
				configMain.getIntWithDefault("height", 720, FlounderDisplay::getWindowHeight),
				"Ebon Universe", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon.png")},
				configMain.getBooleanWithDefault("vsync", true, FlounderDisplay::isVSync),
				configMain.getBooleanWithDefault("antialias", true, FlounderDisplay::isAntialiasing),
				configMain.getIntWithDefault("msaa_samples", 4, FlounderDisplay::getSamples),
				configMain.getBooleanWithDefault("fullscreen", false, FlounderDisplay::isFullscreen),
				configMain.getIntWithDefault("fps_target", 60, FlounderEngine::getTargetFPS),
				FlounderLogger.class, FlounderProfiler.class, FlounderKeyboard.class, FlounderTextures.class, FlounderModels.class, FlounderParticles.class, FlounderShaders.class, FlounderGuis.class
		);
	}

	@Override
	public void init() {
		this.screenshot = new KeyButton(GLFW_KEY_F2);
		this.fullscreen = new KeyButton(GLFW_KEY_F11);
		this.polygons = new KeyButton(GLFW_KEY_P);
		this.toggleMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		this.skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));
		this.switchCamera = new CompoundButton(new KeyButton(GLFW_KEY_C), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_CAMERA_SWITCH));
		this.stillLoading = true;
	}

	public void generateWorlds() {
		Environment.init(new Fog(new Colour(0.0f, 0.0f, 0.0f), 0.003f, 2.0f, 0.0f, 50.0f), new Light(new Colour(0.85f, 0.85f, 0.85f), new Vector3f(0.0f, 2000.0f, 2000.0f)));
		Environment.createWorld();

		// EntityLoader.load("dragon").createEntity(Environment.getEntitys(), new Vector3f(30, 0, 0), new Vector3f());
		/*EntityLoader.load("pane").createEntity(Environment.getEntities(), new Vector3f(), new Vector3f());
		EntityLoader.load("sphere").createEntity(Environment.getEntities(), Environment.getLights().get(0).position, new Vector3f());

		for (int n = 0; n < 32; n++) {
			for (int p = 0; p < 32; p++) {
				for (int q = 0; q < 32; q++) {
					if (Maths.RANDOM.nextInt(10) == 1) {
						EntityLoader.load("crate").createEntity(Environment.getEntities(), new Vector3f((n * 5) + 10, (p * 5) + 10, (q * 5) + 10), new Vector3f(0, Maths.RANDOM.nextInt(360), 0));
					}
				}
			}
		}*/
	}

	public void generatePlayer() {
		//if (FlounderEngine.getCamera() instanceof CameraFocus) {
		//	this.player = new PlayerFocus();
		//} else
		if (FlounderEngine.getCamera() instanceof CameraFPS) {
			this.player = new PlayerFPS();
		} else {
			throw new FlounderRuntimeException("Could not find IPlayer implementation for ICamera!");
		}

		this.player.init();
	}

	public void destroyWorld() {
		player = null;
		FlounderParticles.clear();
		Environment.destroy();
		System.gc();
	}

	@Override
	public void update() {
		if (FlounderEngine.getManagerGUI().isMenuIsOpen()) {
			// Pause the music for the start screen.
			FlounderSound.getMusicPlayer().pauseTrack();
		} else if (!FlounderEngine.getManagerGUI().isMenuIsOpen() && stillLoading) {
			// Unpause the music for the main menu.
			stillLoading = false;
			//	FlounderLogger.log("Starting main menu music.");
			//	FlounderSound.getMusicPlayer().unpauseTrack();
		}

		if (screenshot.wasDown()) {
			FlounderDisplay.screenshot();
			((EbonGuis) FlounderEngine.getManagerGUI()).getOverlayStatus().addMessage("Taking screenshot!");
		}

		if (fullscreen.wasDown()) {
			FlounderDisplay.setFullscreen(!FlounderDisplay.isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenGlUtils.goWireframe(!OpenGlUtils.isInWireframe());
		}

		if (toggleMusic.wasDown()) {
			if (FlounderSound.getMusicPlayer().isPaused()) {
				FlounderSound.getMusicPlayer().unpauseTrack();
			} else {
				FlounderSound.getMusicPlayer().pauseTrack();
			}
		}

		if (skipMusic.wasDown()) {
			EbonSeed.randomize();
			FlounderSound.getMusicPlayer().skipTrack();
		}

		if (switchCamera.wasDown()) {
			//	switchCamera();
		}

		if (player != null) {
			player.update(FlounderEngine.getManagerGUI().isMenuIsOpen());
			update(player.getPosition(), player.getRotation());
		}

		Environment.update();
	}

	/*public void switchCamera() {
		if (FlounderEngine.getCamera() instanceof CameraFocus) {
			CameraFPS newCamera = new CameraFPS();
			PlayerFPS newPlayer = new PlayerFPS();
			newPlayer.setPosition(player.getPosition());
			newPlayer.setRotation(player.getRotation());
			newPlayer.init();
			player.dispose();
			player = newPlayer;
			FlounderEngine.setCamera(newCamera);
		} else if (FlounderEngine.getCamera() instanceof CameraFPS) {
			CameraFocus newCamera = new CameraFocus();
			PlayerFocus newPlayer = new PlayerFocus();
			newPlayer.setPosition(player.getPosition());
			newPlayer.setRotation(player.getRotation());
			newPlayer.init();
			player.dispose();
			player = newPlayer;
			FlounderEngine.setCamera(newCamera);
		}
	}*/

	@Override
	public void profile() {

	}

	@Override
	public void dispose() {
		configControls.dispose();
		configPost.dispose();
		configMain.dispose();
	}
}
