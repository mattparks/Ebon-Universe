#version 130

//---------IN------------
in vec4 pass_positionRelativeToCam;
in vec2 pass_textureCoords;
in vec3 pass_surfaceNormal;
in vec3 pass_toCameraVector;
in vec3 pass_positionEyeSpace[4];
in vec3 pass_toLightVector[4];

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D normalMapTexture;
uniform bool useNormalMap;
uniform float transparency;
uniform vec3 lightColour[4];
uniform vec3 lightAttenuation[4];
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------CONSTANT------------
const float shineDamper = 1;
const float reflectivity = 0;

//---------LIGHTING------------
vec4 lighting(vec4 inColour, vec3 unitNormal) {
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i = 0; i < 4; i++) {
    	float distance = length(pass_toLightVector[i]);
    	float attinuationFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);
    	vec3 unitLightVector = normalize(pass_toLightVector[i]);

    	float brightness = max(dot(unitNormal, unitLightVector), 0.0);
    	vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);
    	float specularFactor = max(dot(reflectedLightDirection, normalize(pass_toCameraVector)), 0.0);
    	float dampedFactor = pow(specularFactor, shineDamper);

    	totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attinuationFactor;
    	totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attinuationFactor;
    }

    totalDiffuse = max(totalDiffuse, 0.2);
    return vec4(totalDiffuse, 1.0) * inColour + vec4(totalSpecular, 1.0);
}

//---------VISIBILITY------------
float visibility(void) {
    return clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

//---------MAIN------------
void main(void) {
	vec4 textureColour = texture(colourTexture, pass_textureCoords);
	vec3 unitNormal = normalize(pass_surfaceNormal);

	if (textureColour.a < 0.4){
	    out_colour = vec4(0.0);
		discard;
	}

	if (useNormalMap) {
    	vec4 normalMapValue = 2.0 * texture(normalMapTexture, pass_textureCoords, -1.0) - 1.0;
    	unitNormal = normalize(normalMapValue.xyz);
    }

    out_colour = textureColour;
    out_colour = lighting(out_colour, unitNormal);
    out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility());
    out_colour.a = min(out_colour.a, transparency);
}