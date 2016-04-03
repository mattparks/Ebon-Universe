package game;

import flounder.devices.*;
import flounder.engine.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import game.example.*;
import game.particles.*;
import game.skyboxes.*;

public class MainMasterRenderer extends IRendererMaster {
	private static final Vector4f POSITIVE_INFINITY = new Vector4f(0, 1, 0, Float.POSITIVE_INFINITY);
	private Matrix4f projectionMatrix;
	private ExampleRenderer exampleRenderer;
	private SkyboxRenderer skyboxRenderer;
	private ParticleRenderer particleRenderer;
	private FontRenderer fontRenderer;
	private GuiRenderer guiRenderer;

	public MainMasterRenderer() {
	}

	@Override
	public void init() {
		projectionMatrix = new Matrix4f();
		exampleRenderer = new ExampleRenderer();
		skyboxRenderer = new SkyboxRenderer();
		particleRenderer = new ParticleRenderer();
		fontRenderer = new FontRenderer();
		guiRenderer = new GuiRenderer();
	}

	@Override
	public void render() {
		/* Clear and update. */
		OpenglUtils.prepareNewRenderParse(Environment.getFog().getFogColour());
		ICamera camera = FlounderEngine.getCamera();
		Matrix4f.perspectiveMatrix(camera.getFOV(), ManagerDevices.getDisplay().getAspectRatio(), camera.getNearPlane(), camera.getFarPlane(), projectionMatrix);

		/* Render game. */
		//exampleRenderer.render(null, camera);
		skyboxRenderer.render(POSITIVE_INFINITY, camera);
		particleRenderer.render(POSITIVE_INFINITY, camera);

		boolean wireframe = OpenglUtils.isInWireframe();
		OpenglUtils.goWireframe(false);

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, null);
		fontRenderer.render(POSITIVE_INFINITY, null);
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
		particleRenderer.dispose();
		fontRenderer.dispose();
		guiRenderer.dispose();
	}
}
