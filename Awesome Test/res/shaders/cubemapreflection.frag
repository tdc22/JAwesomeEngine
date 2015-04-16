uniform samplerCube cubeMap;

varying vec3 varyingNormal;
varying vec3 varyingCamVec;

void main()
{
	gl_FragColor = textureCube(cubeMap, reflect(varyingCamVec, varyingNormal)) + vec4(0.5, 0.5, 0.5, 1);
	//gl_FragColor = vec4(normalize(varyingCampos), 1);
	//vec3 relPixelPosOnScreen = (gl_ProjectionMatrixInverse * normalize(gl_FragCoord)).xyz;
	//vec3 pixelDir = relPixelPosOnScreen - 
	//gl_FragColor = vec4(pixelDir, 1);
	//gl_FragColor = textureCube(cubeMap, reflect(vec3(0, 0, 1), varyingNormal)) + vec4(0.5, 0.5, 0.5, 1);
	//gl_FragColor = vec4(varyingNormal, 1);
}