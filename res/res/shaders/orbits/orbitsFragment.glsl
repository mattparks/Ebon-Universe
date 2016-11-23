#version 130

//---------IN------------

//---------UNIFORM------------
uniform vec3 colour;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------MAIN------------
void main(void) {
	out_colour = vec4(colour, 1.0);
}
