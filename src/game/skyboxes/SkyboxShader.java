package game.skyboxes;

import flounder.resources.*;
import flounder.shaders.*;

public class SkyboxShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/skyboxes", "skyboxVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/skyboxes", "skyboxFragment.glsl");

	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformFloat blendFactor = new UniformFloat("blendFactor");
	protected final UniformFloat lowerFogLimit = new UniformFloat("lowerFogLimit");
	protected final UniformFloat upperFogLimit = new UniformFloat("upperFogLimit");
	protected final UniformVec3 fogColour = new UniformVec3("fogColour");

	protected SkyboxShader() {
		super("skybox", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, blendFactor, lowerFogLimit, upperFogLimit, fogColour);
	}
}