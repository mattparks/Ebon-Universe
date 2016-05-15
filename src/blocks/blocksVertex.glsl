#version 130

layout(location = 0) in vec3 in_position;
// layout(location = 1) in vec2 in_textureCoord;
// layout(location = 2) in vec3 in_normal;
layout(location = 3) in mat4 in_modelMatrix;
layout(location = 7) in vec3 in_colour;
layout(location = 8) in int in_highlighted;

varying vec4 pass_positionRelativeToCam;
varying vec3 pass_colour;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;

void main(void) {
    vec4 worldPosition = in_modelMatrix * vec4(in_position, 1.0);
	mat4 modelViewMatrix = viewMatrix * in_modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * vec4(in_position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	pass_colour = in_colour;

	if (in_highlighted != 0) {
	    pass_colour = pass_colour * 0.7;
	}
}
