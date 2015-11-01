#version 330

in vec3 passNormal;
in vec3 passCamDir;

uniform samplerCube u_cubemap;
uniform float refractionIndex;

out vec4 out_Color;

void main(void) {
	out_Color = texture(u_cubemap, refract(passCamDir, passNormal, refractionIndex)) + vec4(0.2, 0.2, 0.2, 1);
}