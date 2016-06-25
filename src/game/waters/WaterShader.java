package game.waters;

import flounder.resources.*;
import flounder.shaders.*;

public class WaterShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/waters", "waterVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/waters", "waterFragment.glsl");

	protected UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected UniformFloat nearPlane = new UniformFloat("nearPlane");
	protected UniformFloat farPlane = new UniformFloat("farPlane");
	protected UniformVec3 lightPosition = new UniformVec3("lightPosition");
	protected UniformVec3 lightColour = new UniformVec3("lightColour");
	protected UniformFloat moveFactor = new UniformFloat("moveFactor");
	protected UniformVec3 fogColour = new UniformVec3("fogColour");
	protected UniformFloat fogDensity = new UniformFloat("fogDensity");
	protected UniformFloat fogGradient = new UniformFloat("fogGradient");

	protected UniformVec3 colourAdditive = new UniformVec3("colourAdditive");
	protected UniformFloat colourMix = new UniformFloat("colourMix");
	protected UniformFloat textureTiling = new UniformFloat("textureTiling");
	protected UniformFloat waveStrength = new UniformFloat("waveStrength");
	protected UniformFloat normalDampener = new UniformFloat("normalDampener");
	protected UniformFloat dropOffDepth = new UniformFloat("dropOffDepth");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");

	protected WaterShader() {
		super("waters", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, viewMatrix, nearPlane, farPlane, lightPosition, lightColour, moveFactor, fogColour, fogDensity, fogGradient, colourAdditive, colourMix, textureTiling, waveStrength, normalDampener, dropOffDepth, reflectivity, shineDamper);
	}
}
