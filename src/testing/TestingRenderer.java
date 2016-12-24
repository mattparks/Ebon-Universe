package testing;

import flounder.framework.*;
import flounder.helpers.*;
import flounder.renderer.*;

public class TestingRenderer extends IExtension implements IRendererMaster {
	public TestingRenderer() {
		super(FlounderRenderer.class);
	}

	@Override
	public void init() {

	}

	@Override
	public void render() {
		OpenGlUtils.prepareNewRenderParse(1.0f, 0.0f, 0.0f);
	}

	@Override
	public void profile() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isActive() {
		return true;
	}
}
