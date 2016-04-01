import flounder.devices.*;
import flounder.engine.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;


public class MainGame extends IGame {
	private MainGuiExample guiExample;

	public MainGame() {
	}

	@Override
	public void init() {
		Playlist playlist = new Playlist();
		playlist.addMusic(Sound.loadSoundNow(new MyFile(DeviceSound.SOUND_FOLDER, "era-of-space.wav"), 0.5f));
		ManagerDevices.getSound().getMusicPlayer().playMusicPlaylist(playlist, true);

		guiExample = new MainGuiExample();
		GuiManager.addComponent(guiExample, 0, 0, 1, 1);
		guiExample.show(true);
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {

	}
}
