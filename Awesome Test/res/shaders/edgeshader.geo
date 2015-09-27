#version 330

layout(triangles_adjacency) in;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

layout(line_strip, max_vertices = 4) out;

void main(void)
{
	vec4 posa = gl_in[0].gl_Position;
	vec4 posb = gl_in[2].gl_Position;
	vec4 posc = gl_in[4].gl_Position;

	vec4 adj1 = gl_in[1].gl_Position;
	vec4 adj2 = gl_in[3].gl_Position;
	vec4 adj3 = gl_in[5].gl_Position;

	vec4 normal = normalize(vec4(cross(posa.xyz - posb.xyz, posb.xyz - posc.xyz), 0));

	vec4 normal1 = normalize(vec4(cross(posa.xyz - posb.xyz, posb.xyz - adj1.xyz), 0));
	vec4 normal2 = normalize(vec4(cross(adj2.xyz - posb.xyz, posb.xyz - posc.xyz), 0));
	vec4 normal3 = normalize(vec4(cross(posa.xyz - adj3.xyz, adj3.xyz - posc.xyz), 0));

	mat4 mvp = projection * view * model;

	bool a = dot(normal, normal1) >= 0;
	bool b = dot(normal, normal2) >= 0;
	bool c = dot(normal, normal3) >= 0;
	
	if(a) {
		gl_Position = mvp * posa;
		EmitVertex();

		gl_Position = mvp * posb;
		EmitVertex();
	}
	if(b) {
		if(!a) {
			gl_Position = mvp * posb;
			EmitVertex();
		}

		gl_Position = mvp * posc;
		EmitVertex();
	}
	if(c) {
		if(!b) {
			gl_Position = mvp * posc;
			EmitVertex();
		}

		gl_Position = mvp * posa;
		EmitVertex();
	}
	EndPrimitive();
}