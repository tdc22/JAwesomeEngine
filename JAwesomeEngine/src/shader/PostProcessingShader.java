package shader;

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
}
