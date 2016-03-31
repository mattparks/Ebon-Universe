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
		playlist.addMusic(Sound.loadSoundNow(new MyFile(ManagerDevices.getSound().SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		// playlist.addMusic(Sound.loadSoundNow(new MyFile(ManagerAudio.SOUND_FOLDER, "birds.wav"), 0.5f));
		playlist.addMusic(Sound.loadSoundNow(new MyFile(ManagerDevices.getSound().SOUND_FOLDER, "song.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true);
	}

	@Override
	public void update() {

	}

	@Override
	public void dispose() {

	}
}
