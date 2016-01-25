package texture;

import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import game.Layer;
import objects.Camera;
import utils.ViewFrustum;

public class FramebufferObjectMultisample extends FramebufferObject {
	int unsampledFramebufferID;
	Texture unsampledColorTexture, unsampledDepthTexture;
	int unsampledColorRBID, unsampledDepthRBID;

	public FramebufferObjectMultisample(Layer render) {
		super(render);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height) {
		super(render, width, height);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples) {
		super(render, width, height, samples);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam) {
		super(render, width, height, samples, cam);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			boolean renderColor, boolean renderDepth) {
		super(render, width, height, samples, cam, renderColor, renderDepth);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			boolean renderColor, boolean renderDepth, boolean renderColorToTexture, boolean renderDepthToTexture) {
		super(render, width, height, samples, cam, renderColor, renderDepth, renderColorToTexture,
				renderDepthToTexture);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			Texture colorbuffer) {
		super(render, width, height, samples, cam, colorbuffer);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			ViewFrustum frustum) {
		super(render, width, height, samples, cam, frustum);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			Texture colorbuffer, ViewFrustum frustum) {
		super(render, width, height, samples, cam, colorbuffer, frustum);
	}

	@Override
	public int getFramebufferID() {
		return unsampledFramebufferID;
	}

	@Override
	public Texture getColorTexture() {
		return unsampledColorTexture;
	}

	@Override
	public Texture getDepthTexture() {
		return unsampledDepthTexture;
	}

	@Override
	public int getColorTextureID() {
		return unsampledColorTexture.getTextureID();
	}

	@Override
	public int getDepthTextureID() {
		return unsampledDepthTexture.getTextureID();
	}

	@Override
	protected void init(Layer render, int width, int height, int samples, Camera camera, Texture colorbuffer,
			ViewFrustum frustum, boolean renderColor, boolean renderDepth, boolean renderColorToTexture,
			boolean renderDepthToTexture) {
		super.init(render, width, height, samples, camera, null, frustum, renderColor, renderDepth,
				renderColorToTexture, renderDepthToTexture);

		unsampledFramebufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, unsampledFramebufferID);

		// Colorbuffer
		if (renderColor) {
			if (renderColorToTexture) {
				unsampledColorTexture = attachColorTexture(colorbuffer, 0);
			} else {
				unsampledColorRBID = attachColorRenderbuffer(0);
			}
		}

		// Depthbuffer
		if (renderDepth) {
			if (renderDepthToTexture) {
				unsampledDepthTexture = attachDepthTexture(0);
			} else {
				unsampledDepthRBID = attachDepthRenderbuffer(0);
			}
		}

		checkFramebufferStatus();

		if (renderColor) {
			if (renderColorToTexture) {
				if (colorbuffer == null) {
					unsampledColorTexture.unbind();
				}
			} else {
				glBindRenderbuffer(GL_RENDERBUFFER, 0);
			}
		}
		if (renderDepth) {
			if (renderDepthToTexture) {
				unsampledDepthTexture.unbind();
			} else {
				glBindRenderbuffer(GL_RENDERBUFFER, 0);
			}
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	@Override
	public void unbind() {
		super.unbind();
		glBindFramebuffer(GL_FRAMEBUFFER, unsampledFramebufferID);
		clear();
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		copyTo(unsampledFramebufferID);
	}

	@Override
	public void delete() {
		super.delete();
		if (renderDepth) {
			if (renderDepthToTexture) {
				unsampledColorTexture.delete();
			} else {
				glDeleteRenderbuffers(unsampledDepthRBID);
			}
		}
		if (renderColor) {
			if (renderColorToTexture) {
				unsampledDepthTexture.delete();
			} else {
				glDeleteRenderbuffers(unsampledColorRBID);
			}
		}
		glDeleteFramebuffers(unsampledFramebufferID);
	}
}