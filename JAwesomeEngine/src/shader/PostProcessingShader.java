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
		int tex0 = 0, tex1 = 0, tex0depth = 0, tex1depth = 0, tex0normal = 0, tex1normal = 0;
		if (from.isColorRendered()) {
			tex0 = from.getColorTextureID();
			tex1 = to.getColorTextureID();
		}
		if (from.isDepthRendered()) {
			tex0depth = from.getDepthTextureID();
			tex1depth = to.getDepthTextureID();
		}
		if (from.isNormalRendered()) {
			tex0normal = from.getNormalTextureID();
			tex1normal = to.getNormalTextureID();
		}
		for (int i = 0; i < iterations; i++) {
			FramebufferObject current = p ? to : from;
			current.bind();
			current.clear();
			if (from.isColorRendered()) {
				((Texture) shader.getArgument("u_texture")).setTextureID(p ? tex0 : tex1);
			}
			if (from.isDepthRendered()) {
				((Texture) shader.getArgument("u_depthTexture")).setTextureID(p ? tex0depth : tex1depth);
			}
			if (from.isNormalRendered()) {
				((Texture) shader.getArgument("u_normalTexture")).setTextureID(p ? tex0normal : tex1normal);
			}
			shader.renderNoMatrix();
			current.unbind();
			p = !p;
		}
	}
}
