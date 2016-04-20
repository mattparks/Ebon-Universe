#version 130

out vec4 out_colour;

layout(binding = 1) uniform sampler2D shadowMap;

varying vec4 pass_positionRelativeToCam;
varying vec4 pass_shadowCoords;

uniform vec3 colour;
uniform float shadowMapSize;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

const float tileAmount = 50.0;
const float shadowDarkness = 0.6;

void main(void) {
    float visibility = clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);

	float shadowTexelSize = 1.0 / shadowMapSize;
	float shadowHalfw = shadowTexelSize * 0.5;
	float shadowTotal = 0.0;
	float shadowValue = 0.0;
	float shadowShadeFactor;
	shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(0 + shadowHalfw, 0 + shadowHalfw)).r;

    if (pass_shadowCoords.x > 0.0 && pass_shadowCoords.x < 1.0 && pass_shadowCoords.y > 0.0 && pass_shadowCoords.y < 1.0 && pass_shadowCoords.z > 0.0 && pass_shadowCoords.z < 1.0) {
        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, 0 + shadowHalfw)).r;

        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(0 + shadowHalfw, shadowTexelSize + shadowHalfw)).r;

        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, shadowTexelSize + shadowHalfw)).r;

        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

	    shadowShadeFactor = 1.0 - (shadowTotal / 4.0);
	} else {
	    shadowShadeFactor = 1.0;
	}

    out_colour = vec4(colour, 1.0);
//    out_colour = out_colour * shadowShadeFactor;
	out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);
}
