#version 130

//---------CONSTANT------------
const int MAX_JOINTS = 50;
const int MAX_WEIGHTS = 3;

//---------IN------------
layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 2) in vec3 in_normal;
layout(location = 3) in vec3 in_tangent;
layout(location = 4) in ivec3 in_jointIndices;
layout(location = 5) in vec3 in_weights;

//---------UNIFORM------------
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;

uniform mat4 jointTransforms[MAX_JOINTS];
uniform bool animated;

uniform mat4 modelMatrix;
uniform float atlasRows;
uniform vec2 atlasOffset;

//---------OUT------------
out vec4 pass_positionRelativeToCam;
out vec2 pass_textureCoords;
out vec3 pass_surfaceNormal;

//---------MAIN------------
void main(void) {
	vec4 totalLocalPos = vec4(0.0);
	vec4 totalNormal = vec4(0.0);

    if (animated) {
        for (int i = 0; i < MAX_WEIGHTS; i++){
            vec4 localPosition = jointTransforms[in_jointIndices[i]] * vec4(in_position, 1.0);
            totalLocalPos += localPosition * in_weights[i];

            vec4 worldNormal = jointTransforms[in_jointIndices[i]] * vec4(in_normal, 0.0);
            totalNormal += worldNormal * in_weights[i];
        }
	} else {
	    totalLocalPos = vec4(in_position, 1.0);
	    totalNormal = vec4(in_normal, 0.0);
	}

	vec4 worldPosition = modelMatrix * totalLocalPos;
	mat4 modelViewMatrix = viewMatrix * modelMatrix;
	pass_positionRelativeToCam = modelViewMatrix * totalLocalPos;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * pass_positionRelativeToCam;

	vec3 surfaceNormal = (modelViewMatrix * totalNormal).xyz;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(in_tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));
	mat3 toTangentSpace = mat3(tang.x, bitang.x, norm.x, tang.y, bitang.y, norm.y, tang.z, bitang.z, norm.z);

	pass_textureCoords = (in_textureCoords / atlasRows) + atlasOffset;
	pass_surfaceNormal = totalNormal.xyz;//(modelMatrix * totalNormal).xyz; // toTangentSpace * surfaceNormal;
}
