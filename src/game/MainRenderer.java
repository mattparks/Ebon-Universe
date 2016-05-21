package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

public class MainRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);
	private Matrix4f projectionMatrix;

	private AABBRenderer aabbRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.aabbRenderer = new AABBRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();
	}

	@Override
	public void render() {
		OpenglUtils.prepareNewRenderParse(0.125f, 0.125f, 0.125f);
		final ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

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
		aabbRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();
	}
}

