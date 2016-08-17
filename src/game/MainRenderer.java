package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.physics.renderer.*;
import game.entities.*;
import game.options.*;
import game.post.*;
import game.post.deferred.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final int FBO_ATTACHMENTS = 4;

	private Matrix4f projectionMatrix;

	private EntityRenderer entityRenderer;
	private ParticleRenderer particleRenderer;
	private AABBRenderer aabbRenderer;
	private GuiRenderer guiRenderer;
	private GuiRenderer cursorRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	private FilterDeferred filterDeferred;
	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.entityRenderer = new EntityRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.aabbRenderer = new AABBRenderer();
		this.guiRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.GUI);
		this.cursorRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.CURSOR);
		this.fontRenderer = new FontRenderer();

		// Diffuse, Position, Normals, Additonal (Specular, G, B, A)
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).antialias(FlounderEngine.getDevices().getDisplay().getSamples()).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).create();

		this.filterDeferred = new FilterDeferred();
		this.pipelineDemo = new PipelineDemo();
		this.pipelinePaused = new PipelinePaused();
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
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);
		cursorRenderer.render(POSITIVE_INFINITY, null);

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	private void bindRelevantFBO() {
		if (FlounderEngine.getDevices().getDisplay().isAntialiasing()) {
			multisamplingFBO.bindFrameBuffer();
		} else {
			nonsampledFBO.bindFrameBuffer();
		}
	}

	private void unbindRelevantFBO() {
		if (FlounderEngine.getDevices().getDisplay().isAntialiasing()) {
			multisamplingFBO.unbindFrameBuffer();
			multisamplingFBO.resolveFBO(nonsampledFBO);
		} else {
			nonsampledFBO.unbindFrameBuffer();
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
		entityRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
		aabbRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, boolean isStarting, float blurFactor) {
		FBO output = nonsampledFBO;

		if (!isStarting) {
			filterDeferred.applyFilter(output.getColourTexture(0), output.getColourTexture(1), output.getColourTexture(2), output.getColourTexture(3));
			output = filterDeferred.fbo;

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
		particleRenderer.dispose();
		aabbRenderer.dispose();
		cursorRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();

		filterDeferred.dispose();
		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}

