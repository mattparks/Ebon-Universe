#version 130

//---------IN------------
in vec2 pass_textureCoords;
in vec3 pass_colourOffset;

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D particleTexture;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------MAIN------------
void main(void) {
	out_colour = texture(particleTexture, pass_textureCoords) + vec4(pass_colourOffset, 0.0);
}
