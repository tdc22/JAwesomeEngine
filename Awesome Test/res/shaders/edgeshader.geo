#version 120
#extension GL_EXT_geometry_shader4 : enable

void main() {
  vec4 posa = gl_PositionIn[0];
  vec4 posb = gl_PositionIn[2];
  vec4 posc = gl_PositionIn[4];
  
  vec4 adj1 = gl_PositionIn[1];
  vec4 adj2 = gl_PositionIn[3];
  vec4 adj3 = gl_PositionIn[5];
  
  vec4 normal = normalize(vec4(cross(posa.xyz - posb.xyz, posb.xyz - posc.xyz), 0));
  
  vec4 normal1 = normalize(vec4(cross(posa.xyz - posb.xyz, posb.xyz - adj1.xyz), 0));
  vec4 normal2 = normalize(vec4(cross(adj2.xyz - posb.xyz, posb.xyz - posc.xyz), 0));
  vec4 normal3 = normalize(vec4(cross(posa.xyz - adj3.xyz, adj3.xyz - posc.xyz), 0));
  
  if(dot(normal, normal1) >= 0) {
  	gl_Position = gl_ModelViewProjectionMatrix * posa;
    gl_FrontColor = vec4(0,0,0,1);
    EmitVertex();
    
    gl_Position = gl_ModelViewProjectionMatrix * posb;
    gl_FrontColor = vec4(0,0,0,1);
    EmitVertex();
  }
  if(dot(normal, normal2) >= 0) {
  	gl_Position = gl_ModelViewProjectionMatrix * posb;
    gl_FrontColor = vec4(0,0,0,1);
    EmitVertex();
    
    gl_Position = gl_ModelViewProjectionMatrix * posc;
    gl_FrontColor = vec4(0,0,0,1);
    EmitVertex();
  }
  if(dot(normal, normal3) >= 0) {
  	gl_Position = gl_ModelViewProjectionMatrix * posc;
    gl_FrontColor = vec4(0,0,0,1);
    EmitVertex();
    
    gl_Position = gl_ModelViewProjectionMatrix * posa;
    gl_FrontColor = vec4(0,0,0,1);
    EmitVertex();
  }
  EndPrimitive();
}