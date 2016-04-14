package game;

import flounder.engine.*;

/**
 * The games main entry point.
 */
public class Main {
	public static void main(String[] args) {
		FlounderEngine.preinit(null, 1080, 720, "Flounder 2.0", 144, false, true, 0, false);
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MainMasterRenderer()));
		FlounderEngine.run();
		FlounderEngine.dispose();
	}
}
