#version 130

layout(location = 0) out vec4 out_colour;

varying vec2 pass_textureCoords;

layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D depthTexture;
layout(binding = 2) uniform sampler2D normalsTexture;
layout(binding = 3) uniform sampler2D specularTexture;

const int NUMBER_LIGHTS = 32;

uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightsColour[NUMBER_LIGHTS];
uniform vec3 lightsPosition[NUMBER_LIGHTS];
uniform vec3 lightsAttenuation[NUMBER_LIGHTS];

uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

float visibility(vec4 positionRelativeToCam) {
    return clamp(exp(-pow((length(positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

void main(void) {
    vec4 colour = texture2D(colourTexture, pass_textureCoords);
    vec4 depth = texture2D(depthTexture, pass_textureCoords);
    vec4 normal = texture2D(normalsTexture, pass_textureCoords);
    vec4 specular = texture2D(specularTexture, pass_textureCoords);
    vec4 positionRelativeToCam = viewMatrix * depth;

    out_colour = colour;
    out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility(positionRelativeToCam));
    out_colour.a = colour.a;

    /*if (pass_textureCoords.x < 0.499 && pass_textureCoords.y < 0.499) {
        // Quad 3
	    out_colour = vec4(colour.rgb, 1.0);
    } else if (pass_textureCoords.x > 0.50 && pass_textureCoords.y < 0.499) {
        // Quad 4
	    out_colour = vec4(depth.rgb, 1.0);
    } else if (pass_textureCoords.x < 0.499 && pass_textureCoords.y > 0.50) {
        // Quad 1
	    out_colour = vec4(normal.rgb, 1.0);
    } else if (pass_textureCoords.x > 0.50 && pass_textureCoords.y > 0.50) {
        // Quad 2
	    out_colour = vec4(specular.rgb, 1.0);
    } else {
        // Border Area.
        out_colour = vec4(0.0, 0.5, 1.0, 1.0);
    }*/
}