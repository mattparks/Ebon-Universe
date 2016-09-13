#version 130

in vec3 pass_textureCoords;

out vec4 out_colour;

layout(binding = 0) uniform samplerCube cubeMap;

void main(void) {
	vec4 textureColour = texture(cubeMap, pass_textureCoords);
	out_colour = textureColour;
}