#version 120

uniform vec3 lightpos;
uniform vec4 ambient;
uniform vec4 diffuse;
uniform float shininess;

varying vec4 varyingColor;
varying vec3 varyingNormal;
varying vec4 varyingVertex;

void main() {
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    vec3 lightDirection = normalize(lightpos - vertexPosition);
    float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
    gl_FragColor.rgb = diffuseLightIntensity * varyingColor.rgb;
    gl_FragColor += ambient;
    //if (diffuseLightIntensity != 0) {
   	vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
    float fspecular = pow(specular, shininess);
    gl_FragColor += fspecular;
    //}
}