import flounder.engine.*;
import options.*;

public class Main {
	public static void main(final String[] args) {
	//	final StartupOptions startupOptions = new StartupOptions();
	//	startupOptions.run();

		FlounderEngine.preinit(null, 1080, 720, "Voxels", 1080, false, true, 4, false);
		FlounderEngine.init(new IModule(new MainGame(), new MainCamera(), new MasterRenderer()));
		FlounderEngine.run();
		FlounderEngine.dispose();
	}
}
