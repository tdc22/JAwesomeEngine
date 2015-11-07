package game;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import matrix.Matrix4f;
import objects.ViewProjection;
import shader.PostProcessingShader;
import shader.Shader;
import texture.FramebufferObject;
import texture.Texture;

public class Layer implements ViewProjection {
	List<Shader> shader;
	List<PostProcessingShader> postProcessing;
	int postProcessingIterations = 20;
	protected FloatBuffer projectionMatrix, viewMatrix;
	FramebufferObject framebufferMultisample, framebuffer, framebufferPostProcessing;
	boolean active = true;

	public Layer() {
		shader = new ArrayList<Shader>();
		postProcessing = new ArrayList<PostProcessingShader>();
	}

	public void initLayer(int resX, int resY, int samples) {
		framebufferMultisample = new FramebufferObject(this, resX, resY, samples);
		framebuffer = new FramebufferObject(this, resX, resY, 0);
		framebufferPostProcessing = new FramebufferObject(this, resX, resY, 0);
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

	public List<Shader> getShader() {
		return shader;
	}

	public List<PostProcessingShader> getPostProcessingShader() {
		return postProcessing;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void addShader(Shader s) {
		s.addArgument("projection", projectionMatrix);
		s.addArgument("view", new Matrix4f());
		s.addArgument("model", new Matrix4f());
		shader.add(s);
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
		target.renderNoMatrix();
	}

	@Override
	public void setViewMatrix(FloatBuffer buffer) {
		viewMatrix = buffer;
		for (Shader s : shader) {
			s.setArgumentDirect("view", buffer);
		}
	}

	@Override
	public void setProjectionMatrix(FloatBuffer buffer) {
		projectionMatrix = buffer;
		for (Shader s : shader) {
			s.setArgumentDirect("projection", projectionMatrix);
		}
	}

	@Override
	public void setViewProjectionMatrix(FloatBuffer viewBuffer, FloatBuffer projectionBuffer) {
		viewMatrix = viewBuffer;
		projectionMatrix = projectionBuffer;
		for (Shader s : shader) {
			s.setArgumentDirect("view", viewBuffer);
			s.setArgumentDirect("projection", projectionMatrix);
		}
	}

	@Override
	public void setViewMatrix(Matrix4f matrix) {
		viewMatrix = storeMatrix(matrix);
		for (Shader s : shader) {
			s.setArgumentDirect("view", viewMatrix);
		}
	}

	@Override
	public void setProjectionMatrix(Matrix4f matrix) {
		projectionMatrix = storeMatrix(matrix);
		for (Shader s : shader) {
			s.setArgumentDirect("projection", projectionMatrix);
		}
	}

	@Override
	public void setViewProjectionMatrix(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
		this.viewMatrix = storeMatrix(viewMatrix);
		this.projectionMatrix = storeMatrix(projectionMatrix);
		for (Shader s : shader) {
			s.setArgumentDirect("view", viewMatrix);
			s.setArgumentDirect("projection", this.projectionMatrix);
		}
	}

	@Override
	public FloatBuffer getViewMatrixBuffer() {
		return viewMatrix;
	}

	@Override
	public FloatBuffer getProjectionMatrixBuffer() {
		return projectionMatrix;
	}

	protected FloatBuffer storeMatrix(Matrix4f mat) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		mat.store(buf);
		buf.flip();
		return buf;
	}

	public FramebufferObject getFramebufferMultisample() {
		return framebufferMultisample;
	}

	public FramebufferObject getFramebuffer() {
		return framebuffer;
	}

	public FramebufferObject getFramebufferPostProcessor() {
		return framebufferPostProcessing;
	}
}