#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;
layout(location = 4)in mat4 in_InstanceMatrices;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform sampler2D u_texture;

out vec2 tex_coord;

void main(void) {
	gl_Position = projection * view * model * in_InstanceMatrices * in_Position;
	tex_coord = in_TextureCoord;
}