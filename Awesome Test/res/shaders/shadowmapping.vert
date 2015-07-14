varying vec4 shadowCoord;
uniform mat4 lightPos;

void main()
{
	vec4 realPos = gl_ModelViewMatrix * gl_Vertex;
  
	shadowCoord = lightPos * gl_Vertex;
	shadowCoord = gl_TextureMatrix[7] * (vec4(gl_Vertex.xyz, 1.0));
	  
	gl_Position = ftransform();
	
	gl_FrontColor = shadowCoord;
	
}