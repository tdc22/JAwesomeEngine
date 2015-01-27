package utils;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT_BIT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glViewport;
import game.StandardGame;

import java.nio.IntBuffer;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class RenderToTexture {
	StandardGame game;
	// boolean usefbo;
	int framebufferID, colorTextureID, depthRenderBufferID;
	int width = 512;
	int height = 512;
	IntBuffer imageData;

	public RenderToTexture(StandardGame game) {
		this.game = game;

		framebufferID = glGenFramebuffersEXT();
		colorTextureID = glGenTextures();
		depthRenderBufferID = glGenRenderbuffersEXT();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 512, 512, 0, GL_RGBA, GL_INT,
				(java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
				GL_TEXTURE_2D, colorTextureID, 0);

		// initialize depth renderbuffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
				GL14.GL_DEPTH_COMPONENT24, 512, 512);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
				GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
				depthRenderBufferID);

		int framebuffer = EXTFramebufferObject
				.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
		switch (framebuffer) {
		case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
			break;
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ framebufferID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
		default:
			throw new RuntimeException(
					"Unexpected reply from glCheckFramebufferStatusEXT: "
							+ framebuffer);
		}

		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public IntBuffer getData() {
		return imageData;
	}

	public Texture getTexture() {
		return new Texture(colorTextureID);
	}

	public int getTextureID() {
		return colorTextureID;
	}

	public void updateTexture() {
		// glPushMatrix();
		//
		// glPushAttrib(GL_VIEWPORT_BIT);
		// glViewport(0, 0, width, height);
		// glBindTexture(GL_TEXTURE_2D, 0);
		// glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		// glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);
		// glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
		// glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// glLoadIdentity();
		// // GLU.gluLookAt(pos.x, pos.y, pos.z, center.x, center.y, center.z,
		// // up.x, up.y, up.z);
		//
		// // GL11.glTranslatef(10, 10, 10);
		// game.render();
		//
		// glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		//
		// glPopAttrib();
		//
		// glPopMatrix();
		//
		// int size = width * height * 16;
		// glBindTexture(GL_TEXTURE_2D, colorTextureID);
		// ByteBuffer tempData = ByteBuffer.allocateDirect(size);
		// glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, tempData);
		// glBindTexture(GL_TEXTURE_2D, 0);

		// glViewport (0, 0, 512, 512); // set The Current Viewport to the fbo
		// size
		// GL11.glViewport(0, 0, 512, 512);

		// GL11.glDisable(GL_TEXTURE_2D);
		glDisable(GL_TEXTURE_2D);
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, 512, 512);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		// glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);
		// glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
		// GL_TEXTURE_2D, colorTextureID, 0);
		// glPushAttrib(GL_VIEWPORT_BIT);
		// glViewport( 0, 0, 512, 512 );
		// glClear(GL_COLOR_BUFFER_BIT);
		// glLoadIdentity();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(2, 0, 0);
		GL11.glVertex3f(2, 2, 0);
		GL11.glVertex3f(0, 2, 0);
		GL11.glEnd();
		game.render();

		// glEnable(GL_TEXTURE_2D); // enable texturing
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		GL11.glPopAttrib();
		glEnable(GL_TEXTURE_2D);
	}
}
