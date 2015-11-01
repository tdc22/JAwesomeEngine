#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform sampler2D u_texture;
uniform sampler2D u_bumpmap;

out vec3 passLightDir;
out vec2 passTexCoord;

void main(void) {
	passTexCoord = in_TextureCoord;
	passLightDir = in_Position.xyz;
	
	gl_Position = projection * view * model * in_Position;
}