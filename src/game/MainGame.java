package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.textures.*;
import game.particles.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame extends IGame {
	private static List<IParticleSystem> particleSystems = new ArrayList<>();

	private KeyButton screenshot;
	private KeyButton fullscreen;
	private KeyButton polygons;
	private KeyButton pauseMusic;
	private KeyButton skipMusic;

	private Vector3f playerPosition;
	private Vector3f playerRotation;

	public MainGame() {
	}

	@Override
	public void init() {
		screenshot = new KeyButton(GLFW_KEY_F2);
		fullscreen = new KeyButton(GLFW_KEY_F11);
		polygons = new KeyButton(GLFW_KEY_P);
		pauseMusic = new KeyButton(GLFW_KEY_DOWN);
		skipMusic = new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT);
		playerPosition = new Vector3f();
		playerRotation = new Vector3f();

		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "spacey-ambient.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 2.25f, 5.82f);

		Texture coinsTexture = Texture.newTexture(new MyFile(ParticleManager.PARTICLES_LOC, "coins.png")).create();
		coinsTexture.setNumberOfRows(3);

		List<ParticleType> particleTypes = new ArrayList<>();
		particleTypes.add(new ParticleType(coinsTexture, 0.0986f, 3.2f, 0, 1.8f));

		SystemPlayer particleSystem = new SystemPlayer(40, 7.36f, particleTypes);
		particleSystem.setSpeedError(0.25f);
		particleSystem.randomizeRotation();
		particleSystem.setDirection(new Vector3f(0, 0.1f, 0), 0.5f);
		particleSystem.setSystemCenter(playerPosition);
		particleSystems.add(particleSystem);

		Environment.init(new Fog(new Colour(0.15f, 0.16f, 0.18f), 0.001f, 2.0f, 0.0f, 500.0f));
		MainGuis.init();
		ParticleManager.init();
	}

	@Override
	public void update() {
		MainGuis.update();

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
			particleSystems.forEach(IParticleSystem::update);
			ParticleManager.update();
		}
	}

	@Override
	public void dispose() {

	}
}
