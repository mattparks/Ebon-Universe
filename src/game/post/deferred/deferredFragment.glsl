#version 130

layout(location = 0) out vec4 out_colour;

varying vec2 pass_textureCoords;

layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D positionTexture;
layout(binding = 2) uniform sampler2D normalsTexture;
layout(binding = 3) uniform sampler2D additonalTexture;
layout(binding = 4) uniform sampler2D shadowMapTexture;

const int NUMBER_LIGHTS = 32;

uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform mat4 shadowSpaceMatrix;
uniform float shadowMapSize;
uniform float shadowDistance;

uniform vec3 lightsColour[NUMBER_LIGHTS];
uniform vec3 lightsPosition[NUMBER_LIGHTS];
uniform vec3 lightsAttenuation[NUMBER_LIGHTS];

uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

const float transitionDistance = 85.0;
const float tileAmount = 50.0;
const float shadowDarkness = 0.6;

void main(void) {
    vec4 colour = texture2D(colourTexture, pass_textureCoords);
    vec4 position = texture2D(positionTexture, pass_textureCoords);
    vec4 normal = texture2D(normalsTexture, pass_textureCoords);
    vec4 additonal = texture2D(additonalTexture, pass_textureCoords);
    float specular = additonal.r; // g, b, a

    vec4 positionRelativeToCam = viewMatrix * position;
    vec4 shadowCoords = shadowSpaceMatrix * position;
    float distanceAway = length(positionRelativeToCam.xyz);
    distanceAway = distanceAway - ((shadowDistance * 2.0) - (transitionDistance));
    distanceAway = distanceAway / transitionDistance;
    shadowCoords.w = clamp(1.0 - distanceAway, 0.0, 1.0);

	float shadowTexelSize = 1.0 / shadowMapSize;
	float shadowHalfw = shadowTexelSize * 0.5;
	float shadowTotal = 0.0;
	float shadowValue = 0.0;
	float shadowShadeFactor;
	shadowValue = texture(shadowMapTexture, shadowCoords.xy + vec2(0 + shadowHalfw, 0 + shadowHalfw)).r;

    if (shadowCoords.x > 0.0 && shadowCoords.x < 1.0 && shadowCoords.y > 0.0 && shadowCoords.y < 1.0 && shadowCoords.z > 0.0 && shadowCoords.z < 1.0) {
        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowValue = texture(shadowMapTexture, shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, 0 + shadowHalfw)).r;

        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowValue = texture(shadowMapTexture, shadowCoords.xy + vec2(0 + shadowHalfw, shadowTexelSize + shadowHalfw)).r;

        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowValue = texture(shadowMapTexture, shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, shadowTexelSize + shadowHalfw)).r;

        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowShadeFactor = 1.0 - (shadowTotal / 4.0);
    } else {
        shadowShadeFactor = 1.0;
    }

    float visibility = clamp(exp(-pow((length(positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);

    out_colour = vec4((colour * shadowShadeFactor).xyz, colour.a);
    out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);

    /*if (pass_textureCoords.x < 0.499 && pass_textureCoords.y < 0.499) {
        // Quad 3
	    out_colour = vec4(colour.rbg, 1.0);
    } else if (pass_textureCoords.x > 0.50 && pass_textureCoords.y < 0.499) {
        // Quad 4
	    out_colour = vec4(position.rbg, 1.0);
    } else if (pass_textureCoords.x < 0.499 && pass_textureCoords.y > 0.50) {
        // Quad 1
	    out_colour = vec4(normal.rbg, 1.0);
    } else if (pass_textureCoords.x > 0.50 && pass_textureCoords.y > 0.50) {
        // Quad 2
	    out_colour = vec4(additonal.rbg, 1.0);
    } else {
        // Border Area.
        out_colour = vec4(0.0, 0.5, 1.0, 1.0);
    }*/
}