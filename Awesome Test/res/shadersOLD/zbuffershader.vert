uniform vec4 color;
uniform float depthBufferBits, zNear, zFar;
varying float flogz, halfcoef;

//void main(){
//    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
//    //##gl_Position.z = (exp2(depthBufferBits)-1) * log2(gl_Position.w / zNear) / log2(zFar / zNear);
//    float fcoef = 2.0 / log2(zFar + 1.0);
//    gl_Position.z = log2(max(1e-6, 1.0 + gl_Position.w)) * fcoef - 1.0;
//    fdepth = log2(1.0 + gl_Position.w) * 0.5 * fcoef;
//}
void main()
{
	vec4 vertexPosClip = gl_ModelViewProjectionMatrix * vec4(gl_Vertex.xyz, 1.0);
	gl_Position = vertexPosClip;
	float fcoef = 2.0 / log2(zFar + 1.0);
    //gl_Position.z = log2(max(zNear, 1.0 + gl_Position.w)) * fcoef - 1.0;
    gl_Position.z = 2.0*log(gl_Position.w/zNear)/log(zFar/zNear) - 1; 
    gl_Position.z *= gl_Position.w;
    halfcoef = fcoef / 2.0;
    flogz = 1.0 + gl_Position.w;
	//gl_TexCoord[6] = vertexPosClip;
}