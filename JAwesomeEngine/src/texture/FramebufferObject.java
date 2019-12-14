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
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import game.Layer;
import objects.Camera;
import objects.ViewFrustum;
import utils.DefaultValues;

public class FramebufferObject {
	Layer render;
	int frameBufferID, colorRBID, depthRBID, normalRBID, samples;
	Texture colorTexture, depthTexture, normalTexture;
	int width, height;
	IntBuffer imageData;
	FloatBuffer viewTemp, projectionTemp;
	boolean useCam, useFrustum, renderColor, renderDepth, renderNormal, renderColorToTexture, renderDepthToTexture,
			renderNormalToTexture;
	Camera cam;
	ViewFrustum frustum;

	public FramebufferObject(Layer render) {
		init(render, DefaultValues.DEFAULT_FRAMEBUFFER_RESOLUTION_X, DefaultValues.DEFAULT_FRAMEBUFFER_RESOLUTION_Y,
				DefaultValues.DEFAULT_FRAMEBUFFER_SAMPLES, null, null, null,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL, DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height) {
		init(render, width, height, DefaultValues.DEFAULT_FRAMEBUFFER_SAMPLES, null, null, null,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL, DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples) {
		init(render, width, height, samples, null, null, null, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, boolean renderColor, boolean renderDepth,
			boolean renderNormal) {
		init(render, width, height, samples, null, null, null, renderColor, renderDepth, renderNormal,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, boolean renderColor, boolean renderDepth,
			boolean renderNormal, boolean renderColorToTexture, boolean renderDepthToTexture,
			boolean renderNormalToTexture) {
		init(render, width, height, samples, null, null, null, renderColor, renderDepth, renderNormal,
				renderColorToTexture, renderDepthToTexture, renderNormalToTexture);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, Camera cam) {
		init(render, width, height, samples, cam, null, null, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, Camera cam, boolean renderColor,
			boolean renderDepth, boolean renderNormal) {
		init(render, width, height, samples, cam, null, null, renderColor, renderDepth, renderNormal,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, Camera cam, boolean renderColor,
			boolean renderDepth, boolean renderNormal, boolean renderColorToTexture, boolean renderDepthToTexture,
			boolean renderNormalToTexture) {
		init(render, width, height, samples, cam, null, null, renderColor, renderDepth, renderNormal,
				renderColorToTexture, renderDepthToTexture, renderNormalToTexture);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, Camera cam, Texture colorbuffer) {
		init(render, width, height, samples, cam, colorbuffer, null, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, Camera cam, ViewFrustum frustum) {
		init(render, width, height, samples, cam, null, frustum, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public FramebufferObject(Layer render, int width, int height, int samples, Camera cam, Texture colorbuffer,
			ViewFrustum frustum) {
		init(render, width, height, samples, cam, colorbuffer, frustum, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_COLOR,
				DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_DEPTH, DefaultValues.DEFAULT_FRAMEBUFFER_RENDER_NORMAL,
				DefaultValues.DEFAULT_FRAMEBUFFER_COLOR_TEXTURE, DefaultValues.DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE,
				DefaultValues.DEFAULT_FRAMEBUFFER_NORMAL_TEXTURE);
	}

	public void begin() {
		glBindTexture(GL_TEXTURE_2D, 0);

		// System.out.println(useCam + "; " + useFrustum);
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
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glViewport(0, 0, width, height);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}

	public void copyTo(FramebufferObject target) {
		copyTo(target.getFramebufferID(), target.getWidth(), target.getHeight());
	}

	public void copyTo(int targetFramebufferID) {
		copyTo(targetFramebufferID, getWidth(), getHeight());
	}

	public void copyTo(int targetFramebufferID, int w, int h) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferID);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, targetFramebufferID);
		if (renderColor || renderDepth) {
			glReadBuffer(GL_COLOR_ATTACHMENT0);
			glDrawBuffer(GL_COLOR_ATTACHMENT0);
			glBlitFramebuffer(0, 0, width, height, 0, 0, w, h, GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT,
					(w == width && h == height) ? GL_NEAREST : GL_LINEAR);
		}
		if (renderNormal) {
			glReadBuffer(GL_COLOR_ATTACHMENT1);
			glDrawBuffer(GL_COLOR_ATTACHMENT1);
			glBlitFramebuffer(0, 0, width, height, 0, 0, w, h, GL_COLOR_BUFFER_BIT,
					(w == width && h == height) ? GL_NEAREST : GL_LINEAR);
		}
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}

	public void delete() {
		if (useCam)
			cam.delete();
		if (renderNormal) {
			if (renderNormalToTexture) {
				normalTexture.delete();
			} else {
				glDeleteRenderbuffers(normalRBID);
			}
		}
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

	public Texture getColorTexture() {
		return colorTexture;
	}

	public Texture getDepthTexture() {
		return depthTexture;
	}

	public Texture getNormalTexture() {
		return normalTexture;
	}

	public IntBuffer getData() {
		return imageData;
	}

	public Layer getRenderedLayer() {
		return render;
	}

	public int getNormalTextureID() {
		return normalTexture.getTextureID();
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

	public void setRenderedLayer(Layer layer) {
		this.render = layer;
	}

	protected void init(Layer render, int width, int height, int samples, Camera camera, Texture colorbuffer,
			ViewFrustum frustum, boolean renderColor, boolean renderDepth, boolean renderNormal,
			boolean renderColorToTexture, boolean renderDepthToTexture, boolean renderNormalToTexture) {
		this.render = render;
		this.width = width;
		this.height = height;
		this.samples = samples;
		this.renderColor = renderColor;
		this.renderDepth = renderDepth;
		this.renderNormal = renderNormal;
		this.renderColorToTexture = renderColorToTexture;
		this.renderDepthToTexture = renderDepthToTexture;
		this.renderNormalToTexture = renderNormalToTexture;

		if (useCam = (camera != null))
			this.cam = camera;
		if (useFrustum = (frustum != null))
			this.frustum = frustum;

		if (samples > 0)
			glEnable(GL_MULTISAMPLE);

		frameBufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer drawColorBuffers = stack.callocInt(2);

			// Colorbuffer
			if (renderColor) {
				if (renderColorToTexture) {
					colorTexture = attachColorTexture(colorbuffer, GL_COLOR_ATTACHMENT0, samples);
				} else {
					colorRBID = attachColorRenderbuffer(samples, GL_COLOR_ATTACHMENT0);
				}
				drawColorBuffers.put(GL_COLOR_ATTACHMENT0);
			}

			// Depthbuffer
			if (renderDepth) {
				if (renderDepthToTexture) {
					depthTexture = attachDepthTexture(samples);
				} else {
					depthRBID = attachDepthRenderbuffer(samples);
				}
			}

			// Normalbuffer
			if (renderNormal) {
				if (renderNormalToTexture) {
					normalTexture = attachColorTexture(null, GL_COLOR_ATTACHMENT1, samples);
				} else {
					normalRBID = attachColorRenderbuffer(GL_COLOR_ATTACHMENT1, samples);
				}
				drawColorBuffers.put(GL_COLOR_ATTACHMENT1);
			}

			drawColorBuffers.flip();
			glDrawBuffers(drawColorBuffers);
		}

		checkFramebufferStatus();

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
		if (renderNormal) {
			if (renderNormalToTexture) {
				normalTexture.unbind();
			} else {
				glBindRenderbuffer(GL_RENDERBUFFER, 0);
			}
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	protected Texture attachColorTexture(Texture texturebuffer, int attachmentId, int textureSamples) {
		Texture texture = new Texture();
		boolean multisampledTexture = textureSamples > 0;
		if (texturebuffer == null) {
			texture.setTextureType(multisampledTexture ? GL_TEXTURE_2D_MULTISAMPLE : GL_TEXTURE_2D);
			texture.bind();
			if (multisampledTexture) {
				glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, textureSamples, GL_RGBA, width, height, true);
			} else {
				int textureType = texture.getTextureType();
				glTexParameteri(textureType, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(textureType, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				glTexParameteri(textureType, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
				glTexParameteri(textureType, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
						(java.nio.ByteBuffer) null);
			}
		} else {
			texture = texturebuffer;
		}
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentId, texture.getTextureType(), texture.getTextureID(), 0);
		return texture;
	}

	protected int attachColorRenderbuffer(int attachmentId, int textureSamples) {
		int textureRenderbufferID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, textureRenderbufferID);
		if (textureSamples > 0) {
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, textureSamples, GL_RGBA, width, height);
		} else {
			glRenderbufferStorage(GL_RENDERBUFFER, GL_RGBA, width, height);
		}
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachmentId, GL_RENDERBUFFER, textureRenderbufferID);
		return textureRenderbufferID;
	}

	protected Texture attachDepthTexture(int textureSamples) {
		depthTexture = new Texture();
		boolean multisampledDepthTexture = textureSamples > 0;
		depthTexture.setTextureType(multisampledDepthTexture ? GL_TEXTURE_2D_MULTISAMPLE : GL_TEXTURE_2D);
		depthTexture.bind();
		int textureType = depthTexture.getTextureType();
		if (multisampledDepthTexture) {
			glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, textureSamples, GL_DEPTH_COMPONENT, width, height, true);
		} else {
			glTexParameteri(textureType, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(textureType, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(textureType, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(textureType, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE,
					(java.nio.ByteBuffer) null);
		}
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, textureType, depthTexture.getTextureID(), 0);
		return depthTexture;
	}

	protected int attachDepthRenderbuffer(int textureSamples) {
		int depthRenderbufferID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthRenderbufferID);
		if (textureSamples > 0) {
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, textureSamples, GL_DEPTH_COMPONENT, width, height);
		} else {
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		}
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderbufferID);
		return depthRenderbufferID;
	}

	protected void checkFramebufferStatus() {
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

	public boolean isNormalRendered() {
		return renderNormal;
	}

	public boolean isNormalRenderedToTexture() {
		return renderNormalToTexture;
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void updateTexture() {
		begin();
		render.render();
		end();
	}

	public BufferedImage getColorTextureImage() {
		BufferedImage image;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer buffer = stack.calloc(width * height * 4);
			bind();
			// glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			unbind();
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int[] px = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int i = (x + (width * y)) * 4;
					int r = buffer.get(i) & 0xff;
					int g = buffer.get(i + 1) & 0xff;
					int b = buffer.get(i + 2) & 0xff;
					int a = buffer.get(i + 3) & 0xff;
					int argb = (a << 24) | (r << 16) | (g << 8) | b;
					int off = (x + width * (height - y - 1));
					px[off] = argb;
				}
			}
		}
		return image;
	}

	public Camera getCamera() {
		return cam;
	}

	public ViewFrustum getViewFrustum() {
		return frustum;
	}
}