#version 330

in vec2 tex_coord;

uniform sampler2D u_normalTexture;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(texture(u_normalTexture, tex_coord));
}