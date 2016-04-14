#version 130

out vec4 out_colour;

varying vec4 pass_colour;
varying vec2 pass_textureCoords;
varying vec3 pass_surfaceNormal;
varying vec3 pass_toCameraVector;
varying vec3 pass_toLightVector[4];
varying vec4 pass_positionRelativeToCam;

layout(binding = 0) uniform sampler2D modelTexture;
layout(binding = 1) uniform sampler2D normalMap;

uniform vec3 colourMod;
uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform float transparency;
uniform float useNormalMap;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

void main(void) {
	vec4 totalTextureColour = texture(modelTexture, pass_textureCoords, -1.0);
	vec3 unitNormal = normalize(pass_surfaceNormal);

	if (useNormalMap == 1) {
		vec4 normalMapValue = 2.0 * texture(normalMap, pass_textureCoords, -1.0) - 1.0;
		unitNormal = normalize(normalMapValue.xyz);
	}

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < 4; i++) {
		float distance = length(pass_toLightVector[i]);
		float attinuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(pass_toLightVector[i]);

		float brightness = max(dot(unitNormal, unitLightVector), 0.0);
		vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
		float specularFactor = max(dot(reflectedLightDirection, normalize(pass_toCameraVector)), 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);

		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attinuationFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attinuationFactor;
	}

	float visibility = clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
	totalDiffuse = max(totalDiffuse, 0.2);

	if (totalTextureColour.a < 0.4) {
		discard;
	}

    // out_colour = pass_colour;
    out_colour = (totalTextureColour + vec4(colourMod, 1.0));

	out_colour = vec4(totalDiffuse, 1.0) * out_colour + vec4(totalSpecular, 1.0);
	out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);
	out_colour.a = transparency;
}
