#version 330

in vec2 tex_coord;

uniform sampler2D u_depthTexture;
uniform float u_depthMin, u_depthMax;

out vec4 out_Color;

void main(void) {
	vec2 texCoord = tex_coord.xy;
	float w = texture(u_depthTexture, texCoord).r;
	float c = (2.0 * u_depthMin) / (u_depthMax + u_depthMin - w * (u_depthMax - u_depthMin));
	out_Color = vec4(c, c, c, 1.0);
}