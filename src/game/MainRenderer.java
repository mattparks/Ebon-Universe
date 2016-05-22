package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.post.piplines.*;
import flounder.textures.fbos.*;
import game.post.*;
import game.skybox.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);

	private Matrix4f projectionMatrix;

	private AABBRenderer aabbRenderer;
	private SkyboxRenderer skyboxRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO postProcessingFBO;
	private PipelineDemo pipelineDemo;
	private PipelineGaussian pipelineGaussian1;
	private PipelineGaussian pipelineGaussian2;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.aabbRenderer = new AABBRenderer();
		this.skyboxRenderer = new SkyboxRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		final int displayWidth = ManagerDevices.getDisplay().getWidth();
		final int displayHeight = ManagerDevices.getDisplay().getHeight();
		multisamplingFBO = FBO.newFBO(displayWidth, displayHeight).fitToScreen().antialias(ManagerDevices.getDisplay().getSamples()).create();
		postProcessingFBO = FBO.newFBO(displayWidth, displayHeight).fitToScreen().depthBuffer(FBOBuilder.DepthBufferType.TEXTURE).create();
		pipelineDemo = new PipelineDemo();
		pipelineGaussian1 = new PipelineGaussian(displayWidth / 5, displayHeight / 5, false);
		pipelineGaussian2 = new PipelineGaussian(displayWidth / 2, displayHeight / 2, false);
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY);

		/* Post rendering. */
		renderPost(FlounderEngine.isGamePaused(), FlounderEngine.getScreenBlur());

		/* Scene independents. */
		fontRenderer.render(POSITIVE_INFINITY, null);
		guiRenderer.render(POSITIVE_INFINITY, null);

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	private void bindRelevantFBO() {
		if (ManagerDevices.getDisplay().isAntialiasing()) {
			multisamplingFBO.bindFrameBuffer();
		} else {
			postProcessingFBO.bindFrameBuffer();
		}
	}

	private void unbindRelevantFBO() {
		if (ManagerDevices.getDisplay().isAntialiasing()) {
			multisamplingFBO.unbindFrameBuffer();
			multisamplingFBO.resolveMultisampledFBO(postProcessingFBO);
		} else {
			postProcessingFBO.unbindFrameBuffer();
		}
	}

	private void renderScene(final Vector4f clipPlane) {
		/* Clear and update. */
		OpenglUtils.prepareNewRenderParse(Environment.getFog().getFogColour());
		ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Renders each renderer. */
		aabbRenderer.render(clipPlane, camera);
		skyboxRenderer.render(clipPlane, camera);
	}

	private void renderPost(final boolean isPaused, final float blurFactor) {
		FBO output = postProcessingFBO;

		// Demo Pipeline:
		pipelineDemo.renderPipeline(postProcessingFBO);
		output = pipelineDemo.getOutput();

		if (isPaused || blurFactor != 0.0f) {
			pipelineGaussian1.setBlendSpreadValue(blurFactor, 1.0f, 0.0f, 1.0f);
			pipelineGaussian1.setScale(0.5f);
			pipelineGaussian1.renderPipeline(output);

			pipelineGaussian2.setBlendSpreadValue(blurFactor, 1.0f, 0.0f, 1.0f);
			pipelineGaussian2.setScale(2.0f);
			pipelineGaussian2.renderPipeline(pipelineGaussian1.getOutput());

			output = pipelineGaussian2.getOutput();
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
		skyboxRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		postProcessingFBO.delete();
		pipelineDemo.dispose();
		pipelineGaussian1.dispose();
		pipelineGaussian2.dispose();
	}
}

