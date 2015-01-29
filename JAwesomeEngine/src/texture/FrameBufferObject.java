package texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
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
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import game.Camera;
import game.StandardGame;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL14;

public class FrameBufferObject {
	StandardGame game;
	Camera cam;
	int framebufferID, colorTextureID, depthRenderBufferID;
	int width, height;
	IntBuffer imageData;

	public FrameBufferObject(StandardGame game, Camera cam) {
		init(game, cam, 1024, 1024);
	}

	public FrameBufferObject(StandardGame game, Camera cam, int width,
			int height) {
		init(game, cam, width, height);
	}

	private void init(StandardGame game, Camera cam, int width, int height) {
		this.game = game;
		this.cam = cam;
		this.width = width;
		this.height = height;

		framebufferID = glGenFramebuffers();
		colorTextureID = glGenTextures();
		depthRenderBufferID = glGenRenderbuffers();

		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);

		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA,
				GL_INT, (java.nio.ByteBuffer) null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
				GL_TEXTURE_2D, colorTextureID, 0);

		// initialize depth renderbuffer
		glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24,
				width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, depthRenderBufferID);

		int framebuffercheck = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		switch (framebuffercheck) {
		case GL_FRAMEBUFFER_COMPLETE:
			break;
		case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT exception");
		case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT exception");
		case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER exception");
		case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
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
		return new Texture(colorTextureID);
	}

	public int getTextureID() {
		return colorTextureID;
	}

	public void begin() {
		glDisable(GL_TEXTURE_2D);
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void updateTexture() {
		begin();
		cam.begin();
		game.render();
		cam.end();
		end();
	}

	public void end() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glPopAttrib();
		glEnable(GL_TEXTURE_2D);
	}
}
