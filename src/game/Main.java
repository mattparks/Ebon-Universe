package game;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.sounds.*;

public class Main {
	public static void main(final String[] args) {
		MusicPlayer.SOUND_VOLUME = 0.5f;

		final IModule module = new IModule(new MainGame(), new MainCamera(), new MainRenderer());
		final FlounderEngine engine = new FlounderEngine(module, 1080, 720, "Flounder Demo", 144, false, true, 0, false);
		engine.startEngine(FontManager.NEXA_BOLD);
		engine.closeEngine();
	}
}
