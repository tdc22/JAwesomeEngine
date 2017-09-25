#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 1)in vec3 in_Color;
layout(location = 2)in vec2 in_TextureCoord;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform sampler2D u_texture;

out float minusAlpha;
out vec2 tex_coord;

void main(void) {
	gl_Position = projection * view * model * in_Position;
	minusAlpha = 1-sqrt(in_Color.x);
	tex_coord = in_TextureCoord;
}