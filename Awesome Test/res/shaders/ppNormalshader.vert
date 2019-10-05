#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;

out vec2 tex_coord;

void main(void) {
	tex_coord = in_TextureCoord;
	gl_Position = in_Position;
}