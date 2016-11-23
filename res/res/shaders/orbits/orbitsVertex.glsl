#version 130

//---------IN------------
layout(location = 0) in vec2 position;

//---------UNIFORM------------
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform mat4 modelMatrix;

//---------OUT------------

//---------MAIN------------
void main(void) {
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	gl_ClipDistance[0] = dot(modelMatrix * vec4(position, 0.0, 1.0), clipPlane);
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}
