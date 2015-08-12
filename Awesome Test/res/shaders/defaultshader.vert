#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 1)in vec4 in_Color;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec4 pass_Color;

void main(void) {
	gl_Position = projection * view * model * in_Position;
	pass_Color = in_Color;
}