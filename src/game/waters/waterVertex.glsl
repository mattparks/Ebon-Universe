#version 130

#include "flounder/shaders/maths.glsl"

layout(location = 0) in vec2 position;

varying vec2 pass_textureCoords;
varying vec4 pass_clipSpace;
varying vec3 pass_toCameraVector;
varying vec3 pass_toLightVector;
varying vec4 pass_positionRelativeToCamera;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float textureTiling;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	pass_positionRelativeToCamera = viewMatrix * worldPosition;

	gl_Position = projectionMatrix * pass_positionRelativeToCamera;

	pass_textureCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * textureTiling;
	pass_clipSpace = projectionMatrix * pass_positionRelativeToCamera;
	pass_toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	pass_toLightVector = worldPosition.xyz - lightPosition;
}