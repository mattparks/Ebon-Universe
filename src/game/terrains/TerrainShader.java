package game.terrains;

import flounder.resources.*;
import flounder.shaders.*;

public class TerrainShader extends ShaderProgram {
	public static final int MAX_LIGHTS = 4;

	private static final MyFile VERTEX_SHADER = new MyFile("game/terrains", "terrainVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/terrains", "terrainFragment.glsl");

	protected UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected UniformVec4 clipPlane = new UniformVec4("clipPlane");
	protected UniformVec3[] lightPosition = new UniformVec3[MAX_LIGHTS];
	protected UniformVec3[] lightColour = new UniformVec3[MAX_LIGHTS];
	protected UniformVec3[] attenuation = new UniformVec3[MAX_LIGHTS];
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformVec3 fogColour = new UniformVec3("fogColour");
	protected UniformFloat fogDensity = new UniformFloat("fogDensity");
	protected UniformFloat fogGradient = new UniformFloat("fogGradient");

	protected TerrainShader() {
		super("terrain", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, viewMatrix, clipPlane, shineDamper, reflectivity, fogColour, fogDensity, fogGradient);

		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightPosition[i] = new UniformVec3("lightPosition[" + i + "]");
			lightColour[i] = new UniformVec3("lightColour[" + i + "]");
			attenuation[i] = new UniformVec3("attenuation[" + i + "]");
			super.storeAllUniformLocations(lightPosition[i], lightColour[i], attenuation[i]);
		}
	}
}
