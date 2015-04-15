uniform samplerCube cubeMap;

varying vec3 varyingNormal;

void main()
{
	varyingNormal = gl_Normal;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}