import flounder.engine.*;

public class Main {
	public static void main(final String[] args) {
		// TODO: Unity like selection screen.

		FlounderEngine.preinit(null, 1080, 720, "Voxels", 144, false, true, 4, false);
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MasterRenderer()));
		FlounderEngine.run();
		FlounderEngine.dispose();
	}
}
