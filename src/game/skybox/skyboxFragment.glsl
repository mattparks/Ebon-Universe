#version 130

out vec4 out_colour;

varying vec3 pass_textureCoords;

uniform vec3 fogColour;
uniform float lowerFogLimit;
uniform float upperFogLimit;

void main(void) {
	vec4 textureColour = vec4(fogColour, 1.0);
	out_colour = textureColour;
	out_colour = mix(vec4(fogColour, 1.0), out_colour, clamp((pass_textureCoords.y - lowerFogLimit) / (upperFogLimit - lowerFogLimit), 0.0, 1.0));
}
