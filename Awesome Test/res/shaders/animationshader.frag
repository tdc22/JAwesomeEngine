#version 330

in vec4 pass_stuff;
in vec2 pass_TexCoord;

uniform sampler2D u_texture;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(texture(u_texture, pass_TexCoord));
	out_Color = pass_stuff;
}