#version 130

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 1) in vec3 in_normal;
layout(location = 3) in vec3 in_tangent;

out vec4 pass_positionRelativeToCam;
out vec2 pass_textureCoords;
out vec3 pass_surfaceNormal;
out vec3 pass_toCameraVector;
out vec3 pass_positionEyeSpace[4];
out vec3 pass_toLightVector[4];

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform vec3 lightPosition[4];
uniform mat4 modelMatrix;
uniform float atlasRows;
uniform vec2 atlasOffset;

void main(void) {
    vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * vec4(in_position, 1.0);

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	vec3 surfaceNormal = (modelViewMatrix * vec4(in_normal, 0.0)).xyz;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(in_tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));
	mat3 toTangentSpace = mat3(tang.x, bitang.x, norm.x, tang.y, bitang.y, norm.y, tang.z, bitang.z, norm.z);

	pass_textureCoords = (in_textureCoords / atlasRows) + atlasOffset;
	pass_surfaceNormal = toTangentSpace * surfaceNormal;
	pass_toCameraVector = toTangentSpace * (-pass_positionRelativeToCam.xyz);

	for(int i = 0; i < 4; i++) {
		pass_positionEyeSpace[i] = (viewMatrix * vec4(lightPosition[i], 1.0)).xyz;
		pass_toLightVector[i] = toTangentSpace * (pass_positionEyeSpace[i] - pass_positionRelativeToCam.xyz);
	}
}
