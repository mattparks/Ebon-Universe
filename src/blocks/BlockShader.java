package blocks;

import flounder.resources.*;
import flounder.shaders.*;

public class BlockShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("blocks", "blocksVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("blocks", "blocksFragment.glsl");

	protected final UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformVec4 clipPlane = new UniformVec4("clipPlane");

	public BlockShader() {
		super("blocks", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, viewMatrix, clipPlane);
	}
}
