package testing;

import flounder.framework.*;
import flounder.standard.*;

public class TestingInterface extends IExtension implements IStandard {
	public TestingInterface() {
		super(FlounderStandard.class);
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isActive() {
		return true;
	}
}
