#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec4 u_color;

out vec4 pass_Color;

void main(void) {
	gl_Position = (projection * view * model * in_Position).xyww;
	float normY = 0.2 * abs(in_Normal.y);
	pass_Color = vec4(u_color.xyz + vec3(normY, normY, normY), 1);
}