#version 130

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 1) in vec3 in_normal;
layout(location = 3) in vec3 in_tangent;

varying vec2 textureCoords;
varying vec4 entityPosition;
varying vec3 surfaceNormal;
varying vec4 shadowCoords;
varying vec3 toCameraVector;
varying vec4 positionRelativeToCam;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform mat4 modelMatrix;

uniform float numberOfRows;
uniform vec2 textureOffset;

void main(void) {
    vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	positionRelativeToCam = viewMatrix * worldPosition;
	entityPosition = projectionMatrix * positionRelativeToCam;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = entityPosition;

	textureCoords = (in_textureCoords / numberOfRows) + textureOffset;
	surfaceNormal = (modelMatrix * vec4(in_normal, 0.0)).xyz;

	vec3 normal = normalize(surfaceNormal);
	vec3 tangent = normalize((viewMatrix * modelMatrix * vec4(in_tangent, 0.0)).xyz);
	vec3 bitangent = normalize(cross(normal, tangent));
	mat3 toTangentSpace = mat3(normal.x, bitangent.x, normal.x, tangent.y, bitangent.y, normal.y, tangent.z, bitangent.z, normal.z);

	toCameraVector = toTangentSpace * (-positionRelativeToCam.xyz);
}
