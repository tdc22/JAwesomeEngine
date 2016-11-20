package shader;

import texture.FramebufferObject;
import texture.Texture;
import utils.DefaultValues;

public class PostProcessingShader {
	Shader shader;
	int iterations;

	public PostProcessingShader(Shader shader, int iterations) {
		this.shader = shader;
		this.iterations = iterations;
	}

	public PostProcessingShader(Shader shader) {
		this.shader = shader;
		this.iterations = DefaultValues.DEFAULT_POST_PROCESSING_ITERATIONS;
	}

	public Shader getShader() {
		return shader;
	}

	public int getIterations() {
		return iterations;
	}

	public void apply(FramebufferObject from, FramebufferObject to) {
		boolean p = true;
		int tex0 = from.getColorTextureID();
		int tex1 = to.getColorTextureID();
		int tex0depth = from.getDepthTextureID();
		int tex1depth = to.getDepthTextureID();
		for (int i = 0; i < iterations; i++) {
			FramebufferObject current = p ? to : from;
			current.bind();
			current.clear();
			((Texture) shader.getArgument("u_texture")).setTextureID(p ? tex0
					: tex1);
			((Texture) shader.getArgument("u_depthTexture"))
					.setTextureID(p ? tex0depth : tex1depth);
			shader.renderNoMatrix();
			current.unbind();
			p = !p;
		}
	}
}
