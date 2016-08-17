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
import game.shadows.*;
import game.skybox.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);

	private Matrix4f projectionMatrix;

	private ShadowRenderer shadowRenderer;
	private EntityRenderer entityRenderer;
	private SkyboxRenderer skyboxRenderer;
	private ParticleRenderer particleRenderer;
	private AABBRenderer aabbRenderer;
	private GuiRenderer guiRenderer;
	private GuiRenderer cursorRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;

	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.shadowRenderer = new ShadowRenderer();
		this.entityRenderer = new EntityRenderer();
		this.skyboxRenderer = new SkyboxRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.aabbRenderer = new AABBRenderer();
		this.guiRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.GUI);
		this.cursorRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.CURSOR);
		this.fontRenderer = new FontRenderer();

		// Diffuse, Position, Normals, Additonal (Specular, G, B, A)
		multisamplingFBO = FBO.newFBO(1.0f).attachments(4).depthBuffer(DepthBufferType.TEXTURE).create(); // .antialias(FlounderEngine.getDevices().getDisplay().getSamples())

		pipelineDemo = new PipelineDemo();
		pipelinePaused = new PipelinePaused();
	}

	@Override
	public void render() {
		/* Shadow rendering. */
		shadowRenderer.render(POSITIVE_INFINITY, FlounderEngine.getCamera());

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
		multisamplingFBO.bindFrameBuffer();
	}

	private void unbindRelevantFBO() {
		multisamplingFBO.unbindFrameBuffer();
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
		skyboxRenderer.render(clipPlane, camera);
		entityRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
		aabbRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, boolean isStarting, float blurFactor) {
		FBO output = multisamplingFBO;

		if (!isStarting) {
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

	/**
	 * @return Returns the shadow map renderer.
	 */
	public ShadowRenderer getShadowMapRenderer() {
		return shadowRenderer;
	}

	@Override
	public void dispose() {
		shadowRenderer.dispose();
		skyboxRenderer.dispose();
		entityRenderer.dispose();
		particleRenderer.dispose();
		aabbRenderer.dispose();
		cursorRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();

		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}

