#version 130

layout(location = 0) out vec4 out_colour;

varying vec2 pass_textureCoords;

layout(binding = 0) uniform sampler2D colourTexture;
layout(binding = 1) uniform sampler2D positionTexture;
layout(binding = 2) uniform sampler2D normalsTexture;
layout(binding = 3) uniform sampler2D additonalTexture;

const int NUMBER_LIGHTS = 32;

uniform vec3 cameraPosition;
uniform vec3 lightsColour[NUMBER_LIGHTS];
uniform vec3 lightsPosition[NUMBER_LIGHTS];
uniform vec3 lightsAttenuation[NUMBER_LIGHTS];

void main(void) {
    vec3 colour = texture2D(colourTexture, pass_textureCoords).rgb;
    vec3 position = texture2D(positionTexture, pass_textureCoords).rgb;
    vec3 normal = texture2D(normalsTexture, pass_textureCoords).rgb;
    vec4 additonal = texture2D(additonalTexture, pass_textureCoords);
    float specular = additonal.r; // g, b, a

//    for (int i = 0; i < NUMBER_LIGHTS; i++) {
//    }
//    out_colour = normal;

    // Then calculate lighting as usual.
    vec3 lighting = colour * 0.1; // Hard-coded ambient component.
    vec3 viewDir = normalize(cameraPosition - position);

    for(int i = 0; i < NUMBER_LIGHTS; i++) {
        // Diffuse
        vec3 lightDir = normalize(lightsPosition[i] - position);
        vec3 diffuse = max(dot(normal, lightDir), 0.0) * colour * lightsColour[i];
        lighting += diffuse;
    }

   // if (pass_textureCoords.x < 0.5) {
   //     out_colour = vec4(lighting, 1.0);
  //  } else {
        out_colour = vec4(colour, 1.0);
  //  }

    /*if (pass_textureCoords.x < 0.499 && pass_textureCoords.y < 0.499) {
        // Quad 3
	    out_colour = vec4(colour, 1.0);
    } else if (pass_textureCoords.x > 0.50 && pass_textureCoords.y < 0.499) {
        // Quad 4
	    out_colour = vec4(position, 1.0);
    } else if (pass_textureCoords.x < 0.499 && pass_textureCoords.y > 0.50) {
        // Quad 1
	    out_colour = vec4(normal, 1.0);
    } else if (pass_textureCoords.x > 0.50 && pass_textureCoords.y > 0.50) {
        // Quad 2
	    out_colour = additonal;
    } else {
        // Border Area.
        out_colour = vec4(0.0, 0.5, 1.0, 1.0);
    }*/
}
