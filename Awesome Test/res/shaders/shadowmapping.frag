uniform sampler2DShadow shadowMap;
varying vec4 projCoord;

void main ()
{
	vec3 color = vec3(0.8, 0.8, 0.8);
	gl_FragColor = vec4(color * shadow2DProj(shadowMap, projCoord).r, 1);
	//const float kTransparency = 0.3;
	//vec4 color = vec4(0.8, 0.8, 0.8, 1.0);

	//float rValue = shadow2DProj(shadowMap, projCoord).r + kTransparency;
	//rValue = clamp(rValue, 0.0, 1.0);

	//vec3 coordPos  = projCoord.xyz / projCoord.w;

	//if(coordPos.x >= 0.0 && coordPos.y >= 0.0 && coordPos.x <= 1.0 && coordPos.y <= 1.0 )
   	//{
	//	gl_FragColor = color * rValue;
	//}
	//else
	//{
	//	gl_FragColor = color;
	//}
}