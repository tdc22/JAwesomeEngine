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
import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
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
import game.Camera;
import game.StandardGame;

import java.nio.IntBuffer;

public class FramebufferObject {
	StandardGame game;
	int frameBufferID, depthBufferID, samples;
	Texture colorBuffer;
	int width, height;
	IntBuffer imageData;
	boolean multisampled, useCam;
	Camera cam;

	public FramebufferObject(StandardGame game) {
		init(game, 1024, 1024, 0, null, null);
	}

	public FramebufferObject(StandardGame game, int width, int height) {
		init(game, width, height, 0, null, null);
	}

	public FramebufferObject(StandardGame game, int width, int height,
			int samples) {
		init(game, width, height, samples, null, null);
	}

	public FramebufferObject(StandardGame game, int width, int height,
			int samples, Camera cam) {
		init(game, width, height, samples, cam, null);
	}

	public FramebufferObject(StandardGame game, int width, int height,
			int samples, Camera cam, Texture colorbuffer) {
		init(game, width, height, samples, cam, colorbuffer);
	}

	public void begin() {
		glDisable(GL_TEXTURE_2D);
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glBindTexture(GL_TEXTURE_2D, 0);

		if (useCam)
			cam.begin();

		bind();
		clear();
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT
				| GL_STENCIL_BUFFER_BIT);
	}

	public void copyTo(FramebufferObject target) {
		copyTo(target.getFramebufferID(), target.getWidth(), target.getHeight());
	}

	public void copyTo(int framebufferID, int w, int h) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferID);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, framebufferID);
		glBlitFramebuffer(0, 0, width, height, 0, 0, w, h, GL_COLOR_BUFFER_BIT,
				(w == width && h == height) ? GL_NEAREST : GL_LINEAR);
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}

	public void delete() {
		glDeleteRenderbuffers(depthBufferID);
		colorBuffer.delete();
		glDeleteFramebuffers(frameBufferID);
	}

	public void end() {
		unbind();

		if (useCam)
			cam.end();

		glPopAttrib();
		glEnable(GL_TEXTURE_2D);
	}

	public IntBuffer getData() {
		return imageData;
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

	public int getTextureID() {
		return colorBuffer.getTextureID();
	}

	public int getWidth() {
		return width;
	}

	private void init(StandardGame game, int width, int height, int samples,
			Camera camera, Texture colorbuffer) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.samples = samples;

		if (camera != null) {
			this.cam = camera;
			useCam = true;
		}

		multisampled = samples > 0;

		frameBufferID = glGenFramebuffers();
		boolean newColorBuffer = false;
		if (colorbuffer == null) {
			newColorBuffer = true;
			colorBuffer = new Texture();
			colorBuffer.setTextureType(multisampled ? GL_TEXTURE_2D_MULTISAMPLE
					: GL_TEXTURE_2D);
		} else {
			colorBuffer = colorbuffer;
		}
		depthBufferID = glGenRenderbuffers();

		if (multisampled)
			glEnable(GL_MULTISAMPLE);

		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);

		// Colorbuffer
		int textureType = colorBuffer.getTextureType();
		if (newColorBuffer) {
			colorBuffer.bind();
			glTexParameteri(textureType, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(textureType, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(textureType, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(textureType, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			if (multisampled) {
				glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, samples,
						GL_RGBA, width, height, true);
			} else {
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
						GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
			}
		}

		// Depthbuffer
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		if (multisampled) {
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples,
					GL_DEPTH_COMPONENT, width, height);
		} else {
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width,
					height);
		}

		// Attach to Framebuffer
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
				textureType, colorBuffer.getTextureID(), 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, depthBufferID);

		int framebuffercheck = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		switch (framebuffercheck) {
		case GL_FRAMEBUFFER_COMPLETE:
			break;
		case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ frameBufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT exception");
		case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ frameBufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT exception");
		case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
			throw new RuntimeException(
					"FrameBuffer: "
							+ frameBufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER exception");
		case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
			throw new RuntimeException(
					"FrameBuffer: "
							+ frameBufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER exception");
		default:
			throw new RuntimeException(
					"Unexpected reply from glCheckFramebufferStatus: "
							+ framebuffercheck);
		}

		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		if (newColorBuffer)
			colorBuffer.unbind();
	}

	public boolean isMultisampled() {
		return multisampled;
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void updateTexture() {
		begin();
		game.render();
		end();
	}
}
