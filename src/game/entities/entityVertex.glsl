#version 150

#include "flounder/shaders/maths.glsl"

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoords;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 tangent;
layout(location = 4) in vec4 colour;
layout(location = 5) in float material[10]; // TODO

varying vec4 pass_colour;
varying vec2 pass_textureCoords;
varying vec3 pass_surfaceNormal;
varying vec4 pass_shadowCoords;
varying vec3 pass_toCameraVector;
varying vec3 pass_toLightVector[4];
varying vec3 pass_positionEyeSpace[4];
varying vec4 pass_positionRelativeToCam;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 shadowSpaceMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform vec3 lightPosition[4];
uniform float useFakeLighting;
uniform float atlasRows;
uniform vec2 atlasOffset;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * vec4(position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	vec3 realNormal = normal;

	if (useFakeLighting == 1) {
		realNormal = vec3(0.0, 1.0, 0.0);
	}

	vec3 surfaceNormal = (modelViewMatrix * vec4(realNormal, 0.0)).xyz;
	// vec3 surfaceNormal = realNormal;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));
	mat3 toTangentSpace = mat3(tang.x, bitang.x, norm.x, tang.y, bitang.y, norm.y, tang.z, bitang.z, norm.z);

    float specularCoefficient = material[0];
    vec3 ambientColour = vec3(material[1], material[2], material[3]);
    vec3 diffuseColour = vec3(material[4], material[5], material[6]);
    vec3 specularColour = vec3(material[7], material[8], material[9]);

    pass_colour = vec4(specularCoefficient, 0, 0, 1.0);
	pass_textureCoords = (textureCoords / atlasRows) + atlasOffset;
	pass_surfaceNormal = toTangentSpace * surfaceNormal;
	// pass_surfaceNormal = (modelMatrix * vec4(surfaceNormal, 0.0)).xyz;
	pass_shadowCoords = shadowSpaceMatrix * worldPosition;
	pass_toCameraVector = toTangentSpace * (-pass_positionRelativeToCam.xyz);
	// pass_toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

	for(int i = 0; i < 4; i++) {
		pass_positionEyeSpace[i] = (viewMatrix * vec4(lightPosition[i], 1.0)).xyz;
		pass_toLightVector[i] = toTangentSpace * (pass_positionEyeSpace[i] - pass_positionRelativeToCam.xyz);
		// pass_toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
}
