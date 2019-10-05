package texture;

import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import game.Layer;
import objects.Camera;
import objects.ViewFrustum;

public class FramebufferObjectMultisample extends FramebufferObject {
	int unsampledFramebufferID;
	Texture unsampledColorTexture, unsampledDepthTexture, unsampledNormalTexture;
	int unsampledColorRBID, unsampledDepthRBID, unsampledNormalRBID;

	public FramebufferObjectMultisample(Layer render) {
		super(render);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height) {
		super(render, width, height);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples) {
		super(render, width, height, samples);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, boolean renderColor,
			boolean renderDepth, boolean renderNormal) {
		super(render, width, height, samples, null, renderColor, renderDepth, renderNormal);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, boolean renderColor,
			boolean renderDepth, boolean renderNormal, boolean renderColorToTexture, boolean renderDepthToTexture,
			boolean renderNormalToTexture) {
		super(render, width, height, samples, null, renderColor, renderDepth, renderNormal, renderColorToTexture,
				renderDepthToTexture, renderNormalToTexture);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam) {
		super(render, width, height, samples, cam);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			boolean renderColor, boolean renderDepth, boolean renderNormal) {
		super(render, width, height, samples, cam, renderColor, renderDepth, renderNormal);
	}

	public FramebufferObjectMultisample(Layer render, int width, int height, int samples, Camera cam,
			boolean renderColor, boolean renderDepth, boolean renderNormal, boolean renderColorToTexture,
			boolean renderDepthToTexture, boolean renderNormalToTexture) {
		super(render, width, height, samples, cam, renderColor, renderDepth, renderNormal, renderColorToTexture,
				renderDepthToTexture, renderNormalToTexture);
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
	public Texture getNormalTexture() {
		return unsampledNormalTexture;
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
	public int getNormalTextureID() {
		return unsampledNormalTexture.getTextureID();
	}

	@Override
	protected void init(Layer render, int width, int height, int samples, Camera camera, Texture colorbuffer,
			ViewFrustum frustum, boolean renderColor, boolean renderDepth, boolean renderNormal,
			boolean renderColorToTexture, boolean renderDepthToTexture, boolean renderNormalToTexture) {
		super.init(render, width, height, samples, camera, null, frustum, renderColor, renderDepth, renderNormal,
				renderColorToTexture, renderDepthToTexture, renderNormalToTexture);

		unsampledFramebufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, unsampledFramebufferID);

		// Colorbuffer
		if (renderColor) {
			if (renderColorToTexture) {
				unsampledColorTexture = attachColorTexture(colorbuffer, GL_COLOR_ATTACHMENT0, 0);
			} else {
				unsampledColorRBID = attachColorRenderbuffer(GL_COLOR_ATTACHMENT0, 0);
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

		// Normalbuffer
		if (renderNormal) {
			if (renderNormalToTexture) {
				unsampledNormalTexture = attachColorTexture(null, GL_COLOR_ATTACHMENT1, 0);
			} else {
				unsampledNormalRBID = attachColorRenderbuffer(GL_COLOR_ATTACHMENT1, 0);
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
		if (renderNormal) {
			if (renderNormalToTexture) {
				unsampledNormalTexture.unbind();
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
		if (renderNormal) {
			if (renderNormalToTexture) {
				unsampledNormalTexture.delete();
			} else {
				glDeleteRenderbuffers(unsampledNormalRBID);
			}
		}
		if (renderDepth) {
			if (renderDepthToTexture) {
				unsampledDepthTexture.delete();
			} else {
				glDeleteRenderbuffers(unsampledDepthRBID);
			}
		}
		if (renderColor) {
			if (renderColorToTexture) {
				unsampledColorTexture.delete();
			} else {
				glDeleteRenderbuffers(unsampledColorRBID);
			}
		}
		glDeleteFramebuffers(unsampledFramebufferID);
	}
}