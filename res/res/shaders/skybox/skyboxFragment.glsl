#version 130

//---------INCLUDES------------
#include "maths.glsl"

//---------IN------------
in float pass_height;

//---------UNIFORM------------

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------CONSTANT------------
const vec4 colour1 = vec4(0.2, 0.5, 0.9, 1.0);
const vec4 colour2 = vec4(0.2, 0.7, 0.7, 1.0);

//---------MAIN------------
void main(void) {
	float fadeFactor = 1.0 - smoothlyStep(-50.0, 70.0, pass_height);
	out_colour = mix(colour2, colour1, fadeFactor);
}