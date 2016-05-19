package game;

import flounder.engine.*;

public class Main {
	public static void main(final String[] args) {
		final IModule module = new IModule(new MainGame(), new MainCamera(), new MainRenderer());
		final FlounderEngine engine = new FlounderEngine(module, 1080, 720, "Flounder Demo", 144, false, true, 4, false);
		engine.run();
		engine.dispose();
	}
}
