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
import game.entities.*;
import game.options.*;
import game.post.*;
import game.post.deferred.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);

	private Matrix4f projectionMatrix;

	private EntityRenderer entityRenderer;
	private AABBRenderer aabbRenderer;
	private GuiRenderer guiRenderer;
	private GuiRenderer cursorRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO postProcessingFBO;

	private FilterDeferred deferredShading;
	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.entityRenderer = new EntityRenderer();
		this.aabbRenderer = new AABBRenderer();
		this.guiRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.GUI);
		this.cursorRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.CURSOR);
		this.fontRenderer = new FontRenderer();

		// Diffuse, Position, Normals, Additonal (Specular, G, B, A)
		multisamplingFBO = FBO.newFBO(1.0f).attachments(4).antialias(FlounderEngine.getDevices().getDisplay().getSamples()).create();
		postProcessingFBO = FBO.newFBO(1.0f).attachments(4).depthBuffer(DepthBufferType.TEXTURE).create();

		deferredShading = new FilterDeferred();
		pipelineDemo = new PipelineDemo();
		pipelinePaused = new PipelinePaused();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY);

		/* Unbinds the FBO. */
		unbindRelevantFBO();

		/* Post rendering. */
		renderPost(FlounderEngine.isGamePaused(), MainGuis.isStartingGame(), FlounderEngine.getScreenBlur());

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);
		cursorRenderer.render(POSITIVE_INFINITY, null);
	}

	private void bindRelevantFBO() {
//		if (OptionsPost.POST_ENABLED) {
		if (FlounderEngine.getDevices().getDisplay().isAntialiasing()) {
			multisamplingFBO.bindFrameBuffer();
		} else {
			postProcessingFBO.bindFrameBuffer();
		}
//		}
	}

	private void unbindRelevantFBO() {
//		if (OptionsPost.POST_ENABLED) {
		if (FlounderEngine.getDevices().getDisplay().isAntialiasing()) {
			multisamplingFBO.unbindFrameBuffer();
			multisamplingFBO.resolveFBO(3, postProcessingFBO);
		} else {
			postProcessingFBO.unbindFrameBuffer();
		}
//		}
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

		/* Renders the world*/

		/* Renders each renderer. */
		entityRenderer.render(clipPlane, camera);
		aabbRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, boolean isStarting, float blurFactor) {
		FBO output = postProcessingFBO;

		if (!isStarting) {
			deferredShading.applyFilter(output.getColourTexture(0), output.getColourTexture(1), output.getColourTexture(2), output.getColourTexture(3));
			output = deferredShading.fbo;

			if (OptionsPost.POST_ENABLED) {
				if (pipelineDemo.willRunDemo()) {
					pipelineDemo.renderPipeline(deferredShading.fbo);
					output = pipelineDemo.getOutput();
				}

				if (isPaused || blurFactor != 0.0f) {
					pipelinePaused.setBlurFactor(blurFactor);
					pipelinePaused.renderPipeline(output);
					output = pipelinePaused.getOutput();
				}
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
		entityRenderer.dispose();
		aabbRenderer.dispose();
		cursorRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		postProcessingFBO.delete();

		deferredShading.dispose();
		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}

