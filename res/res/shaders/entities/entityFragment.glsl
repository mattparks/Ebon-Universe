#version 130

//---------IN------------
in vec4 pass_positionRelativeToCam;
in vec2 pass_textureCoords;
in vec3 pass_surfaceNormal;

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D diffuseMap;
layout(binding = 1) uniform sampler2D normalMap;
uniform bool useNormalMap;
uniform float transparency;
uniform vec3 lightDirection;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------CONSTANT------------
const vec2 lightBias = vec2(0.7, 0.6);

//---------VISIBILITY------------
float visibility(void) {
	return clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

//---------MAIN------------
void main(void) {
	vec4 diffuseColour = texture(diffuseMap, pass_textureCoords);
	vec3 unitNormal = normalize(pass_surfaceNormal);

	float diffuseLight = max(dot(-lightDirection, unitNormal), 0.0) * lightBias.x + lightBias.y;

	if (diffuseColour.a < 0.4){
		out_colour = vec4(0.0);
		discard;
	}

	if (useNormalMap) {
		vec4 normalMapValue = 2.0 * texture(normalMap, pass_textureCoords, -1.0) - 1.0;
		unitNormal = normalize(normalMapValue.xyz);
	}

	out_colour = vec4(diffuseColour.rgb * diffuseLight, diffuseColour.a);
	out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility());
	out_colour.a = min(out_colour.a, transparency);
}