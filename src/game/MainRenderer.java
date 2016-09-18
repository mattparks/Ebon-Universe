package game;

import flounder.engine.*;
import flounder.engine.implementation.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.physics.renderer.*;
import game.celestial.dust.*;
import game.celestial.stars.*;
import game.entities.*;
import game.options.*;
import game.post.*;
import game.skybox.*;
import game.uis.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final int FBO_ATTACHMENTS = 1;

	private Matrix4f projectionMatrix;

	private SkyboxRenderer skyboxRenderer;
	private EntityRenderer entityRenderer;
	private ParticleRenderer particleRenderer;
	private StarRenderer starRenderer;
	private SunRenderer sunRenderer;
	private DustRenderer dustRenderer;
	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private GuiRenderer cursorRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.skyboxRenderer = new SkyboxRenderer();
		this.entityRenderer = new EntityRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.starRenderer = new StarRenderer();
		this.sunRenderer = new SunRenderer();
		this.dustRenderer = new DustRenderer();
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.GUI);
		this.cursorRenderer = new GuiRenderer(GuiRenderer.GuiRenderType.CURSOR);
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).antialias(FlounderEngine.getDevices().getDisplay().getSamples()).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).create();

		this.pipelineDemo = new PipelineDemo();
		this.pipelinePaused = new PipelinePaused();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY, Environment.getFog() != null ? Environment.getFog().getFogColour() : MainSlider.FADE_COLOUR_STARTUP);

		/* Post rendering. */
		renderPost(FlounderEngine.isGamePaused(), FlounderEngine.getScreenBlur());

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

	private void renderScene(Vector4f clipPlane, Colour clearColour) {
		/* Clear and update. */
		ICamera camera = FlounderEngine.getCamera();
		OpenGlUtils.prepareNewRenderParse(clearColour);
		Matrix4f.perspectiveMatrix(camera.getFOV(), FlounderEngine.getDevices().getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Renders each renderer. */
		if (Environment.getGalaxyManager().renderStars()) {
			starRenderer.render(clipPlane, camera);
			dustRenderer.render(clipPlane, camera);
			skyboxRenderer.getSkyboxFBO().setLoaded(false);
		} else {
			if (!skyboxRenderer.getSkyboxFBO().isLoaded()) {
				if (Environment.getGalaxyManager().getInSystemStar() != null) {
					camera.getPosition().set(Environment.getGalaxyManager().getInSystemStar().getPosition());
				}

				starRenderer.render(clipPlane, camera);
				dustRenderer.render(clipPlane, camera);
				unbindRelevantFBO();
				skyboxRenderer.getSkyboxFBO().bindFBO();

				for (int face = 0; face < 6; face++) {
					skyboxRenderer.getSkyboxFBO().bindFace(face);
					skyboxRenderer.rotateCamera(camera, face);
					OpenGlUtils.prepareNewRenderParse(clearColour);
					Matrix4f.perspectiveMatrix(SkyboxFBO.CAMERA_FOV, 1.0f, SkyboxFBO.CAMERA_NEAR, SkyboxFBO.CAMERA_FAR, projectionMatrix);
					starRenderer.render(clipPlane, camera);
					dustRenderer.render(clipPlane, camera);
				}

				skyboxRenderer.getSkyboxFBO().unbindFBO();
				skyboxRenderer.getSkyboxFBO().setLoaded(true);
				bindRelevantFBO();
			} else {
				skyboxRenderer.render(clipPlane, camera);
				sunRenderer.render(clipPlane, camera);
			}
		}

		entityRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
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
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		skyboxRenderer.dispose();
		entityRenderer.dispose();
		particleRenderer.dispose();
		sunRenderer.dispose();
		starRenderer.dispose();
		dustRenderer.dispose();
		boundingRenderer.dispose();
		cursorRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();

		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}

