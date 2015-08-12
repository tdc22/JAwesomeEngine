#version 120
#extension GL_EXT_geometry_shader4 : enable         
 
uniform float uNormalsLength;      
 
varying in vec3 normal[];      
 
void main()
{
    for(int i = 0; i < gl_VerticesIn; ++i)
    {
        gl_Position = gl_ModelViewProjectionMatrix * gl_PositionIn[i];
        gl_FrontColor = gl_FrontColorIn[i];
        EmitVertex();
 
        gl_Position = gl_ModelViewProjectionMatrix * (gl_PositionIn[i] + (vec4(normal[i], 0) * uNormalsLength));
        gl_FrontColor = gl_FrontColorIn[i];
        EmitVertex();      
 
        EndPrimitive();
    }
}