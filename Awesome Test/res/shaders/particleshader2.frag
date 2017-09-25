#version 330

in float minusAlpha;
in vec2 tex_coord;

uniform sampler2D u_texture;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(texture(u_texture, tex_coord).xyz, texture(u_texture, tex_coord).w - minusAlpha);
}