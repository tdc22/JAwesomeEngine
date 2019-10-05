#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 1)in vec4 in_Color;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec4 pass_Color, pass_Normal;

void main(void) {
	gl_Position = projection * view * model * in_Position;
	pass_Color = in_Color;
	pass_Normal = in_Normal;
	pass_Normal.a = 1.0;
}