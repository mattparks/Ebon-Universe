package testing;

import flounder.camera.*;
import flounder.devices.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.renderer.*;

public class TestingRenderer extends IExtension implements IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final Colour CLEAR_COLOUR = new Colour(1.0f, 1.0f, 1.0f);

	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	public TestingRenderer() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderDisplay.class, FlounderRenderer.class);
	}

	@Override
	public void init() {
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).antialias(4).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).create();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY, CLEAR_COLOUR);

		/* Post rendering. */
		renderPost(FlounderGuis.getGuiMaster().isGamePaused(), FlounderGuis.getGuiMaster().getBlurFactor());

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	private void bindRelevantFBO() {
		if (FlounderDisplay.isAntialiasing()) {
			multisamplingFBO.bindFrameBuffer();
		} else {
			nonsampledFBO.bindFrameBuffer();
		}
	}

	private void unbindRelevantFBO() {
		if (FlounderDisplay.isAntialiasing()) {
			multisamplingFBO.unbindFrameBuffer();
			multisamplingFBO.resolveFBO(nonsampledFBO);
		} else {
			nonsampledFBO.unbindFrameBuffer();
		}
	}

	private void renderScene(Vector4f clipPlane, Colour clearColour) {
		/* Clear and update. */
		ICamera camera = FlounderCamera.getCamera();
		OpenGlUtils.prepareNewRenderParse(clearColour);

		/* Renders each renderer. */
		boundingRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, float blurFactor) {
		FBO output = nonsampledFBO;
		output.blitToScreen();
	}

	@Override
	public void profile() {

	}

	@Override
	public void dispose() {
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();
	}

	@Override
	public boolean isActive() {
		return true;
	}
}

