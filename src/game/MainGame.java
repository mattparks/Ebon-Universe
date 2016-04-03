package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.inputs.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.sounds.*;

import static org.lwjgl.glfw.GLFW.*;

public class MainGame extends IGame {
	private KeyButton screenshot;
	private KeyButton pauseMusic;
	private KeyButton skipMusic;

	private Vector3f playerPosition;
	private Vector3f playerRotation;

	public MainGame() {
	}

	@Override
	public void init() {
		screenshot = new KeyButton(GLFW_KEY_F10);
		pauseMusic = new KeyButton(GLFW_KEY_DOWN);
		skipMusic = new KeyButton(GLFW_KEY_LEFT, GLFW_KEY_RIGHT);
		playerPosition = new Vector3f();
		playerRotation = new Vector3f();

		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		playlist.addMusic(Sound.loadSoundInBackground(new MyFile(DeviceSound.SOUND_FOLDER, "spacey-ambient.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 2.25f, 5.82f);

		Environment.init(new Fog(new Colour(0.15f, 0.16f, 0.18f), 0.001f, 2.0f, 0.0f, 500.0f));
		MainGuis.init();
	}

	@Override
	public void update() {
		MainGuis.update();

		if (screenshot.wasDown()) {
			ManagerDevices.getDisplay().screenshot();
		}

		if (pauseMusic.wasDown()) {
			ManagerDevices.getSound().getMusicPlayer().pauseTrack();
		}

		if (skipMusic.wasDown()) {
			ManagerDevices.getSound().getMusicPlayer().skipTrack();
		}

		super.updateGame(playerPosition, playerRotation, MainGuis.isMenuOpen(), MainGuis.getBlurFactor());
	}

	@Override
	public void dispose() {

	}
}
