#version 330

layout(location = 0)in vec4 in_Position;
layout(location = 3)in vec4 in_Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec3 u_lightpos;
uniform vec3 u_ambient;
uniform vec3 u_diffuse;
uniform float u_shininess;

out vec4 pass_Color;
out vec4 pass_Normal;
out vec4 pass_Vertex;

void main(void) {
	pass_Color = vec4(u_diffuse, 1);
    pass_Normal = in_Normal;
    pass_Vertex = view * model * in_Position;
	gl_Position = projection * pass_Vertex;
}