#version 330

in vec2 tex_coord;

uniform sampler2D u_texture;
uniform sampler2D u_bumpmap;

out vec4 out_Color;

void main(void) {
	// Extract the normal from the normal map  
	vec3 normal = normalize(texture(u_bumpmap, tex_coord.st).rgb * 2.0 - 1.0);  
  
	// Determine where the light is positioned (this can be set however you like)  
	vec3 light_pos = normalize(vec3(1.0, 1.0, 1.5));  
  
	// Calculate the lighting diffuse value  
	float diffuse = max(dot(normal, light_pos), 0.0);  
  
	vec3 color = diffuse * texture(u_texture, tex_coord.st).rgb;  
  
	// Set the output color of our current pixel  
	out_Color = vec4(color, 1.0); 
}