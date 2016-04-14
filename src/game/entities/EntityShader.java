package game.entities;

import flounder.resources.*;
import flounder.shaders.*;

public class EntityShader extends ShaderProgram {
	public static final int MAX_LIGHTS = 4;

	private static final MyFile VERTEX_SHADER = new MyFile("engine/entities", "entityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("engine/entities", "entityFragment.glsl");

	protected final UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformVec4 clipPlane = new UniformVec4("clipPlane");
	protected final UniformVec3[] lightPosition = new UniformVec3[MAX_LIGHTS];
	protected final UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	protected final UniformFloat atlasRows = new UniformFloat("atlasRows");
	protected final UniformVec2 atlasOffset = new UniformVec2("atlasOffset");

	protected final UniformVec3 colourMod = new UniformVec3("colourMod");
	protected final UniformVec3[] lightColour = new UniformVec3[MAX_LIGHTS];
	protected final UniformVec3[] attenuation = new UniformVec3[MAX_LIGHTS];
	protected final UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected final UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected final UniformFloat transparency = new UniformFloat("transparency");
	protected final UniformBoolean useNormalMap = new UniformBoolean("useNormalMap");
	protected final UniformVec3 fogColour = new UniformVec3("fogColour");
	protected final UniformFloat fogDensity = new UniformFloat("fogDensity");
	protected final UniformFloat fogGradient = new UniformFloat("fogGradient");

	public EntityShader() {
		super("entities", new MyFile("game/entities", "entityVertex.glsl"), new MyFile("game/entities", "entityFragment.glsl"));
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, viewMatrix, clipPlane, useFakeLighting, atlasRows, atlasOffset, colourMod, shineDamper, reflectivity, transparency, useNormalMap, fogColour, fogDensity, fogGradient);

		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightPosition[i] = new UniformVec3("lightPosition[" + i + "]");
			lightColour[i] = new UniformVec3("lightColour[" + i + "]");
			attenuation[i] = new UniformVec3("attenuation[" + i + "]");
			super.storeAllUniformLocations(lightPosition[i], lightColour[i], attenuation[i]);
		}
	}
}
