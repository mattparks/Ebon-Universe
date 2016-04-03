package game.example;

import flounder.resources.*;
import flounder.shaders.*;

public class ExampleShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("game/example", "exampleVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("game/example", "exampleFragment.glsl");

	protected ExampleShader() {
		super("example", VERTEX_SHADER, FRAGMENT_SHADER);
	}
}
