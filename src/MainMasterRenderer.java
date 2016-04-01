import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import flounder.resources.*;
import flounder.textures.*;

public class MainMasterRenderer extends IRendererMaster {
	private Matrix4f projectionMatrix;
	private FontRenderer fontRenderer;
	private GuiRenderer guiRenderer;

	public MainMasterRenderer() {
	}

	@Override
	public void init() {
		projectionMatrix = new Matrix4f();
		fontRenderer = new FontRenderer();
		guiRenderer = new GuiRenderer();
	}

	@Override
	public void render() {
		/* Render example. */
		renderScene();
		boolean wireframe = OpenglUtils.isInWireframe();
		OpenglUtils.goWireframe(false);

		/* Scene independents. */
		guiRenderer.render(null, null);
		fontRenderer.render(null, null);
		OpenglUtils.goWireframe(wireframe);
	}

	private void renderScene() {
		OpenglUtils.prepareNewRenderParse(0.0f, 0.5f, 0.5f);
		ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getDisplayAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);
	}

	@Override
	public void dispose() {
		fontRenderer.dispose();
		guiRenderer.dispose();
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
