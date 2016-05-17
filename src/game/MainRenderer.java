package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import game.blocks.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);
	private Matrix4f projectionMatrix;

	private BlockRenderer blockRenderer;
	private AABBRenderer aabbRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.blockRenderer = new BlockRenderer();
		this.aabbRenderer = new AABBRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();
	}

	@Override
	public void render() {
		OpenglUtils.prepareNewRenderParse(1, 1, 1);
		final ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		blockRenderer.render(POSITIVE_INFINITY, camera);
		aabbRenderer.render(POSITIVE_INFINITY, camera);

		fontRenderer.render(POSITIVE_INFINITY, camera);
		guiRenderer.render(POSITIVE_INFINITY, camera);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		blockRenderer.dispose();
		aabbRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();
	}
}

