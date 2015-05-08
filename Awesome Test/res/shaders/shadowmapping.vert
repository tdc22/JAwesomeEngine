varying vec4 projCoord;

void main()
{
	vec4 realPos = gl_ModelViewMatrix * gl_Vertex;
  
	projCoord = gl_TextureMatrix[0] * realPos;
	//gl_FrontColor = vec4(0.8, 0.8, 0.8, 1.0);

	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	
}