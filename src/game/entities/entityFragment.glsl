#version 130

layout(location = 0) out vec4 out_colour;
layout(location = 1) out vec4 out_position;
layout(location = 2) out vec4 out_normal;
layout(location = 3) out vec4 out_additonal;

layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D normalMapTexture;

varying vec2 textureCoords;
varying vec4 entityPosition;
varying vec3 surfaceNormal;
varying vec3 toCameraVector;
varying vec4 positionRelativeToCam;

uniform bool useNormalMap;
uniform float transparency;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

void main(void) {
	vec4 textureColour = texture(colourTexture, textureCoords);
	vec3 unitNormal = normalize(surfaceNormal);

	if (textureColour.a < 0.5){
		discard;
	}

	if (useNormalMap) {
    	vec4 normalMapValue = 2.0 * texture(normalMapTexture, textureCoords, -1.0) - 1.0;
    	unitNormal = normalize(normalMapValue.xyz);
    }

    float visibility = clamp(exp(-pow((length(positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);

    out_colour = textureColour;
    out_colour.a = min(out_colour.a, transparency);
    out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);
    out_position = entityPosition;
    out_normal = vec4(unitNormal, 0.0);
    out_additonal = vec4(0.2, 0.0, 0.0, 0.0);
}
