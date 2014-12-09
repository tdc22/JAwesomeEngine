uniform samplerCube cubeMap;
uniform sampler2D colorMap;

const float reflect_factor = 0.5;

void main (void)
{
	vec4 output_color;
	
	// Perform a simple 2D texture look up.
	vec3 base_color = texture2D(colorMap, gl_TexCoord[0].xy).rgb;
	
	// Perform a cube map look up.
	vec3 cube_color = textureCube(cubeMap, gl_TexCoord[1].xyz).rgb;

	// Write the final pixel.
	gl_FragColor = vec4( mix(base_color, cube_color, reflect_factor), 1.0);
}