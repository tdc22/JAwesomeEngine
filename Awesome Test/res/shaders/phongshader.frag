#version 330

in vec4 pass_Color;
in vec4 pass_Normal;
in vec4 pass_Vertex;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec3 u_lightpos;
uniform vec3 u_ambient;
uniform vec3 u_diffuse;
uniform float u_shininess;

out vec4 out_Color;

void main(void) {
    vec3 surfaceToCamera = normalize(vec3(0, 0, 1) - pass_Vertex.xyz);
    vec3 surfaceNormal = normalize((view * model * pass_Normal).xyz);
    vec3 lightDirection = normalize(u_lightpos - pass_Vertex.xyz);
    
    /////////////////////////DIFFUSE////////////////////////////////////////
   	float diffuseCoeff = max(0.0, dot(surfaceNormal, lightDirection));
    vec3 diffuse = diffuseCoeff * u_diffuse;

    /////////////////////////AMBIENT////////////////////////////////////////
    vec3 ambient = u_ambient;

    /////////////////////////SPECULAR//////////////////////////////////////
    float specularCoeff = 0.0;
    if(diffuseCoeff > 0.0)
        specularCoeff = pow(max(0.0, dot(surfaceToCamera, reflect(-lightDirection, surfaceNormal))), u_shininess);
    vec3 specular = vec3(specularCoeff, specularCoeff, specularCoeff);
    
    out_Color = vec4(ambient + diffuse +  specular, 1);
}