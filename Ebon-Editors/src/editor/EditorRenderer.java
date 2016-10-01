package editor;

import ebon.entities.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.entrance.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.particles.*;
import flounder.physics.bounding.*;

public class EditorRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);
	private static final int FBO_ATTACHMENTS = 1;

	private Matrix4f projectionMatrix;

	private EntityRenderer entityRenderer;
	private ParticleRenderer particleRenderer;
	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.entityRenderer = new EntityRenderer();
		this.particleRenderer = new ParticleRenderer();
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		// Diffuse, Depth, Normals
		this.multisamplingFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).antialias(FlounderDisplay.getSamples()).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).attachments(FBO_ATTACHMENTS).depthBuffer(DepthBufferType.TEXTURE).create();
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
		/* Clear and run. */
		ICamera camera = FlounderEngine.getCamera();
		OpenGlUtils.prepareNewRenderParse(1.0f, 1.0f, 1.0f);
		Matrix4f.perspectiveMatrix(camera.getFOV(), FlounderDisplay.getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Renders each renderer. */
		entityRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
		boundingRenderer.render(clipPlane, camera);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		entityRenderer.dispose();
		particleRenderer.dispose();
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();
	}
}

