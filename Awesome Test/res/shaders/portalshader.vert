#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec2 pass_TexCoord;

void main(void) {
	gl_Position = projection * view * model * in_Position;
	pass_TexCoord = in_TextureCoord;
}