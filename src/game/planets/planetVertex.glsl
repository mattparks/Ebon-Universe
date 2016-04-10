#version 130

#include "game/planets/noise2D.glsl"

layout(location = 0) in vec3 position;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    vec4 worldPosition = modelMatrix * vec4(position.x, 0, position.z, 1.0);
  //  worldPosition.y = snoise(vec2(worldPosition.x, worldPosition.z)) * 5;
	vec4 positionRelativeToCam = viewMatrix * worldPosition;

	gl_Position = projectionMatrix * positionRelativeToCam;
}
