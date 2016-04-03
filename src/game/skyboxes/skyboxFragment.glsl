#version 130

out vec4 out_colour;

varying vec3 textureCoords;

layout(binding = 0) uniform samplerCube cubeMap;

uniform vec3 fogColour;
uniform float lowerFogLimit;
uniform float upperFogLimit;

void main(void) {
	vec4 textureColour = texture(cubeMap, textureCoords);
	// out_colour = mix(vec4(fogColour, 1.0), textureColour, clamp((textureCoords.y - lowerFogLimit) / (upperFogLimit - lowerFogLimit), 0.0, 1.0));
	out_colour = textureColour;
}
