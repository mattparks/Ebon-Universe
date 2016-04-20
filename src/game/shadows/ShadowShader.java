package game.shadows;

import flounder.resources.*;
import flounder.shaders.*;

public class ShadowShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/shadows", "shadowVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/shadows", "shadowFragment.glsl");

	protected UniformMat4 mvpMatrix = new UniformMat4("mvpMatrix");

	protected ShadowShader() {
		super("shadow", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(mvpMatrix);
	}
}
