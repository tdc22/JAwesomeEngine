uniform samplerCube cubeMap;

varying vec3 varyingNormal;

void main()
{
	gl_FragColor = textureCube(cubeMap, reflect(vec3(0, 0, 1), varyingNormal)) + vec4(0.5, 0.5, 0.5, 1);
	//gl_FragColor = vec4(varyingNormal, 1);
}