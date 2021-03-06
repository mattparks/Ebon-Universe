package ebon;

import ebon.entities.*;
import ebon.options.*;
import ebon.particles.*;
import ebon.post.*;
import ebon.skybox.*;
import flounder.camera.*;
import flounder.devices.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.post.filters.*;
import flounder.profiling.*;
import flounder.renderer.*;

public class EbonRenderer extends IRendererMaster {
	public static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final Colour CLEAR_COLOUR = new Colour(0.0f, 0.0f, 0.0f);

	private SkyboxRenderer skyboxRenderer;
	private EntitiesRenderer entitiesRenderer;
	private ParticleRenderer particleRenderer;

	private BoundingRenderer boundingRenderer;
	private GuisRenderer guisRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;
	private FilterTiltShift filterTiltShift;

	public EbonRenderer() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderDisplay.class);
	}

	@Override
	public void init() {
		this.skyboxRenderer = new SkyboxRenderer();
		this.entitiesRenderer = new EntitiesRenderer();
		this.particleRenderer = new ParticleRenderer();

		this.boundingRenderer = new BoundingRenderer();
		this.guisRenderer = new GuisRenderer();
		this.fontRenderer = new FontRenderer();

		this.multisamplingFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).antialias(
				Ebon.configMain.getIntWithDefault("msaa_samples", 4, () -> multisamplingFBO.getSamples())
		).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).create();

		this.pipelineDemo = new PipelineDemo();
		this.pipelinePaused = new PipelinePaused();
		this.filterTiltShift = new FilterTiltShift(0.75f, 1.1f, 0.004f, 3.0f);
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
		guisRenderer.render(POSITIVE_INFINITY, FlounderCamera.getCamera());
		fontRenderer.render(POSITIVE_INFINITY, FlounderCamera.getCamera());

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

		skyboxRenderer.render(clipPlane, camera);
		entitiesRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
		boundingRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, float blurFactor) {
		FBO output = nonsampledFBO;

		if (OptionsPost.POST_ENABLED) {
			filterTiltShift.applyFilter(output.getColourTexture(0));
			output = filterTiltShift.fbo;

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

	public int getSamples() {
		return multisamplingFBO.getSamples();
	}

	public void setSamples(int samples) {
		multisamplingFBO.setSamples(samples);
	}

	@Override
	public void dispose() {
		skyboxRenderer.dispose();
		entitiesRenderer.dispose();
		particleRenderer.dispose();

		boundingRenderer.dispose();
		guisRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();

		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}

	@Override
	public boolean isActive() {
		return true;
	}
}

