package game;

import flounder.engine.*;

public class Main {
	public static void main(final String[] args) {
		FlounderEngine.preinit(1080, 720, "Flounder Demo", 144, false, true, 4, false);
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MainRenderer()));
		new FlounderEngine().run();
		FlounderEngine.dispose();
	}
}
