#version 120
 
uniform sampler2D texture, depthTexture;
uniform float depthMin, depthMax;
 
void main() {
	vec2 texCoord = gl_TexCoord[0].xy;
	float w = texture2D(depthTexture, texCoord).r;
	float c = (2.0 * depthMin) / (depthMax + depthMin - w * (depthMax - depthMin));
	vec3 color = vec3(c, c, c);
	gl_FragColor = vec4(color,1.0);
}