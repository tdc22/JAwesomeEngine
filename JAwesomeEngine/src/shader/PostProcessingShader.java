package shader;

public class PostProcessingShader {
	Shader shader;
	int iterations;

	public PostProcessingShader(Shader shader, int iterations) {
		this.shader = shader;
		this.iterations = iterations;
	}

	public Shader getShader() {
		return shader;
	}

	public int getIterations() {
		return iterations;
	}
}
