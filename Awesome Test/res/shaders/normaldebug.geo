#version 330

layout(triangles_adjacency) in;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform float u_normalsLength;      
 
in vec4 pass_Normal[];      

layout(line_strip, max_vertices = 6) out;
 
void main()
{
	for(int i = 0; i < 6; i += 2)
    {
        gl_Position = gl_in[i].gl_Position;
        EmitVertex();
 
        gl_Position = (gl_in[i].gl_Position + (pass_Normal[i] * u_normalsLength));
        EmitVertex();
        EndPrimitive();
    }
}