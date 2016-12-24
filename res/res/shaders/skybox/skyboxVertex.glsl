#version 130

//---------IN------------
layout(location = 0) in vec3 in_position;

//---------UNIFORM------------
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

//---------OUT------------
out float pass_height;

//---------MAIN------------
void main(void) {
	gl_Position = projectionMatrix * viewMatrix * vec4(in_position, 1.0);
 	pass_height = in_position.y;
}
