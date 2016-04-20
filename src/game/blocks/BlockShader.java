package game.blocks;

import flounder.resources.*;
import flounder.shaders.*;

public class BlockShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/blocks", "blocksVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/blocks", "blocksFragment.glsl");

	protected final UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformMat4 shadowSpaceMatrix = new UniformMat4("shadowSpaceMatrix");
	protected final UniformFloat shadowDistance = new UniformFloat("shadowDistance");
	protected final UniformFloat shadowMapSize = new UniformFloat("shadowMapSize");
	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformVec4 clipPlane = new UniformVec4("clipPlane");
	protected final UniformVec3 colour = new UniformVec3("colour");
	protected final UniformVec3 fogColour = new UniformVec3("fogColour");
	protected final UniformFloat fogDensity = new UniformFloat("fogDensity");
	protected final UniformFloat fogGradient = new UniformFloat("fogGradient");

	public BlockShader() {
		super("blocks", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, shadowSpaceMatrix, shadowDistance, shadowMapSize, viewMatrix, clipPlane, colour, fogColour, fogDensity, fogGradient);
	}
}
