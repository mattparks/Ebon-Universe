package game.blocks;

import flounder.resources.*;
import flounder.shaders.*;

public class BlockShader extends ShaderProgram {
//	public static final int MAX_LIGHTS = 4;

	private static final MyFile VERTEX_SHADER = new MyFile("game/blocks", "blocksVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/blocks", "blocksFragment.glsl");

	protected final UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformVec4 clipPlane = new UniformVec4("clipPlane");
	protected final UniformVec3 colour = new UniformVec3("colour");

//	protected final UniformVec3[] lightPosition = new UniformVec3[MAX_LIGHTS];
//	protected final UniformVec3[] lightColour = new UniformVec3[MAX_LIGHTS];
//	protected final UniformVec3[] attenuation = new UniformVec3[MAX_LIGHTS];
//	protected final UniformFloat shineDamper = new UniformFloat("shineDamper");
//	protected final UniformFloat reflectivity = new UniformFloat("reflectivity");
//	protected final UniformVec3 fogColour = new UniformVec3("fogColour");
//	protected final UniformFloat fogDensity = new UniformFloat("fogDensity");
//	protected final UniformFloat fogGradient = new UniformFloat("fogGradient");

	public BlockShader() {
		super("blocks", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, viewMatrix, clipPlane, colour);//, shineDamper, reflectivity, fogColour, fogDensity, fogGradient);

	//	for (int i = 0; i < MAX_LIGHTS; i++) {
	//		lightPosition[i] = new UniformVec3("lightPosition[" + i + "]");
	//		lightColour[i] = new UniformVec3("lightColour[" + i + "]");
	//		attenuation[i] = new UniformVec3("attenuation[" + i + "]");
	//		super.storeAllUniformLocations(lightPosition[i], lightColour[i], attenuation[i]);
	//	}
	}
}
