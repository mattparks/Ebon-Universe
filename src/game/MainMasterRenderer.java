package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.engine.options.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.post.piplines.*;
import flounder.textures.fbos.*;
import game.particles.*;
import game.skyboxes.*;
import game.world.*;

public class MainMasterRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);
	private FBO multisamplingFBO;
	private FBO postProcessingFBO;
	private PipelineDemo pipelineDemo;
	private PipelineBloom pipelineBloom;
	private PipelineGaussian pipelineGaussian1;
	private PipelineGaussian pipelineGaussian2;
	private PipelineDepthOfField pipelineDepthOfField;

	private Matrix4f projectionMatrix;
	private SkyboxRenderer skyboxRenderer;
	private ParticleRenderer particleRenderer;
	private FontRenderer fontRenderer;
	private GuiRenderer guiRenderer;

	public MainMasterRenderer() {
	}

	@Override
	public void init() {
		int displayWidth = ManagerDevices.getDisplay().getWidth(), displayHeight = ManagerDevices.getDisplay().getHeight();
		multisamplingFBO = FBO.newFBO(displayWidth, displayHeight).fitToScreen().antialias(ManagerDevices.getDisplay().getSamples()).create();
		postProcessingFBO = FBO.newFBO(displayWidth, displayHeight).fitToScreen().depthBuffer(FBOBuilder.DepthBufferType.TEXTURE).create();
		pipelineDemo = new PipelineDemo();
		pipelineBloom = new PipelineBloom();
		pipelineGaussian1 = new PipelineGaussian(720, 430, false);
		pipelineGaussian2 = new PipelineGaussian(displayWidth, displayHeight, true);
		pipelineDepthOfField = new PipelineDepthOfField();

		projectionMatrix = new Matrix4f();
		skyboxRenderer = new SkyboxRenderer();
		particleRenderer = new ParticleRenderer();
		fontRenderer = new FontRenderer();
		guiRenderer = new GuiRenderer();
	}

	@Override
	public void render() {
		/* Scene rendering. */
		bindRelevantFBO();
		renderScene(POSITIVE_INFINITY);

		/* Post rendering. */
		boolean wireframe = OpenglUtils.isInWireframe();
		OpenglUtils.goWireframe(false);
		renderPost(FlounderEngine.isGamePaused(), FlounderEngine.getScreenBlur());

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);
		OpenglUtils.goWireframe(wireframe);
	}

	private void renderScene(final Vector4f clipPlane) {
		/* Clear and update. */
		OpenglUtils.prepareNewRenderParse(Environment.getFog().getFogColour());
		ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Renders each renderer. */
		// exampleRenderer.render(null, camera);
		skyboxRenderer.render(clipPlane, camera);
		particleRenderer.render(clipPlane, camera);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		multisamplingFBO.delete();
		postProcessingFBO.delete();
		pipelineDemo.dispose();
		pipelineBloom.dispose();
		pipelineGaussian1.dispose();
		pipelineGaussian2.dispose();
		pipelineDepthOfField.dispose();

		skyboxRenderer.dispose();
		particleRenderer.dispose();
		fontRenderer.dispose();
		guiRenderer.dispose();
	}

	private void bindRelevantFBO() {
		if (ManagerDevices.getDisplay().isAntialiasing()) {
			multisamplingFBO.bindFrameBuffer();
		} else {
			postProcessingFBO.bindFrameBuffer();
		}
	}

	private void renderPost(final boolean isPaused, final float blurFactor) {
		if (ManagerDevices.getDisplay().isAntialiasing()) {
			multisamplingFBO.unbindFrameBuffer();
			multisamplingFBO.resolveMultisampledFBO(postProcessingFBO);
		} else {
			postProcessingFBO.unbindFrameBuffer();
		}

		FBO output = postProcessingFBO;

		switch (OptionsGraphics.POST_EFFECT) {
			case 0:
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				pipelineDemo.renderPipeline(postProcessingFBO);
				output = pipelineDemo.getOutput();
				break;
			case 8:
				pipelineBloom.renderPipeline(postProcessingFBO);
				output = pipelineBloom.getOutput();
				break;
			case 9:
				pipelineGaussian1.renderPipeline(postProcessingFBO);
				output = pipelineGaussian1.getOutput();
				break;
			case 10:
				pipelineDepthOfField.renderPipeline(postProcessingFBO);
				output = pipelineDepthOfField.getOutput();
				break;
		}

		if (isPaused) {
			pipelineGaussian1.setBlendSpreadValue(new Vector4f(blurFactor, 1, 0, 1));
			pipelineGaussian1.setScale(6.75f);
			pipelineGaussian1.renderPipeline(output);

			pipelineGaussian2.setBlendSpreadValue(new Vector4f(blurFactor, 1, 0, 1));
			pipelineGaussian2.setScale(4.25f);
			pipelineGaussian2.renderPipeline(pipelineGaussian1.getOutput());
			output = pipelineGaussian2.getOutput();
		}

		output.blitToScreen();
	}
}
