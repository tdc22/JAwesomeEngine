#version 120

uniform vec3 lightpos;
uniform vec4 ambient;
uniform vec4 diffuse;
uniform float shininess;

varying vec4 varyingColor;
varying vec3 varyingNormal;
varying vec4 varyingVertex;

void main() {
    varyingColor = diffuse;
    varyingNormal = gl_Normal;
    varyingVertex = gl_Vertex;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}