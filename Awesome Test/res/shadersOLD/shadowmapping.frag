uniform sampler2D shadowMap;
varying vec4 shadowCoord;

void main ()
{	
	vec4 shadowCoordinateWdivide = shadowCoord / shadowCoord.w ;
	
	shadowCoordinateWdivide.z += 0.0005;
			
	float distanceFromLight = texture2D(shadowMap,shadowCoordinateWdivide.st).z;
	
	float shadow = 1.0;
	if (shadowCoord.w > 0.0)
		shadow = distanceFromLight < shadowCoordinateWdivide.z ? 0.8 : 1.0 ;
		
	gl_FragColor = shadow * gl_Color;
}