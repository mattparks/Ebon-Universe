#version 130

layout(location = 0) in vec3 position;

varying vec3 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
//    gl_Position = vec4(position.x / 300, position.y / 300, 0.0f, 1.0);

	textureCoords = position;
}
