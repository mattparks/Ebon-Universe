import flounder.engine.*;

public class Main {
	public static void main(final String[] args) {
		FlounderEngine.preinit(null, 1080, 720, "Voxels", 144, false, true, 0, false);
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MasterRenderer()));
		FlounderEngine.run();
		FlounderEngine.dispose();
	}
}
