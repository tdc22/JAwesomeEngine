package texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import game.Camera;
import game.StandardGame;
import vector.Vector3f;

public class CubeEnvironmentMap {
	Vector3f pos;
	FramebufferObject top, bottom, front, back, right, left;
	int cubemapID;
	int width, height;

	public CubeEnvironmentMap(StandardGame game, Vector3f pos) {
		init(game, pos, 1024, 1024);
	}

	public CubeEnvironmentMap(StandardGame game, Vector3f pos, int resX,
			int resY) {
		init(game, pos, resX, resY);
	}

	public int getTextureHeight() {
		return height;
	}

	public int getTextureID() {
		return cubemapID;
	}

	public int getTextureWidth() {
		return width;
	}

	private void init(StandardGame game, Vector3f pos, int resX, int resY) {
		this.width = resX;
		this.height = resY;

		top = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 0, 90));
		bottom = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 0,
				-90));
		front = new FramebufferObject(game, resX, resY, 0,
				new Camera(pos, 0, 0));
		back = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 180,
				0));
		left = new FramebufferObject(game, resX, resY, 0,
				new Camera(pos, 90, 0));
		right = new FramebufferObject(game, resX, resY, 0, new Camera(pos, -90,
				0));

		cubemapID = glGenTextures();

		glDisable(GL_TEXTURE_2D);
		glEnable(GL_TEXTURE_CUBE_MAP);
		glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapID);

		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, width, height,
				0, GL_RGBA8, GL_UNSIGNED_BYTE, front.getTextureID());
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, width, height,
				0, GL_RGBA8, GL_UNSIGNED_BYTE, back.getTextureID());
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, width, height,
				0, GL_RGBA8, GL_UNSIGNED_BYTE, top.getTextureID());
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, width, height,
				0, GL_RGBA8, GL_UNSIGNED_BYTE, bottom.getTextureID());
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, width, height,
				0, GL_RGBA8, GL_UNSIGNED_BYTE, right.getTextureID());
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, width, height,
				0, GL_RGBA8, GL_UNSIGNED_BYTE, left.getTextureID());

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R,
				GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S,
				GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T,
				GL_CLAMP_TO_EDGE);

		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
		glDisable(GL_TEXTURE_CUBE_MAP);
		glEnable(GL_TEXTURE_2D);
	}

	public void updateTexture() {
		top.updateTexture();
		bottom.updateTexture();
		front.updateTexture();
		back.updateTexture();
		left.updateTexture();
		right.updateTexture();
	}

	public FramebufferObject getFramebufferFront() {
		return back; // TODO: correct and add others
	}

	public void delete() {
		glDeleteTextures(cubemapID);
		top.delete();
		bottom.delete();
		front.delete();
		back.delete();
		left.delete();
		right.delete();
	}

}
