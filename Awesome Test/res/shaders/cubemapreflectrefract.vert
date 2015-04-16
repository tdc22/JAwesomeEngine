uniform samplerCube cubeMap;

varying vec3 varyingNormal;
varying vec3 varyingCamVec;

void main()
{
	varyingNormal = gl_Normal;
	vec3 campos = (gl_ModelViewMatrixInverse * vec4(0, 0, 0, -1)).xyz;
	varyingCamVec = campos + gl_Vertex.xyz;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}