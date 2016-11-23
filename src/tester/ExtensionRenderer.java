package tester;

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
import tester.options.*;
import tester.post.*;
import tester.uis.*;

public class ExtensionRenderer extends IExtension implements IRendererMaster {
	public static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	public static final int FBO_ATTACHMENTS = 1;

	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;

	public ExtensionRenderer() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderDisplay.class, FlounderRenderer.class);
	}

	@Override
	public void init() {
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).antialias(
				FlounderTester.configMain.getIntWithDefault("msaa_samples", 4, () -> multisamplingFBO.getSamples())
		).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).create();

		this.pipelineDemo = new PipelineDemo();
		this.pipelinePaused = new PipelinePaused();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY, MainSlider.FADE_COLOUR_STARTUP); // EbonWorld.getFog() != null ? EbonWorld.getFog().getFogColour() :

		/* Post rendering. */
		renderPost(FlounderGuis.getGuiMaster().isMenuIsOpen(), FlounderGuis.getGuiMaster().getBlurFactor());

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

	public int getSamples() {
		return multisamplingFBO.getSamples();
	}

	public void setSamples(int samples) {
		multisamplingFBO.setSamples(samples);
	}

	private void renderScene(Vector4f clipPlane, Colour clearColour) {
		/* Clear and run. */
		ICamera camera = FlounderCamera.getCamera();
		OpenGlUtils.prepareNewRenderParse(clearColour);

		boundingRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, float blurFactor) {
		FBO output = nonsampledFBO;

		if (OptionsPost.POST_ENABLED) {
			if (pipelineDemo.willRunDemo()) {
				pipelineDemo.renderPipeline(output);
				output = pipelineDemo.getOutput();
			}

			if (isPaused || blurFactor != 0.0f) {
				pipelinePaused.setBlurFactor(blurFactor);
				pipelinePaused.renderPipeline(output);
				output = pipelinePaused.getOutput();
			}
		}

		output.blitToScreen();
	}

	@Override
	public void profile() {
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

		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}