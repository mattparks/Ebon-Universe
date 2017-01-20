package editors.editor;

import ebon.entities.*;
import ebon.particles.*;
import ebon.skybox.*;
import flounder.camera.*;
import flounder.devices.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.renderer.*;

public class EditorRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);

	private SkyboxRenderer skyboxRenderer;
	private EntitiesRenderer entitiesRenderer;
	private ParticleRenderer particleRenderer;
	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	public EditorRenderer() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderDisplay.class);
	}

	@Override
	public void init() {
		this.skyboxRenderer = new SkyboxRenderer();
		this.entitiesRenderer = new EntitiesRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		this.multisamplingFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).antialias(FlounderDisplay.getSamples()).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).create();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY);

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	@Override
	public void profile() {

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

		nonsampledFBO.blitToScreen();
	}

	private void renderScene(Vector4f clipPlane) {
		/* Clear and update. */
		ICamera camera = FlounderCamera.getCamera();
		OpenGlUtils.prepareNewRenderParse(1.0f, 1.0f, 1.0f);

		/* Renders each renderer. */
		skyboxRenderer.render(clipPlane, camera);
		entitiesRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
		boundingRenderer.render(clipPlane, camera);
	}

	@Override
	public void dispose() {
		skyboxRenderer.dispose();
		entitiesRenderer.dispose();
		particleRenderer.dispose();
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();
	}

	@Override
	public boolean isActive() {
		return true;
	}
}

