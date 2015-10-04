package game;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.List;

import objects.ViewProjection;
import shader.PostProcessingShader;
import shader.Shader;
import texture.FramebufferObject;
import texture.Texture;

public class Layer {
	List<Shader> shader;
	List<PostProcessingShader> postProcessing;
	int postProcessingIterations = 20;
	FramebufferObject framebufferMultisample, framebuffer, framebufferPostProcessing;
	boolean active = true;

	public Layer() {
		shader = new ArrayList<Shader>();
		postProcessing = new ArrayList<PostProcessingShader>();
	}

	public void initLayer(ViewProjection vp, int resX, int resY, int samples) {
		framebufferMultisample = new FramebufferObject(vp, resX, resY, samples);
		framebuffer = new FramebufferObject(vp, resX, resY, 0);
		framebufferPostProcessing = new FramebufferObject(vp, resX, resY, 0);
	}

	public void delete() {
		for (Shader s : shader) {
			s.delete();
		}
		for (PostProcessingShader p : postProcessing) {
			p.getShader().delete();
		}
		if (framebufferMultisample != null)
			framebufferMultisample.delete();
		if (framebuffer != null)
			framebuffer.delete();
		if (framebufferPostProcessing != null)
			framebufferPostProcessing.delete();
	}

	public void begin() {
		framebufferMultisample.begin();
	}

	public void render() {
		for (Shader s : shader) {
			s.render();
		}
	}

	public void end() {
		framebufferMultisample.end();
		framebuffer.clear();
		framebufferMultisample.copyTo(framebuffer);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void applyPostProcessing(Shader target, int width, int height) {
		boolean p = true;
		int tex0 = framebuffer.getColorTextureID();
		int tex1 = framebufferPostProcessing.getColorTextureID();
		int tex0depth = framebuffer.getDepthTextureID();
		int tex1depth = framebufferPostProcessing.getDepthTextureID();
		for (PostProcessingShader pp : postProcessing) {
			for (int i = 0; i < pp.getIterations(); i++) {
				FramebufferObject current = p ? framebufferPostProcessing : framebuffer;
				current.bind();
				current.clear();
				((Texture) pp.getShader().getArgument("u_texture")).setTextureID(p ? tex0 : tex1);
				((Texture) pp.getShader().getArgument("u_depthTexture")).setTextureID(p ? tex0depth : tex1depth);
				pp.getShader().render();
				current.unbind();
				p = !p;
			}
		}
		glBindTexture(GL_TEXTURE_2D, p ? tex0 : tex1);
		glViewport(0, 0, width, height);
		target.render();
	}
}