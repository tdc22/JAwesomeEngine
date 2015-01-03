package objects;

import utils.Shader;

public abstract class RenderedObject extends DataGameObject implements
		Renderable {
	protected Shader shader;
	protected boolean shadered = false;
	protected boolean shaderactive = true;
	
	public void setShader(Shader shader) {
		this.shader = shader;
		shadered = true;
	}

	public Shader getShader() {
		return shader;
	}
	
	public void setShaderActive(boolean active) {
		shaderactive = active;
	}
}
