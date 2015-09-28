#version 330

in vec2 tex_coord;

uniform sampler2D u_texture;
uniform vec3 u_color;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(u_color, texture(u_texture, tex_coord).w);
}