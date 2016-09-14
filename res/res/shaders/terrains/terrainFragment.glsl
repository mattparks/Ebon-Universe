#version 130

//---------IN------------
in vec4 pass_positionRelativeToCam;
in vec2 pass_textureCoords;
in vec3 pass_surfaceNormal;
in vec3 pass_toCameraVector;
in vec3 pass_tilePosition;
in vec3 pass_toLightVector[4];

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D backgroundTexture;
layout(binding = 1) uniform sampler2D rTexture;
layout(binding = 2) uniform sampler2D gTexture;
layout(binding = 3) uniform sampler2D bTexture;
uniform vec3 lightColour[4];
uniform vec3 lightAttenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

//---------OUT------------
out vec4 out_colour;

//---------CONSTANT------------
const float tileAmount = 50.0;

//---------MAIN------------
void main(void) {
	vec4 blendMapColour = vec4(pass_tilePosition, 1.0);
	blendMapColour = vec4(normalize(pass_surfaceNormal).x, 0, 0, 1); // TODO
	float backgroundTextureAmount = 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
	vec2 tiledCoords = pass_textureCoords * tileAmount;
	vec4 totalTextureColour = (texture(backgroundTexture, tiledCoords) * backgroundTextureAmount) + (texture(rTexture, tiledCoords) * blendMapColour.r) + (texture(gTexture, tiledCoords) * blendMapColour.g) + (texture(bTexture, tiledCoords) * blendMapColour.b);

	vec3 unitNormal = normalize(pass_surfaceNormal);
	vec3 unitVectorToCamera = normalize(pass_toCameraVector);
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < 4; i++) {
		float distance = length(pass_toLightVector[i]);
		float attinuationFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(pass_toLightVector[i]);

		float brightness = max(dot(unitNormal, unitLightVector), 0.0);
		vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
		float specularFactor = max(dot(reflectedLightDirection, unitVectorToCamera), 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);

		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attinuationFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attinuationFactor;
	}

	float visibility = clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
	totalDiffuse = max(totalDiffuse, 0.2);

    out_colour = vec4(1,0,0,1);
	//out_colour = vec4(totalDiffuse, 1.0) * totalTextureColour + vec4(totalSpecular, 1.0);
	//out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);
}