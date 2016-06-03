package game.skybox;

import flounder.resources.*;
import flounder.shaders.*;

public class SkyboxShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/skybox", "skyboxVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/skybox", "skyboxFragment.glsl");

	protected UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected UniformFloat blendFactor = new UniformFloat("blendFactor");
	protected UniformFloat lowerFogLimit = new UniformFloat("lowerFogLimit");
	protected UniformFloat upperFogLimit = new UniformFloat("upperFogLimit");
	protected UniformVec3 fogColour = new UniformVec3("fogColour");

	protected SkyboxShader() {
		super("skybox", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, blendFactor, lowerFogLimit, upperFogLimit, fogColour);
	}
}
