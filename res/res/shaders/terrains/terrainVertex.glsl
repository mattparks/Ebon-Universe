#version 130

#include "maths.glsl"

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 1) in vec3 in_normal;

varying vec4 pass_positionRelativeToCam;
varying vec2 pass_textureCoords;
varying vec3 pass_surfaceNormal;
varying vec3 pass_toCameraVector;
varying vec3 pass_tilePosition;
varying vec3 pass_toLightVector[4];

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform vec3 lightPosition[4];
uniform mat4 modelMatrix;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	pass_positionRelativeToCam = viewMatrix * worldPosition;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	pass_textureCoords = in_textureCoords;
	pass_surfaceNormal = (modelMatrix * vec4(in_normal, 0.0)).xyz;
	pass_toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	pass_tilePosition = in_position;

	for (int i = 0; i < 4; i++) {
		pass_toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
}