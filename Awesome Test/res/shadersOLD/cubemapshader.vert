varying vec3 vertexVector;
uniform samplerCube cubeMap;

void main()
{	
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	vertexVector = gl_Vertex.xyz;
}