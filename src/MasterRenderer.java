import blocks.*;
import flounder.devices.*;
import flounder.engine.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.physics.*;

public class MasterRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);
	private Matrix4f projectionMatrix;

	private BlockRenderer blockRenderer;
	private AABBRenderer aabbRenderer;

	@Override
	public void init() {
		this.projectionMatrix = new Matrix4f();

		this.blockRenderer = new BlockRenderer();
		this.aabbRenderer = new AABBRenderer();
	}

	@Override
	public void render() {
		OpenglUtils.prepareNewRenderParse(1, 1, 1);
		final ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);
		blockRenderer.render(POSITIVE_INFINITY, camera);
		aabbRenderer.render(POSITIVE_INFINITY, camera);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		blockRenderer.dispose();
		aabbRenderer.dispose();
	}
}
