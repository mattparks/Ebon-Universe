package game.planets;

import flounder.resources.*;
import flounder.shaders.*;

public class PlanetShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/planets", "planetVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/planets", "planetFragment.glsl");

	protected final UniformMat4 modelMatrix = new UniformMat4("modelMatrix");
	protected final UniformMat4 projectionMatrix = new UniformMat4("projectionMatrix");
	protected final UniformMat4 viewMatrix = new UniformMat4("viewMatrix");
	protected final UniformVec3 colour = new UniformVec3("colour");

	protected PlanetShader() {
		super("planet", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(modelMatrix, projectionMatrix, viewMatrix, colour);
	}
}
