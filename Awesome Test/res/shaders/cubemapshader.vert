#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform samplerCube u_cubemap;

out vec3 passNormal;

void main(void) {
	gl_Position = projection * view * model * in_Position;
	passNormal = in_Normal.xyz;
}