package texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL30.glRenderbufferStorageMultisample;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import objects.Camera;
import objects.ViewProjection;
import utils.DefaultValues;
import utils.ViewFrustum;

public class FramebufferObject {
	ViewProjection render;
	int frameBufferID, colorRBID, depthRBID, samples;
	Texture colorTexture, depthTexture;
	int width, height;
	IntBuffer imageData;
	FloatBuffer viewTemp, projectionTemp;
	boolean multisampled, useCam, useFrustum, renderColor, renderDepth, renderColorToTexture, renderDepthToTexture;
	Camera cam;
	ViewFrustum frustum;

	public FramebufferObject(ViewProjection render) {
		init(render, DefaultValues.DEFAULT_FRAMEBUFFER_RESOLUTION_X, DefaultValues.DEFAULT_FRAMEBUFFER_RESOLUTION_Y,
				DefaultValues.DEFAULT_FRAMEBUFFER_SAMPLES, null, null, null,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public FramebufferObject(ViewProjection render, int width, int height) {
		init(render, width, height, DefaultValues.DEFAULT_FRAMEBUFFER_SAMPLES, null, null, null,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public FramebufferObject(ViewProjection render, int width, int height, int samples) {
		init(render, width, height, samples, null, null, null, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public FramebufferObject(ViewProjection render, int width, int height, int samples, Camera cam) {
		init(render, width, height, samples, cam, null, null, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public FramebufferObject(ViewProjection render, int width, int height, int samples, Camera cam, boolean renderColor,
			boolean renderDepth) {
		init(render, width, height, samples, cam, null, null, renderColor, renderDepth,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public FramebufferObject(ViewProjection render, int width, int height, int samples, Camera cam, boolean renderColor,
			boolean renderDepth, boolean renderColorToTexture, boolean renderDepthToTexture) {
		init(render, width, height, samples, cam, null, null, renderColor, renderDepth, renderColorToTexture,
				renderDepthToTexture);
	}

	public FramebufferObject(ViewProjection render, int width, int height, int samples, Camera cam,
			Texture colorbuffer) {
		init(render, width, height, samples, cam, colorbuffer, null, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public FramebufferObject(ViewProjection render, int width, int height, int samples, Camera cam, Texture colorbuffer,
			ViewFrustum frustum) {
		init(render, width, height, samples, cam, colorbuffer, frustum, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE);
	}

	public void begin() {
		glDisable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);

		if (useCam && useFrustum) {
			viewTemp = render.getViewMatrixBuffer();
			projectionTemp = render.getProjectionMatrixBuffer();
			render.setViewProjectionMatrix(cam.getMatrixBuffer(), frustum.getMatrixBuffer());
		} else if (useCam) {
			viewTemp = render.getViewMatrixBuffer();
			render.setViewMatrix(cam.getMatrixBuffer());
		} else if (useFrustum) {
			projectionTemp = render.getProjectionMatrixBuffer();
			render.setProjectionMatrix(frustum.getMatrixBuffer());
		}

		bind();
		clear();
	}

	public void end() {
		unbind();

		if (useCam && useFrustum) {
			render.setViewProjectionMatrix(viewTemp, projectionTemp);
		} else if (useCam) {
			render.setViewMatrix(viewTemp);
		} else if (useFrustum) {
			render.setProjectionMatrix(projectionTemp);
		}

		glEnable(GL_TEXTURE_2D);
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}

	public void copyTo(FramebufferObject target) {
		copyTo(target.getFramebufferID(), target.getWidth(), target.getHeight());
	}

	public void copyTo(int framebufferID, int w, int h) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferID);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, framebufferID);
		glBlitFramebuffer(0, 0, width, height, 0, 0, w, h, GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT,
				(w == width && h == height) ? GL_NEAREST : GL_LINEAR);
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}

	public void delete() {
		if (useCam)
			cam.delete();
		if (renderDepth) {
			if (renderDepthToTexture) {
				depthTexture.delete();
			} else {
				glDeleteRenderbuffers(depthRBID);
			}
		}
		if (renderColor) {
			if (renderColorToTexture) {
				colorTexture.delete();
			} else {
				glDeleteRenderbuffers(colorRBID);
			}
		}
		glDeleteFramebuffers(frameBufferID);
	}

	public int getColorTextureID() {
		return colorTexture.getTextureID();
	}

	public IntBuffer getData() {
		return imageData;
	}

	public int getDepthTextureID() {
		return depthTexture.getTextureID();
	}

	public int getFramebufferID() {
		return frameBufferID;
	}

	public int getHeight() {
		return height;
	}

	public int getSamples() {
		return samples;
	}

	public int getWidth() {
		return width;
	}

	private void init(ViewProjection render, int width, int height, int samples, Camera camera, Texture colorbuffer,
			ViewFrustum frustum, boolean renderColor, boolean renderDepth, boolean renderColorToTexture,
			boolean renderDepthToTexture) {
		this.render = render;
		this.width = width;
		this.height = height;
		this.samples = samples;
		this.renderColor = renderColor;
		this.renderDepth = renderDepth;
		this.renderColorToTexture = renderColorToTexture;
		this.renderDepthToTexture = renderDepthToTexture;

		if (useCam = (camera != null))
			this.cam = camera;
		if (useFrustum = (frustum != null))
			this.frustum = frustum;

		multisampled = samples > 0;
		if (multisampled)
			glEnable(GL_MULTISAMPLE);

		frameBufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);

		// Colorbuffer
		if (renderColor) {
			if (renderColorToTexture) {
				boolean newColorBuffer = colorbuffer == null;
				if (newColorBuffer) {
					colorTexture = new Texture();
					colorTexture.setTextureType(multisampled ? GL_TEXTURE_2D_MULTISAMPLE : GL_TEXTURE_2D);
				} else {
					colorTexture = colorbuffer;
				}
				int textureType = colorTexture.getTextureType();
				if (newColorBuffer) {
					colorTexture = new Texture();
					colorTexture.setTextureType(multisampled ? GL_TEXTURE_2D_MULTISAMPLE : GL_TEXTURE_2D);
					colorTexture.bind();
					if (multisampled) {
						glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, samples, GL_RGBA, width, height, true);
					} else {
						glTexParameteri(textureType, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
						glTexParameteri(textureType, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
						glTexParameteri(textureType, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
						glTexParameteri(textureType, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
						glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
								(java.nio.ByteBuffer) null);
					}
				}
				glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, textureType, colorTexture.getTextureID(),
						0);
			} else {
				colorRBID = glGenRenderbuffers();
				glBindRenderbuffer(GL_RENDERBUFFER, colorRBID);
				if (multisampled) {
					glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_RGBA, width, height);
				} else {
					glRenderbufferStorage(GL_RENDERBUFFER, GL_RGBA, width, height);
				}
				glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRBID);
			}
		}

		// Depthbuffer
		if (renderDepth) {
			if (renderDepthToTexture) {
				depthTexture = new Texture();
				depthTexture.setTextureType(multisampled ? GL_TEXTURE_2D_MULTISAMPLE : GL_TEXTURE_2D);
				depthTexture.bind();
				int textureType = depthTexture.getTextureType();
				if (multisampled) {
					glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, samples, GL_DEPTH_COMPONENT, width, height,
							true);
				} else {
					glTexParameteri(textureType, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
					glTexParameteri(textureType, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
					glTexParameteri(textureType, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
					glTexParameteri(textureType, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
					glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT,
							GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
				}
				glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, textureType, depthTexture.getTextureID(),
						0);
			} else {
				depthRBID = glGenRenderbuffers();
				glBindRenderbuffer(GL_RENDERBUFFER, depthRBID);
				if (multisampled) {
					glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_DEPTH_COMPONENT, width, height);
				} else {
					glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
				}
				glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRBID);
			}
		}

		int framebuffercheck = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		switch (framebuffercheck) {
		case GL_FRAMEBUFFER_COMPLETE:
			break;
		case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
			throw new RuntimeException(
					"FrameBuffer: " + frameBufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT exception");
		case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
			throw new RuntimeException("FrameBuffer: " + frameBufferID
					+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT exception");
		case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
			throw new RuntimeException(
					"FrameBuffer: " + frameBufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER exception");
		case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
			throw new RuntimeException(
					"FrameBuffer: " + frameBufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER exception");
		default:
			throw new RuntimeException("Unexpected reply from glCheckFramebufferStatus: " + framebuffercheck);
		}

		if (renderColor) {
			if (renderColorToTexture) {
				if (colorbuffer == null) {
					colorTexture.unbind();
				}
			} else {
				glBindRenderbuffer(GL_RENDERBUFFER, 0);
			}
		}
		if (renderDepth) {
			if (renderDepthToTexture) {
				depthTexture.unbind();
			} else {
				glBindRenderbuffer(GL_RENDERBUFFER, 0);
			}
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public boolean isColorRendered() {
		return renderColor;
	}

	public boolean isColorRenderedToTexture() {
		return renderColorToTexture;
	}

	public boolean isDepthRendered() {
		return renderDepth;
	}

	public boolean isDepthRenderedToTexture() {
		return renderDepthToTexture;
	}

	public boolean isMultisampled() {
		return multisampled;
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void updateTexture() {
		begin();
		render.render();
		end();
	}
}