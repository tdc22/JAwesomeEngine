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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import game.StandardGame;

import java.nio.IntBuffer;

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

		// framebufferID = glGenFramebuffersEXT();
		// colorTextureID = glGenTextures();
		// depthRenderBufferID = glGenRenderbuffersEXT();
		//
		// glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		// glBindTexture(GL_TEXTURE_2D, colorTextureID);
		// glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		// glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA,
		// GL_INT, (java.nio.ByteBuffer) null);
		// glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
		// GL_COLOR_ATTACHMENT0_EXT,
		// GL_TEXTURE_2D, colorTextureID, 0);
		//
		// // initialize depth renderbuffer
		// glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);
		// glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
		// GL14.GL_DEPTH_COMPONENT24, width, height);
		// glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
		// GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
		// depthRenderBufferID);
		//
		// glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
		// glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		// glBindTexture(GL_TEXTURE_2D, 0);

		framebufferID = glGenFramebuffersEXT(); // create a new framebuffer
		colorTextureID = glGenTextures(); // and a new texture used as a color
											// buffer
		depthRenderBufferID = glGenRenderbuffersEXT(); // And finally a new
														// depthbuffer

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); // switch to
																	// the new
																	// framebuffer

		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, colorTextureID); // Bind the colorbuffer
														// texture
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // make
																			// it
																			// linear
																			// filterd
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 512, 512, 0, GL_RGBA, GL_INT,
				(java.nio.ByteBuffer) null); // Create the texture data
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
				GL_TEXTURE_2D, colorTextureID, 0); // attach it to the
													// framebuffer

		// initialize depth renderbuffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind
																			// the
																			// depth
																			// renderbuffer
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
				GL14.GL_DEPTH_COMPONENT24, 512, 512); // get the data space for
														// it
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
				GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
				depthRenderBufferID); // bind it to the renderbuffer

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
	}

	public IntBuffer getData() {
		return imageData;
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

		glBindTexture(GL_TEXTURE_2D, 0); // unlink textures because if we dont
											// it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); // switch to
																	// rendering
																	// on our
																	// FBO

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Screen And
															// Depth Buffer on
															// the fbo to red
		glLoadIdentity();

		game.render();

		glEnable(GL_TEXTURE_2D); // enable texturing
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // switch to rendering on
														// the framebuffer
	}
}
