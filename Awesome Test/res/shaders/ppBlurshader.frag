#version 330

in vec2 tex_coord;

uniform sampler2D u_texture;
uniform vec2 u_shift;

out vec4 out_Color;

const int gaussRadius = 11;
const float gaussFilter[gaussRadius] = float[gaussRadius](
	0.0402,0.0623,0.0877,0.1120,0.1297,0.1362,0.1297,0.1120,0.0877,0.0623,0.0402
);

void main(void) {
	vec2 texCoord = tex_coord - float(int(gaussRadius/2)) * u_shift;
	vec3 color = vec3(0.0, 0.0, 0.0); 
	
	for (int i = 0; i < gaussRadius; ++i) { 
		color += gaussFilter[i] * texture(u_texture, texCoord).rgb;
		texCoord += u_shift;
	}
	
	out_Color = vec4(color, 1.0);
}