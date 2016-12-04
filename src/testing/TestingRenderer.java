package testing;

import flounder.camera.*;
import flounder.devices.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.renderer.*;

public class TestingRenderer extends IExtension implements IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final int FBO_ATTACHMENTS = 1;

	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	public TestingRenderer() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderRenderer.class);
	}

	@Override
	public void init() {
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).antialias(8).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).create();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY);

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	@Override
	public void profile() {

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

		nonsampledFBO.blitToScreen();
	}

	private void renderScene(Vector4f clipPlane) {
		/* Clear and update. */
		ICamera camera = FlounderCamera.getCamera();
		OpenGlUtils.prepareNewRenderParse(1.0f, 1.0f, 1.0f);

		/* Renders each renderer. */
		boundingRenderer.render(clipPlane, camera);
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void dispose() {
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();
	}
}

