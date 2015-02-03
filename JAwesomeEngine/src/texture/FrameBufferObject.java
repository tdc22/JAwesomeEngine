package texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
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
import game.Camera;
import game.StandardGame;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL14;

public class FrameBufferObject {
	StandardGame game;
	Camera cam;
	int frameBufferID, msFrameBufferID, colorBufferID, msColorBufferID,
			depthBufferID, msDepthBufferID, samples;
	int width, height;
	IntBuffer imageData;
	boolean multisampled;

	public FrameBufferObject(StandardGame game, Camera cam) {
		init(game, cam, 1024, 1024, 0);
	}

	public FrameBufferObject(StandardGame game, Camera cam, int width,
			int height) {
		init(game, cam, width, height, 0);
	}

	public FrameBufferObject(StandardGame game, Camera cam, int width,
			int height, int samples) {
		init(game, cam, width, height, samples);
	}

	private void init(StandardGame game, Camera cam, int width, int height,
			int samples) {
		this.game = game;
		this.cam = cam;
		this.width = width;
		this.height = height;
		this.samples = samples;

		multisampled = samples > 0;

		frameBufferID = glGenFramebuffers();
		colorBufferID = glGenTextures();
		depthBufferID = glGenRenderbuffers();

		if (multisampled) {
			glEnable(GL_MULTISAMPLE);
			msFrameBufferID = glGenFramebuffers();

			// Multi sample colorbuffer
			msColorBufferID = glGenRenderbuffers();
			glBindRenderbuffer(GL_RENDERBUFFER, msColorBufferID);
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples,
					GL_RGBA8, width, height);

			// Multi sample depthbuffer
			msDepthBufferID = glGenRenderbuffers();
			glBindRenderbuffer(GL_RENDERBUFFER, msDepthBufferID);
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples,
					GL_DEPTH_COMPONENT, width, height);

			// Attach them
			glBindFramebuffer(GL_FRAMEBUFFER, msFrameBufferID);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
					GL_RENDERBUFFER, msColorBufferID);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
					GL_RENDERBUFFER, msDepthBufferID);

			// Normal colorbuffer
			glBindTexture(GL_TEXTURE_2D, colorBufferID);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA,
					GL_INT, (java.nio.ByteBuffer) null);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
					GL_TEXTURE_2D, colorBufferID, 0);

			glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
					GL_TEXTURE_2D, colorBufferID, 0);

		} else {
			// color texture
			glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);

			glBindTexture(GL_TEXTURE_2D, colorBufferID);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA,
					GL_INT, (java.nio.ByteBuffer) null);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
					GL_TEXTURE_2D, colorBufferID, 0);

			// depth renderbuffer
			glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
			glRenderbufferStorage(GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24,
					width, height);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
					GL_RENDERBUFFER, depthBufferID);
		}

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
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getFramebufferID() {
		return frameBufferID;
	}

	public IntBuffer getData() {
		return imageData;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Texture getTexture() {
		return new Texture(colorBufferID);
	}

	public int getTextureID() {
		return colorBufferID;
	}

	public int getSamples() {
		return samples;
	}

	public boolean isMultisampled() {
		return multisampled;
	}

	public void begin() {
		glDisable(GL_TEXTURE_2D);
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glBindTexture(GL_TEXTURE_2D, 0);

		if (multisampled) {
			glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
			glClear(GL_COLOR_BUFFER_BIT);
			glBindFramebuffer(GL_FRAMEBUFFER, msFrameBufferID);
		} else
			glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT
				| GL_STENCIL_BUFFER_BIT);
		cam.begin();
	}

	public void updateTexture() {
		begin();
		game.render();
		end();
	}

	public void end() {
		cam.end();

		if (multisampled) {
			glBindFramebuffer(GL_READ_FRAMEBUFFER, msFrameBufferID);
			glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferID);

			glBlitFramebuffer(0, 0, width, height, 0, 0, width, height,
					GL_COLOR_BUFFER_BIT, GL_NEAREST);

			// glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
			// glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		}

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glPopAttrib();
		glEnable(GL_TEXTURE_2D);
	}

	public void delete() {
		glDeleteRenderbuffers(depthBufferID);
		glDeleteTextures(colorBufferID);
		glDeleteFramebuffers(frameBufferID);
		if (multisampled) {
			glDeleteTextures(msDepthBufferID);
			glDeleteFramebuffers(msColorBufferID);
			glDeleteTextures(msFrameBufferID);
		}
	}
}
