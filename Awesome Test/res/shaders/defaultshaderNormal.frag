#version 330

in vec4 pass_Color, pass_Normal;

layout(location = 0)out vec4 out_Color;
layout(location = 1)out vec4 out_Normal;

void main(void) {
	out_Color = pass_Color;
	out_Normal = pass_Normal;
}