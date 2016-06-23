package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.renderer.*;
import flounder.post.filters.*;
import flounder.post.piplines.*;
import game.blocks.*;
import game.options.*;
import game.post.*;
import game.skybox.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);

	private Matrix4f projectionMatrix;

	private AABBRenderer aabbRenderer;
	//private SkyboxRenderer skyboxRenderer;
	private BlockRenderer blockRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO postProcessingFBO;

	private PipelineDemo pipelineDemo;

	private FilterDarken filterDarken;
	private FBO pipelineGaussian1;
	private PipelineGaussian pipelineGaussian2;
	private FilterCombineSlide filterCombineSlide;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.aabbRenderer = new AABBRenderer();
		//this.skyboxRenderer = new SkyboxRenderer();
		this.blockRenderer = new BlockRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		multisamplingFBO = FBO.newFBO(1.0f).antialias(FlounderEngine.getDevices().getDisplay().getSamples()).create();
		postProcessingFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).create();

		pipelineDemo = new PipelineDemo();

		filterDarken = new FilterDarken();
		pipelineGaussian1 = FBO.newFBO(1.0f / 10.0f).depthBuffer(DepthBufferType.NONE).create();
		pipelineGaussian2 = new PipelineGaussian(1.0f / 7.0f);
		filterCombineSlide = new FilterCombineSlide();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY);

		/* Post rendering. */
		renderPost(FlounderEngine.isGamePaused(), MainGuis.isStartingGame(), FlounderEngine.getScreenBlur());

		/* Scene independents. */
		fontRenderer.render(POSITIVE_INFINITY, null);
		guiRenderer.render(POSITIVE_INFINITY, null);

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	private void bindRelevantFBO() {
		if (OptionsPost.POST_ENABLED) {
			if (FlounderEngine.getDevices().getDisplay().isAntialiasing()) {
				multisamplingFBO.bindFrameBuffer();
			} else {
				postProcessingFBO.bindFrameBuffer();
			}
		}
	}

	private void unbindRelevantFBO() {
		if (OptionsPost.POST_ENABLED) {
			if (FlounderEngine.getDevices().getDisplay().isAntialiasing()) {
				multisamplingFBO.unbindFrameBuffer();
				multisamplingFBO.resolveFBO(postProcessingFBO);
			} else {
				postProcessingFBO.unbindFrameBuffer();
			}
		}
	}

	private void renderScene(Vector4f clipPlane) {
		/* Clear and update. */
		OpenGlUtils.prepareNewRenderParse(MainGuis.isStartingGame() ? MainGuis.STARTUP_COLOUR : Environment.getFog().getFogColour());
		ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), FlounderEngine.getDevices().getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Don't render while starting. */
		if (MainGuis.isStartingGame()) {
			return;
		}

		/* Renders each renderer. */
		aabbRenderer.render(clipPlane, camera);
		//skyboxRenderer.render(clipPlane, camera);
		blockRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, boolean isStarting, float blurFactor) {
		FBO output = postProcessingFBO;

		if (OptionsPost.POST_ENABLED && !isStarting) {
			// Demo Pipeline:
			pipelineDemo.renderPipeline(postProcessingFBO);
			output = pipelineDemo.getOutput();

			// Paused Screen:
			if (isPaused || blurFactor != 0.0f) {
				output.resolveFBO(pipelineGaussian1);

				pipelineGaussian2.setScale(1.25f);
				pipelineGaussian2.renderPipeline(pipelineGaussian1);

				filterDarken.applyFilter(pipelineGaussian2.getOutput().getColourTexture());
				filterDarken.setFactorValue(Math.max(Math.abs(1.0f - blurFactor), 0.45f));

				filterCombineSlide.setSlideSpace(blurFactor, 1.0f, 0.0f, 1.0f);
				filterCombineSlide.applyFilter(output.getColourTexture(), filterDarken.fbo.getColourTexture());
				output = filterCombineSlide.fbo;
			}
		}

		output.blitToScreen();
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		aabbRenderer.dispose();
		//skyboxRenderer.dispose();
		blockRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		postProcessingFBO.delete();

		pipelineDemo.dispose();

		filterDarken.dispose();
		pipelineGaussian1.delete();
		pipelineGaussian2.dispose();
		filterCombineSlide.dispose();
	}
}

