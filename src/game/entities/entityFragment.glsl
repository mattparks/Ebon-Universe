#version 130

layout(location = 0) out vec4 out_colour;

layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D normalMapTexture;
layout(binding = 2) uniform sampler2D shadowMap;

varying vec2 textureCoords;
varying vec3 surfaceNormal;
varying vec4 shadowCoords;
varying vec3 toCameraVector;
varying vec3 positionEyeSpace[4];
varying vec3 toLightVector[4];
varying vec4 positionRelativeToCam;

uniform bool useNormalMap;
uniform float transparency;
uniform float shadowMapSize;

uniform vec3 lightColour[4];
uniform vec3 lightAttenuation[4];

uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

const float tileAmount = 50.0;
const float shadowDarkness = 0.6;

const float shineDamper = 1;
const float reflectivity = 0;

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

vec4 lighting(vec4 inColour, vec3 unitNormal) {
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i = 0; i < 4; i++) {
    	float distance = length(toLightVector[i]);
    	float attinuationFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);
    	vec3 unitLightVector = normalize(toLightVector[i]);

    	float brightness = max(dot(unitNormal, unitLightVector), 0.0);
    	vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
    	float specularFactor = max(dot(reflectedLightDirection, normalize(toCameraVector)), 0.0);
    	float dampedFactor = pow(specularFactor, shineDamper);

    	totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attinuationFactor;
    	totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attinuationFactor;
    }

    totalDiffuse = max(totalDiffuse, 0.2);
    return vec4(totalDiffuse, 1.0) * inColour + vec4(totalSpecular, 1.0);
}

float visibility(void) {
    return clamp(exp(-pow((length(positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

void main(void) {
	vec4 textureColour = texture(colourTexture, textureCoords);
	vec3 unitNormal = normalize(surfaceNormal);

	if (textureColour.a < 0.4){
	    out_colour = vec4(0.0);
		discard;
	}

	if (useNormalMap) {
    	vec4 normalMapValue = 2.0 * texture(normalMapTexture, textureCoords, -1.0) - 1.0;
    	unitNormal = normalize(normalMapValue.xyz);
    }

	out_colour = lighting(textureColour, unitNormal);
    // out_colour = vec4((out_colour * shadow()).rgb, 1.0);
    out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility());
    out_colour.a = min(out_colour.a, transparency);
}