varying vec3 lightToVertexDirection;

void main(){
    // get vertex in clip space
	vec4 vertexClip = ftransform();
	// get light in clip space
	vec4 lightClip = gl_ProjectionMatrix * gl_LightSource[0].position;
	
	// get direction
	lightToVertexDirection = normalize(vertexClip.xyz - lightClip.xyz);
	
	gl_Position = gl_ModelViewMatrix * gl_Vertex;
}