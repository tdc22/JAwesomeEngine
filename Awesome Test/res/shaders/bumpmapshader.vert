void main() {  
gl_TexCoord[0] = gl_MultiTexCoord0;  
  
// Set the position of the current vertex  
gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;  
}