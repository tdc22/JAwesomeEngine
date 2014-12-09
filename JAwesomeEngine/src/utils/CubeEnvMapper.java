package utils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import game.StandardGame;

import org.lwjgl.opengl.ARBTextureCubeMap;

import vector.Vector3f;

public class CubeEnvMapper {
	Vector3f pos;
	RenderToTexture top, bottom, front, back, right, left;
	int width = 800;
	int height = 600;
	int cubemap;

	public CubeEnvMapper(StandardGame game, Vector3f pos) {
		top = new RenderToTexture(game, pos, new Vector3f(0, 1, 0),
				new Vector3f(0, 0, 1));
		bottom = new RenderToTexture(game, pos, new Vector3f(0, -1, 0),
				new Vector3f(0, 0, -1));
		front = new RenderToTexture(game, pos, new Vector3f(0, 0, 1),
				new Vector3f(0, 1, 0));
		back = new RenderToTexture(game, pos, new Vector3f(0, 0, -1),
				new Vector3f(0, 1, 0));
		right = new RenderToTexture(game, pos, new Vector3f(1, 0, 0),
				new Vector3f(0, 1, 0));
		left = new RenderToTexture(game, pos, new Vector3f(-1, 0, 0),
				new Vector3f(0, 1, 0));
	}

	public int getTextureID() {
		return cubemap;
	}

	public void updateTexture() {
		top.updateTexture();
		bottom.updateTexture();
		front.updateTexture();
		back.updateTexture();
		left.updateTexture();
		right.updateTexture();

		cubemap = glGenTextures(); // Make texture object
		glBindTexture(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_ARB, cubemap); // Make
																			// it
																			// a
																			// cubemap

		glTexImage2D(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_POSITIVE_X_ARB, 0,
				GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
				front.getTextureID()); // postive x
		glTexImage2D(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_NEGATIVE_X_ARB, 0,
				GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
				back.getTextureID()); // negative x
		glTexImage2D(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_POSITIVE_Y_ARB, 0,
				GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
				top.getTextureID()); // postive y
		glTexImage2D(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y_ARB, 0,
				GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
				bottom.getTextureID()); // negative y
		glTexImage2D(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_POSITIVE_Z_ARB, 0,
				GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
				right.getTextureID()); // positive z
		glTexImage2D(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z_ARB, 0,
				GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE,
				left.getTextureID()); // negative z

		glTexParameteri(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_ARB,
				GL_TEXTURE_MAG_FILTER, GL_LINEAR); // Set far filtering mode
		glTexParameteri(ARBTextureCubeMap.GL_TEXTURE_CUBE_MAP_ARB,
				GL_TEXTURE_MIN_FILTER, GL_LINEAR); // Set near filtering mode
	}
}
