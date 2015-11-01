#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform samplerCube u_cubemap;

out vec3 passNormal;
out vec3 passCamDir;

void main(void) {
	passNormal = in_Normal.xyz;
	mat4 mv = view * model;
	vec3 campos = (inverse(mv) * vec4(0, 0, 0, 1)).xyz;
	passCamDir = in_Position.xyz - campos;
	gl_Position = projection * mv * in_Position; // Matrix multiplication not commutative
}