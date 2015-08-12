#version 330

in vec2 tex_coord;

uniform sampler2D u_cubemap;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(texture(u_cubemap, tex_coord).rgb + vec3(0.2, 0.2, 0.2), 1);
}