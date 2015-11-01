#version 330

in vec3 passNormal;

uniform samplerCube u_cubemap;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(texture(u_cubemap, passNormal).rgb, 1);
}