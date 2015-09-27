#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec4 pass_Normal;

void main(void) {
	mat4 mvp = projection * view * model;
	gl_Position = mvp * in_Position;
	pass_Normal = mvp * in_Normal;
}