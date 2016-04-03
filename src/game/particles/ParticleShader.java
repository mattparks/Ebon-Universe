package game.particles;

import flounder.resources.*;
import flounder.shaders.*;

public class ParticleShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/particles", "particleVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/particles", "particleFragment.glsl");

	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformVec4 clipPlane = new UniformVec4("clipPlane");
	protected final UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	// protected final UniformFloat transparency = new UniformFloat("transparency");

	protected ParticleShader() {
		super("particle", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(viewMatrix, projectionMatrix, clipPlane, numberOfRows);
	}
}
