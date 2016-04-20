#version 130

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 2) in vec3 in_normal;
layout(location = 3) in vec3 in_tangent;

varying vec4 pass_positionRelativeToCam;
varying vec4 pass_shadowCoords;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 shadowSpaceMatrix;
uniform float shadowDistance;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;

const float transitionDistance = 25.0;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * vec4(in_position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	pass_shadowCoords = shadowSpaceMatrix * worldPosition;

	float distanceAway = length(pass_positionRelativeToCam.xyz);
	distanceAway = distanceAway - ((shadowDistance * 2) - (transitionDistance));
	distanceAway = distanceAway / transitionDistance;
	pass_shadowCoords.w = clamp(1.0 - distanceAway, 0.0, 1.0);
}
