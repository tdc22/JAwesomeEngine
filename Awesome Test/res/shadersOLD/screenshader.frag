uniform sampler2D colorMap;
varying vec3 normal;

void main (void)
{
	if(normal == vec3(0,0,1)) {
		gl_FragColor = texture( colorMap, gl_TexCoord[0].st);
	}
}