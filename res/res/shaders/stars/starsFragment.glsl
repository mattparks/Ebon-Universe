#version 130

layout(location = 0) out vec4 out_colour;

varying vec2 pass_textureCoords;
varying vec3 pass_colourOffset;

layout(binding = 0) uniform sampler2D particleTexture;

void main(void) {
	out_colour = texture(particleTexture, pass_textureCoords) + vec4(pass_colourOffset, 0.0);
}
