#version 130

//---------IN------------
layout(location = 0) in vec3 in_position;

//---------UNIFORM------------
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform mat4 modelMatrix;

//---------OUT------------
out vec4 pass_positionRelativeToCam;
out vec3 pass_textureCoords;

//---------MAIN------------
void main(void) {
    vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * vec4(in_position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	pass_textureCoords.x = -in_position.x;
	pass_textureCoords.y = -in_position.y;
	pass_textureCoords.z = in_position.z;
}