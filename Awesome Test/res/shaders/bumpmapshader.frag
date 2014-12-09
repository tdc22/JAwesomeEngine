uniform sampler2D diffuseTexture;
uniform sampler2D normalTexture;
	
// New bumpmapping
varying vec3 lightVec;
varying vec3 halfVec;
varying vec3 eyeVec;


void main()
{

	// lookup normal from normal map, move from [0,1] to  [-1, 1] range, normalize
	vec3 normal = 2.0 * texture2D (normalTexture, gl_TexCoord[0].st).rgb - 1.0;
	normal = normalize (normal);
	
	// compute diffuse lighting
	float lamberFactor= max (dot (lightVec, normal), 0.2) ;
	vec4 diffuseMaterial = 0.0;
	vec4 diffuseLight  = 0.0;
	
	// compute specular lighting
	vec4 specularMaterial ;
	vec4 specularLight ;
	float shininess ;
  
	// compute ambient
	vec4 ambientLight = gl_LightSource[0].ambient;	
	
	if (lamberFactor > 0.0)
	{
		diffuseMaterial = texture2D (diffuseTexture, gl_TexCoord[0].st);
		diffuseLight  = gl_LightSource[0].diffuse;
		
		// In doom3, specular value comes from a texture 
		specularMaterial =  vec4(0.4)  ;
		specularLight = gl_LightSource[0].specular;
		shininess = pow (max (dot (halfVec, normal), 0.0), 2.0)  ;
		 
		gl_FragColor =	diffuseMaterial * diffuseLight * lamberFactor ;
		gl_FragColor +=	specularMaterial * specularLight * shininess ;				
	
	}
	
	gl_FragColor +=	ambientLight;
	
}