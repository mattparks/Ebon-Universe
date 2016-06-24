#version 130

#include "flounder/shaders/maths.glsl"

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoords;
layout(location = 2) in vec3 normal;

varying vec2 pass_textureCoords;
varying vec3 pass_surfaceNormal;
varying vec3 pass_toCameraVector;
varying vec3 pass_toLightVector[4];
varying vec4 pass_positionRelativeToCam;
varying vec3 pass_tilePosition;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];
uniform vec4 clipPlane;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	pass_positionRelativeToCam = viewMatrix * worldPosition;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	pass_textureCoords = textureCoords;
	pass_surfaceNormal = (modelMatrix * vec4(normal, 0.0)).xyz;
	pass_toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	pass_tilePosition = position;

	for (int i = 0; i < 4; i++) {
		pass_toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
}