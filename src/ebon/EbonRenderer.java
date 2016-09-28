package ebon;

import ebon.celestial.manager.*;
import ebon.celestial.stars.*;
import ebon.entities.*;
import ebon.options.*;
import ebon.post.*;
import ebon.skybox.*;
import ebon.uis.*;
import ebon.world.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.physics.bounding.*;

public class EbonRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final int FBO_ATTACHMENTS = 1;

	private Matrix4f projectionMatrix;

	private SkyboxRenderer skyboxRenderer;
	private EntityRenderer entityRenderer;
	private ParticleRenderer particleRenderer;
	private StarRenderer starRenderer;
	private SunRenderer sunRenderer;
	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
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
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).antialias(FlounderDisplay.getSamples()).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).create();

		this.pipelineDemo = new PipelineDemo();
		this.pipelinePaused = new PipelinePaused();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY, EbonWorld.getFog() != null ? EbonWorld.getFog().getFogColour() : MainSlider.FADE_COLOUR_STARTUP);

		/* Post rendering. */
		renderPost(FlounderEngine.isGamePaused(), FlounderEngine.getScreenBlur());

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
		/* Clear and run. */
		ICamera camera = FlounderEngine.getCamera();
		OpenGlUtils.prepareNewRenderParse(clearColour);
		Matrix4f.perspectiveMatrix(camera.getFOV(), FlounderDisplay.getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Renders each renderer. */
		if (EbonGalaxies.getStars() != null) {
			if (EbonGalaxies.renderStars()) {
				starRenderer.render(clipPlane, camera);
				skyboxRenderer.getSkyboxFBO().setLoaded(false);
			} else {
				if (!skyboxRenderer.getSkyboxFBO().isLoaded()) {
					if (EbonGalaxies.getInSystemStar() != null) {
						camera.getPosition().set(EbonGalaxies.getInSystemStar().getPosition());
					}

					starRenderer.render(clipPlane, camera);
					unbindRelevantFBO();
					skyboxRenderer.getSkyboxFBO().bindFBO();

					for (int face = 0; face < 6; face++) {
						skyboxRenderer.getSkyboxFBO().bindFace(face);
						skyboxRenderer.rotateCamera(camera, face);
						OpenGlUtils.prepareNewRenderParse(clearColour);
						Matrix4f.perspectiveMatrix(SkyboxFBO.CAMERA_FOV, 1.0f, SkyboxFBO.CAMERA_NEAR, SkyboxFBO.CAMERA_FAR, projectionMatrix);
						starRenderer.render(clipPlane, camera);
					}

					skyboxRenderer.getSkyboxFBO().unbindFBO();
					skyboxRenderer.getSkyboxFBO().setLoaded(true);
					bindRelevantFBO();
				} else {
					skyboxRenderer.render(clipPlane, camera);
					sunRenderer.render(clipPlane, camera);
				}
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
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();

		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}

