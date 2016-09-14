#version 130

//---------IN------------
in vec4 pass_positionRelativeToCam;
in vec3 pass_textureCoords;

//---------UNIFORM------------
layout(binding = 0) uniform samplerCube cubeMap;

//---------OUT------------
out vec4 out_colour;

//---------MAIN------------
void main(void) {
	out_colour = texture(cubeMap, pass_textureCoords);
}