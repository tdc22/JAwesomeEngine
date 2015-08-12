#version 330

layout(location = 0)in vec4 in_Position;

void main(void) {
	gl_Position = in_Position;
}