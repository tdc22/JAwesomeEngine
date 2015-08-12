uniform samplerCube cubeMap;

varying vec3 varyingNormal;
varying vec3 varyingCamVec;

void main()
{
	gl_FragColor = textureCube(cubeMap, reflect(varyingCamVec, varyingNormal)) + vec4(0.5, 0.5, 0.5, 1);
}