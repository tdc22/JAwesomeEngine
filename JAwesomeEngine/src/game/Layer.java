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
import texture.FramebufferObjectMultisample;
import utils.StringConstants;

public class Layer implements ViewProjection {
	List<Shader> shader;
	List<PostProcessingShader> postProcessing;
	protected FloatBuffer projectionMatrix, viewMatrix;
	FramebufferObjectMultisample framebufferMultisample;
	FramebufferObject framebufferPostProcessing;
	boolean active = true;

	public Layer() {
		shader = new ArrayList<Shader>();
		postProcessing = new ArrayList<PostProcessingShader>();
	}

	public void initLayer(int resX, int resY, int samples, boolean renderColor, boolean renderDepth,
			boolean renderNormal) {
		framebufferMultisample = new FramebufferObjectMultisample(this, resX, resY, samples, renderColor, renderDepth,
				renderNormal, renderColor, renderDepth, renderNormal);
		framebufferPostProcessing = new FramebufferObject(this, resX, resY, 0, renderColor, renderDepth, renderNormal,
				renderColor, renderDepth, renderNormal);
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
		s.addArgument(StringConstants.MVP_PROJECTION, projectionMatrix);
		s.addArgument(StringConstants.MVP_VIEW, new Matrix4f());
		s.addArgument(StringConstants.MVP_MODEL, new Matrix4f());
		shader.add(s);
	}

	public void removeShader(Shader s) {
		shader.remove(s);
	}

	public void applyPostProcessing(Shader target, int width, int height) {
		int iterations = 0;
		int tex0 = framebufferMultisample.getColorTextureID();
		int tex1 = framebufferPostProcessing.getColorTextureID();
		for (PostProcessingShader pp : postProcessing) {
			if (iterations % 2 == 0)
				pp.apply(framebufferMultisample, framebufferPostProcessing);
			else
				pp.apply(framebufferPostProcessing, framebufferMultisample);
			iterations += pp.getIterations();
		}
		glBindTexture(GL_TEXTURE_2D, (iterations % 2 == 0) ? tex0 : tex1);
		glViewport(0, 0, width, height);
		target.renderNoMatrix();
	}

	@Override
	public void setViewMatrix(FloatBuffer buffer) {
		viewMatrix = buffer;
		for (Shader s : shader) {
			s.setArgumentDirect(StringConstants.MVP_VIEW, buffer);
		}
	}

	@Override
	public void setProjectionMatrix(FloatBuffer buffer) {
		projectionMatrix = buffer;
		for (Shader s : shader) {
			s.setArgumentDirect(StringConstants.MVP_PROJECTION, projectionMatrix);
		}
	}

	@Override
	public void setViewProjectionMatrix(FloatBuffer viewBuffer, FloatBuffer projectionBuffer) {
		viewMatrix = viewBuffer;
		projectionMatrix = projectionBuffer;
		for (Shader s : shader) {
			s.setArgumentDirect(StringConstants.MVP_VIEW, viewBuffer);
			s.setArgumentDirect(StringConstants.MVP_PROJECTION, projectionMatrix);
		}
	}

	@Override
	public void setViewMatrix(Matrix4f matrix) {
		viewMatrix = storeMatrix(matrix);
		for (Shader s : shader) {
			s.setArgumentDirect(StringConstants.MVP_VIEW, viewMatrix);
		}
	}

	@Override
	public void setProjectionMatrix(Matrix4f matrix) {
		projectionMatrix = storeMatrix(matrix);
		for (Shader s : shader) {
			s.setArgumentDirect(StringConstants.MVP_PROJECTION, projectionMatrix);
		}
	}

	@Override
	public void setViewProjectionMatrix(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
		this.viewMatrix = storeMatrix(viewMatrix);
		this.projectionMatrix = storeMatrix(projectionMatrix);
		for (Shader s : shader) {
			s.setArgumentDirect(StringConstants.MVP_VIEW, viewMatrix);
			s.setArgumentDirect(StringConstants.MVP_PROJECTION, this.projectionMatrix);
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

	private final FloatBuffer buf = BufferUtils.createFloatBuffer(16);

	protected FloatBuffer storeMatrix(Matrix4f mat) {
		mat.store(buf);
		buf.flip();
		return buf;
	}

	public FramebufferObject getFramebuffer() {
		return framebufferMultisample;
	}

	public FramebufferObject getFramebufferPostProcessor() {
		return framebufferPostProcessing;
	}
}