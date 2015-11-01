#version 330

in vec3 passLightDir;
in vec2 passTexCoord;

uniform sampler2D u_texture;
uniform sampler2D u_bumpmap;

out vec4 out_Color;

void main(void) {
	// Extract the normal from the normal map  
	vec3 normal = normalize(texture2D(u_bumpmap, passTexCoord.st).rgb * 2.0 - 1.0);  
  
	// Calculate the lighting diffuse value  
	float diffuse = max(dot(normal, passLightDir), 0.1);  

	vec3 color = diffuse * texture2D(u_texture, passTexCoord).rgb;  

	// Set the output color of our current pixel  
	out_Color = vec4(color, 1.0);  
}