#version 130

out vec4 out_colour;

varying vec4 pass_positionRelativeToCam;
varying vec3 pass_colour;

uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

void main(void) {
    float visibility = clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
    out_colour = vec4(pass_colour, 1.0);
	out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);
}
