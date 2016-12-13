package testing;

import flounder.devices.*;
import flounder.framework.*;
import flounder.resources.*;

public class Testing {
	public static void main(String[] args) {
		FlounderFramework framework = new FlounderFramework("Testing", -1, new TestingInterface(), new TestingGui(), new TestingRenderer(), new TestingCamera(), new TestingPlayer());
		FlounderDisplay.setup(1080, 720, "Testing", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon.png")}, false, true, 0, false);
		framework.run();
		System.exit(0);
	}
}
