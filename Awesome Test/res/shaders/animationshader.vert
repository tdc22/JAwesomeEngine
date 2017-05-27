#version 330

const int MAX_JOINTS = 50;

layout(location = 0)in vec4 in_Position;
layout(location = 2)in vec2 in_TextureCoord;
layout(location = 4)in ivec4 in_JointIndices;
layout(location = 5)in vec4 in_JointWeights;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform sampler2D u_texture;
uniform mat4 u_jointTransforms[MAX_JOINTS];

out vec4 pass_stuff;
out vec2 pass_TexCoord;

void main(void) {
	vec4 totalLocalPos = in_Position;

	for(int i = 0; i < 4; i++) {
		mat4 jointTransform = u_jointTransforms[in_JointIndices[i]];
		vec4 posePosition = jointTransform * vec4(in_Position.xyz, 1.0);
		totalLocalPos += posePosition * in_JointWeights[i];
		
		totalLocalPos += posePosition * 10;
	}
	pass_stuff = vec4(in_JointWeights.r, in_JointWeights.g, in_JointWeights.b, 1);

	gl_Position = projection * view * model * totalLocalPos;
	pass_TexCoord = in_TextureCoord;
}