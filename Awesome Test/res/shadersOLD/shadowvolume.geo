#version 120
#extension GL_EXT_geometry_shader4 : enable

varying vec3 lightToVertexDirection;

void main() {
  for(int i = 0; i < gl_VerticesIn; ++i) {
    gl_FrontColor = vec4(1,1,1,1);
    gl_Position = gl_PositionIn[i];
    EmitVertex();
  }
  EndPrimitive();
}