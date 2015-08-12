#version 330

layout(location = 0)in vec4 in_Position;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec4 u_color;

out vec4 pass_Color;

void main(void) {
	gl_Position = projection * view * model * in_Position;
	pass_Color = u_color;
}