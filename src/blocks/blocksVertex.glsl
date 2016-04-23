#version 130

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 2) in vec3 in_normal;
layout(location = 3) in vec3 in_tangent;

uniform mat4 modelMatrix;
uniform vec3 colour;

varying vec4 pass_positionRelativeToCam;
varying vec3 pass_colour;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;

void main(void) {
    vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * vec4(in_position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	pass_colour = colour;
}
