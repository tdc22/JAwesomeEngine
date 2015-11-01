#version 330

in vec3 passNormal;
in vec3 passCamDir;

uniform samplerCube u_cubemap;

out vec4 out_Color;

void main(void) {
	out_Color = texture(u_cubemap, reflect(passCamDir, passNormal)) + vec4(0.2, 0.2, 0.2, 1);
}