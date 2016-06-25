#version 130

out vec4 out_colour;

varying vec2 pass_textureCoords;
varying vec4 pass_clipSpace;
varying vec3 pass_toCameraVector;
varying vec3 pass_toLightVector;
varying vec4 pass_positionRelativeToCamera;

layout(binding = 0) uniform sampler2D reflectionTexture;
layout(binding = 1) uniform sampler2D refractionTexture;
layout(binding = 2) uniform sampler2D dudvTexture;
layout(binding = 3) uniform sampler2D normalTexture;
layout(binding = 4) uniform sampler2D depthTexture;

uniform vec3 colourAdditive;
uniform float colourMix;
uniform float moveFactor;
uniform float waveStrength;
uniform float normalDampener; // TODO
uniform float dropOffDepth;

uniform float nearPlane;
uniform float farPlane;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

void main(void) {
	vec2 ndc = (pass_clipSpace.xy / pass_clipSpace.w) / 2.0 + 0.5;
	vec2 refractionTextureCoords = vec2(ndc.x, ndc.y);
	vec2 reflectionTextureCoords = vec2(ndc.x, -ndc.y);

	float depth = texture(depthTexture, refractionTextureCoords).r;
	float floorDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - (2.0 * depth - 1.0) * (farPlane - nearPlane));
	depth = gl_FragCoord.z;
	float waterDistance = 2.0 * nearPlane * farPlane / (farPlane + nearPlane - (2.0 * depth - 1.0) * (farPlane - nearPlane));
	float waterDepth = floorDistance - waterDistance;

	vec2 distortedTextureCoords = texture(dudvTexture, vec2(pass_textureCoords.x + moveFactor, pass_textureCoords.y)).rg * 0.1;
	distortedTextureCoords = pass_textureCoords + vec2(distortedTextureCoords.x, distortedTextureCoords.y + moveFactor);
	vec2 totalDistortion = (texture(dudvTexture, distortedTextureCoords).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / dropOffDepth, 0.0, 1.0);;

	refractionTextureCoords += totalDistortion;
	refractionTextureCoords = clamp(refractionTextureCoords, 0.001, 0.999);

	reflectionTextureCoords += totalDistortion;
	reflectionTextureCoords.x = clamp(reflectionTextureCoords.x, 0.001, 0.999);
	reflectionTextureCoords.y = clamp(reflectionTextureCoords.y, -0.999, -0.001);

	vec4 reflectionColour = texture(reflectionTexture, reflectionTextureCoords);
	vec4 refractionColour = texture(refractionTexture, refractionTextureCoords);

	vec4 normalTextureColour = texture(normalTexture, distortedTextureCoords);
	vec3 normal = vec3(normalTextureColour.r * 2.0 - 1.0, normalTextureColour.b * 3.0, normalTextureColour.g * 2.0 - 1.0);
	normal = normalize(normal);

	vec3 viewVector = normalize(pass_toCameraVector);
	float refractiveFactor = dot(viewVector, normal);
	refractiveFactor = pow(refractiveFactor, 0.5);
	refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);

	vec3 reflectedLight = reflect(normalize(pass_toLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = lightColour * specular * reflectivity * clamp(waterDepth / dropOffDepth, 0.0, 1.0);

	float visibility = clamp(exp(-pow((length(pass_positionRelativeToCamera.xyz) * fogDensity), fogGradient)), 0.0, 1.0);

	//out_colour = mix(reflectionColour, refractionColour, refractiveFactor);
	out_colour = refractionColour;

	out_colour = mix(out_colour, vec4(colourAdditive, 1.0), colourMix) + vec4(specularHighlights, 0.0);
	out_colour = mix(texture(refractionTexture, vec2(ndc.x, ndc.y)), out_colour, clamp(waterDepth / dropOffDepth, 0.0, 1.0));
	out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility);
	//out_colour = texture(refractionTexture, pass_textureCoords);
}