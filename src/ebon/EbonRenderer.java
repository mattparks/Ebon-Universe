package ebon;

import ebon.cameras.*;
import ebon.entities.*;
import ebon.options.*;
import ebon.particles.*;
import ebon.post.*;
import ebon.skybox.*;
import ebon.uis.*;
import ebon.universe.galaxies.*;
import ebon.universe.orbits.*;
import ebon.universe.stars.*;
import ebon.world.*;
import flounder.camera.*;
import flounder.devices.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.renderer.*;

public class EbonRenderer extends IExtension implements IRendererMaster {
	public static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	public static final int FBO_ATTACHMENTS = 1;

	private SkyboxRenderer skyboxRenderer;
	private EntityRenderer entityRenderer;
	private ParticleRenderer particleRenderer;
	private StarRenderer starRenderer;
	private SunRenderer sunRenderer;
	private OrbitsRenderer orbitsRenderer;
	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	private PipelineDemo pipelineDemo;
	private PipelinePaused pipelinePaused;

	@Override
	public void init() {
		this.skyboxRenderer = new SkyboxRenderer();
		this.entityRenderer = new EntityRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.starRenderer = new StarRenderer();
		this.sunRenderer = new SunRenderer();
		this.orbitsRenderer = new OrbitsRenderer();
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).antialias(
				Ebon.configMain.getIntWithDefault("msaa_samples", 4, () -> multisamplingFBO.getSamples())
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
		renderScene(POSITIVE_INFINITY, EbonWorld.getFog() != null ? EbonWorld.getFog().getFogColour() : MainSlider.FADE_COLOUR_STARTUP);

		/* Post rendering. */
		renderPost(FlounderCamera.isGamePaused(), FlounderGuis.getGuiMaster().getBlurFactor());

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

		/* Renders each renderer. */
		if (EbonGalaxies.getStars() != null) {
			if (EbonGalaxies.renderStars()) {
				starRenderer.render(clipPlane, camera);
				skyboxRenderer.getSkyboxFBO().setLoaded(false);
			} else {
				if (!skyboxRenderer.getSkyboxFBO().isLoaded()) {
					ICamera previousCamera = camera;
					camera = new CameraCubeMap();
					camera.init();
					FlounderEngine.setCamera(camera);

					if (EbonGalaxies.getInSystemStar() != null) {
						((CameraCubeMap) camera).setCentre(EbonGalaxies.getInSystemStar().getPosition());
					}

					starRenderer.render(clipPlane, camera);
					unbindRelevantFBO();
					skyboxRenderer.getSkyboxFBO().bindFBO();

					for (int face = 0; face < 6; face++) {
						skyboxRenderer.getSkyboxFBO().bindFace(face);
						((CameraCubeMap) camera).switchToFace(face);
						OpenGlUtils.prepareNewRenderParse(clearColour);
						starRenderer.render(clipPlane, camera);
					}

					skyboxRenderer.getSkyboxFBO().unbindFBO();
					skyboxRenderer.getSkyboxFBO().setLoaded(true);
					bindRelevantFBO();

					camera = previousCamera;
					FlounderEngine.setCamera(camera);
				} else {
					skyboxRenderer.render(clipPlane, camera);
					sunRenderer.render(clipPlane, camera);
				}
			}
		}

		entityRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
		orbitsRenderer.render(clipPlane, camera);
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
		skyboxRenderer.dispose();
		entityRenderer.dispose();
		particleRenderer.dispose();
		sunRenderer.dispose();
		starRenderer.dispose();
		orbitsRenderer.dispose();
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();

		pipelineDemo.dispose();
		pipelinePaused.dispose();
	}
}

