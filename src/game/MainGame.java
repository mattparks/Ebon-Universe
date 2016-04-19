package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.sounds.*;
import game.blocks.*;
import game.models.*;
import game.options.*;
import game.particles.*;
import game.world.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame extends IGame {
	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;
	private KeyButton screenshot;
	private KeyButton fullscreen;
	private KeyButton polygons;
	private CompoundButton pauseMusic;
	private CompoundButton skipMusic;
	private IAxis inputForward;
	private IAxis inputTurn;
	private float currentSpeed;
	private float currentTurnSpeed;

	private Vector3f playerPosition;
	private Vector3f playerRotation;

	public MainGame() {
	}

	@Override
	public void init() {
		screenshot = new KeyButton(GLFW_KEY_F2);
		fullscreen = new KeyButton(GLFW_KEY_F11);
		polygons = new KeyButton(GLFW_KEY_P);
		pauseMusic = new CompoundButton(new KeyButton(GLFW_KEY_DOWN), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_PAUSE));
		skipMusic = new CompoundButton(new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT), new JoystickButton(OptionsControls.JOYSTICK_PORT, OptionsControls.JOYSTICK_MUSIC_SKIP));

		IButton leftKeyButtons = new KeyButton(new int[]{GLFW_KEY_A, GLFW_KEY_LEFT});
		IButton rightKeyButtons = new KeyButton(new int[]{GLFW_KEY_D, GLFW_KEY_RIGHT});
		IButton upKeyButtons = new KeyButton(new int[]{GLFW_KEY_W, GLFW_KEY_UP});
		IButton downKeyButtons = new KeyButton(new int[]{GLFW_KEY_S, GLFW_KEY_DOWN});
		inputForward = new CompoundAxis(new ButtonAxis(upKeyButtons, downKeyButtons), new JoystickAxis(0, 1));
		inputTurn = new CompoundAxis(new ButtonAxis(leftKeyButtons, rightKeyButtons), new JoystickAxis(0, 3));

		playerPosition = new Vector3f();
		playerRotation = new Vector3f();
		ChunkManager.init();

		// Initializes 3D game objects.
		Environment.init(new Fog(new Colour(0.15f, 0.16f, 0.18f), 0.001f, 2.0f, 0.0f, 500.0f));
		MainGuis.init();
		ParticleManager.init();

		// Creates a new music playlist and then plays it!
		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "spacey-ambient.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 3.2f, 7.2f);

		// Creates a new smoke particle type.
		//	Texture smokeTexture = Texture.newTexture(new MyFile(ParticleManager.PARTICLES_LOC, "smoke.png")).createInBackground();
		//	smokeTexture.setNumberOfRows(8);
		//	ParticleType smokeParticleType = new ParticleType(smokeTexture, 0.1f, 3.0f, 0, 6.0f);

		// Creates a new fire particle type.
		//	Texture fireTexture = Texture.newTexture(new MyFile(ParticleManager.PARTICLES_LOC, "fire.png")).createInBackground();
		//	fireTexture.setNumberOfRows(8);
		//	ParticleType fireParticleType = new ParticleType(fireTexture, 0.1f, 1.5f, 0, 4.0f);

		// Creates a list of usable particles for the system..
		//	List<ParticleType> particleTypes = new ArrayList<>();
		//	particleTypes.add(smokeParticleType);
		//	particleTypes.add(fireParticleType);

		// Creates a new simple particle emitter system.
		//	SystemSimple particleSystem = new SystemSimple(92, 10.0f, particleTypes);
		//	particleSystem.setSpeedError(0.3f);
		//	particleSystem.randomizeRotation();
		//	particleSystem.setDirection(new Vector3f(1.0f, 0, 0), 0.075f);
		//	particleSystem.setSystemCenter(playerPosition);
		//	ParticleManager.addSystem(particleSystem);
	}

	@Override
	public void update() {
		MainGuis.update();

		if (!MainGuis.isMenuOpen()) {
			currentSpeed = (float) (-RUN_SPEED * Maths.deadband(0.05f, inputForward.getAmount()));
			currentTurnSpeed = 0.0f; // Add back in once a player model is added. // (float) (-TURN_SPEED * Maths.deadband(0.05f, inputTurn.getAmount()));
			float distance = currentSpeed * FlounderEngine.getDelta();
			float dx = (float) (-distance * Math.sin(Math.toRadians(Maths.normalizeAngle(playerRotation.getY() + FlounderEngine.getCamera().getYaw()))));
			float dz = (float) (-distance * Math.cos(Math.toRadians(Maths.normalizeAngle(playerRotation.getY() - FlounderEngine.getCamera().getYaw()))));
			float ry = currentTurnSpeed * FlounderEngine.getDelta();
			playerPosition.set(playerPosition.x + dx, playerPosition.y, playerPosition.z + dz);
			playerRotation.set(playerRotation.x, playerRotation.y + ry, playerRotation.z);
		}

		if (screenshot.wasDown()) {
			ManagerDevices.getDisplay().screenshot();
		}

		if (fullscreen.wasDown()) {
			ManagerDevices.getDisplay().setFullscreen(!ManagerDevices.getDisplay().isFullscreen());
		}

		if (polygons.wasDown()) {
			OpenglUtils.goWireframe(!OpenglUtils.isInWireframe());
		}

		if (pauseMusic.wasDown()) {
			ManagerDevices.getSound().getMusicPlayer().pauseTrack();
		}

		if (skipMusic.wasDown()) {
			ManagerDevices.getSound().getMusicPlayer().skipTrack();
		}

		super.updateGame(playerPosition, playerRotation, MainGuis.isMenuOpen(), MainGuis.getBlurFactor());

		if (!MainGuis.isMenuOpen()) {
			ParticleManager.update();
			ChunkManager.update();
		}
	}

	@Override
	public void dispose() {

	}
}
