varying vec3 vertexVector;
uniform samplerCube cubeMap;

void main()
{
	gl_FragColor = textureCube(cubeMap, vertexVector);
}