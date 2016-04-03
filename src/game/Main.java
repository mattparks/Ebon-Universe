package game;

import flounder.engine.*;

public class Main {
	public static void main(String[] args) {
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MainMasterRenderer()), 1080, 720, "Flounder 2.0", 144, false, true, 4, false);
		FlounderEngine.run();
		FlounderEngine.dispose();
	}
}
