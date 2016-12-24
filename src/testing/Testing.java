package testing;

import flounder.devices.*;
import flounder.framework.*;
import flounder.resources.*;

public class Testing extends FlounderFramework {
	public static void main(String[] args) {
		Testing testing = new Testing();
		testing.run();
		System.exit(0);
	}

	public Testing() {
		super("Testing", -1, new TestingJFrame(), new TestingInterface(), new TestingRenderer());
		FlounderDisplay.setup(1080, 720, "Testing", new MyFile[]{}, false, true, 0, false, true);
	}
}
