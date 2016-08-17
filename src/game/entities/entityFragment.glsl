#version 130

layout(location = 0) out vec4 out_colour;

layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D normalMapTexture;
layout(binding = 2) uniform sampler2D shadowMap;

varying vec2 textureCoords;
varying vec4 entityPosition;
varying vec3 surfaceNormal;
varying vec4 shadowCoords;
varying vec3 toCameraVector;
varying vec4 positionRelativeToCam;

uniform bool useNormalMap;
uniform float transparency;
uniform float shadowMapSize;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

const float tileAmount = 50.0;
const float shadowDarkness = 0.6;

float shadow(void) {
    const float bias = 0.0001;
	float shadowTexelSize = 1.0 / shadowMapSize;
	float shadowHalfw = shadowTexelSize * 0.5;
	float shadowTotal = 0.0;
	float shadowValue = 0.0;
	float shadowShadeFactor;
	shadowValue = texture(shadowMap, shadowCoords.xy + vec2(0 + shadowHalfw, 0 + shadowHalfw)).r + bias;

    if (shadowCoords.x > 0.0 && shadowCoords.x < 1.0 && shadowCoords.y > 0.0 && shadowCoords.y < 1.0 && shadowCoords.z > 0.0 && shadowCoords.z < 1.0) {
        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowValue = texture(shadowMap, shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, 0 + shadowHalfw)).r + bias;

        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowValue = texture(shadowMap, shadowCoords.xy + vec2(0 + shadowHalfw, shadowTexelSize + shadowHalfw)).r + bias;

        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowValue = texture(shadowMap, shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, shadowTexelSize + shadowHalfw)).r + bias;

        if (shadowValue < shadowCoords.z) {
            shadowTotal += shadowDarkness * shadowCoords.w;
        }

        shadowShadeFactor = 1.0 - (shadowTotal / 4.0);
    } else {
        shadowShadeFactor = 1.0;
    }

    return shadowShadeFactor;
}

float visibility(void) {
    return clamp(exp(-pow((length(positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

void main(void) {
	vec4 textureColour = texture(colourTexture, textureCoords);
	vec3 unitNormal = normalize(surfaceNormal);

	if (textureColour.a < 0.5){
		discard;
	}

	if (useNormalMap) {
    	vec4 normalMapValue = 2.0 * texture(normalMapTexture, textureCoords, -1.0) - 1.0;
    	unitNormal = normalize(normalMapValue.xyz);
    }

    out_colour = vec4((textureColour * shadow()).xyz,1.0);
    out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility());
    out_colour.a = min(out_colour.a, transparency);
}