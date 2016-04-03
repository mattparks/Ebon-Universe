package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import game.example.*;
import game.skyboxes.*;

public class MainMasterRenderer extends IRendererMaster {
	private Matrix4f projectionMatrix;
	private ExampleRenderer exampleRenderer;
	private SkyboxRenderer skyboxRenderer;
	private FontRenderer fontRenderer;
	private GuiRenderer guiRenderer;

	public MainMasterRenderer() {
	}

	@Override
	public void init() {
		projectionMatrix = new Matrix4f();
		exampleRenderer = new ExampleRenderer();
		skyboxRenderer = new SkyboxRenderer();
		fontRenderer = new FontRenderer();
		guiRenderer = new GuiRenderer();
	}

	@Override
	public void render() {
		/* Clear and update. */
		OpenglUtils.prepareNewRenderParse(0.25f, 0.5f, 0.75f);
		ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Render game. */
		//exampleRenderer.render(null, camera);
		skyboxRenderer.render(null, camera);

		boolean wireframe = OpenglUtils.isInWireframe();
		OpenglUtils.goWireframe(false);

		/* Scene independents. */
		guiRenderer.render(null, null);
		fontRenderer.render(null, null);
		OpenglUtils.goWireframe(wireframe);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public void dispose() {
		exampleRenderer.dispose();
		skyboxRenderer.dispose();
		fontRenderer.dispose();
		guiRenderer.dispose();
	}
}
