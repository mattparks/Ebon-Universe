import flounder.devices.*;
import flounder.engine.*;
import flounder.resources.*;
import flounder.sounds.*;


public class MainGame extends IGame {
	public MainGame() {
	}

	@Override
	public void init() {
		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundNow(new MyFile(DeviceSound.SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		playlist.addMusic(Sound.loadSoundNow(new MyFile(DeviceSound.SOUND_FOLDER, "spacey-ambient.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true, 2.25f, 5.82f);
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {

	}
}
