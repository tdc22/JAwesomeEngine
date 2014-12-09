uniform vec4 color;
uniform float depthBufferBits, zNear, zFar;
varying float flogz, halfcoef;

//void main(){
//    gl_FragColor = color;
//    gl_FragDepth = fdepth;
//}

void main()
{
	gl_FragColor = color;
	//gl_FragDepth = log2(flogz) * halfcoef;
	
	const float C = 1.0;
	const float far = zFar;
	const float offset = 1.0;
	//gl_FragDepth = (log(C * gl_TexCoord[6].z + offset) / log(C * far + offset));
	gl_FragDepth = (log(C * gl_TexCoord[6].w + offset) / log(C * far + offset));
}