uniform samplerCube cubeMap;
uniform float refractionIndex;

varying vec3 varyingNormal;
varying vec3 varyingCamVec;

void main()
{
	gl_FragColor = textureCube(cubeMap, refract(varyingCamVec, varyingNormal, refractionIndex)) + vec4(0.5, 0.5, 0.5, 1);
}