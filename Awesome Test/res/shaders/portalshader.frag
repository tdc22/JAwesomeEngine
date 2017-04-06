#version 330

in vec2 pass_TexCoord;

uniform vec3 u_color;
uniform sampler2D u_texture;

out vec4 out_Color;

void main(void) {
	vec3 texColor = texture(u_texture, pass_TexCoord).rgb;
	out_Color.rgb = mix(texColor, u_color, length(pass_TexCoord - vec2(0.5, 0.5)));
	out_Color.a = 1;
}