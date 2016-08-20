#version 130

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_textureCoords;
layout(location = 1) in vec3 in_normal;
layout(location = 3) in vec3 in_tangent;

varying vec2 textureCoords;
varying vec3 surfaceNormal;
varying vec4 shadowCoords;
varying vec3 toCameraVector;
varying vec3 positionEyeSpace[4];
varying vec3 toLightVector[4];
varying vec4 positionRelativeToCam;

uniform mat4 projectionMatrix;
uniform mat4 shadowSpaceMatrix;
uniform float shadowDistance;
uniform mat4 viewMatrix;
uniform vec4 clipPlane;
uniform mat4 modelMatrix;

uniform vec3 lightPosition[4];

uniform float numberOfRows;
uniform vec2 textureOffset;

const float transitionDistance = 75.0;

void main(void) {
    vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	positionRelativeToCam = viewMatrix * worldPosition;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	gl_Position = projectionMatrix * positionRelativeToCam;

	textureCoords = (in_textureCoords / numberOfRows) + textureOffset;
	surfaceNormal = (modelMatrix * viewMatrix * vec4(in_normal, 0.0)).xyz;
	shadowCoords = shadowSpaceMatrix * worldPosition;

    float distanceAway = length(positionRelativeToCam.xyz);
    distanceAway = distanceAway - ((shadowDistance * 2.0) - (transitionDistance));
    distanceAway = distanceAway / transitionDistance;
    shadowCoords.w = clamp(1.0 - distanceAway, 0.0, 1.0);

	vec3 normal = normalize(surfaceNormal);
	vec3 tangent = normalize((modelMatrix * viewMatrix * vec4(in_tangent, 0.0)).xyz);
	vec3 bitangent = normalize(cross(normal, tangent));
	mat3 toTangentSpace = mat3(normal.x, bitangent.x, normal.x, tangent.y, bitangent.y, normal.y, tangent.z, bitangent.z, normal.z);

	toCameraVector = toTangentSpace * (-positionRelativeToCam.xyz);

	for(int i = 0; i < 4; i++) {
    	positionEyeSpace[i] = (viewMatrix * vec4(lightPosition[i], 1.0)).xyz;
    	toLightVector[i] = toTangentSpace * (positionEyeSpace[i] - positionRelativeToCam.xyz);
    }
}
