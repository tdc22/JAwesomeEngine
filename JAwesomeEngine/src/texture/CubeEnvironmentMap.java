package texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import game.Camera;
import game.Debugger;
import game.StandardGame;
import vector.Vector3f;

public class CubeEnvironmentMap {
	Vector3f pos;
	FramebufferObject top, bottom, front, back, right, left;
	Texture cubemap;
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
		return cubemap.getTextureID();
	}

	public int getTextureWidth() {
		return width;
	}

	private void init(StandardGame game, Vector3f pos, int resX, int resY) {
		this.width = resX;
		this.height = resY;

		cubemap = new CubeMap();

		top = new FramebufferObject(game, resX, resY, 0,
				new Camera(pos, 0, 90), new Texture(cubemap.getTextureID(),
						GL_TEXTURE_CUBE_MAP_POSITIVE_Y));
		bottom = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 0,
				-90), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_NEGATIVE_Y));
		front = new FramebufferObject(game, resX, resY, 0,
				new Camera(pos, 0, 0), new Texture(cubemap.getTextureID(),
						GL_TEXTURE_CUBE_MAP_POSITIVE_X));
		back = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 180,
				0), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_NEGATIVE_X));
		left = new FramebufferObject(game, resX, resY, 0,
				new Camera(pos, 90, 0), new Texture(cubemap.getTextureID(),
						GL_TEXTURE_CUBE_MAP_POSITIVE_Z));
		right = new FramebufferObject(game, resX, resY, 0, new Camera(pos, -90,
				0), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_NEGATIVE_Z));
	}

	public void updateTexture() {
		glGetError(); // TODO: REMOVE ALL
		top.updateTexture();
		bottom.updateTexture();
		front.updateTexture();
		back.updateTexture();
		left.updateTexture();
		right.updateTexture();

		if (glGetError() != GL_NO_ERROR)
			System.out.println("a");
		glEnable(GL_TEXTURE_CUBE_MAP); // TODO: Delete
		cubemap.bind();
		if (glGetError() != GL_NO_ERROR)
			System.out.println("b");

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S,
				GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T,
				GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R,
				GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_BASE_LEVEL, 0);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAX_LEVEL, 0);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		if (glGetError() != GL_NO_ERROR)
			System.out.println("c");

		// TODO: check formats GL_RGBA and GL_RGBA8 etc.
		// glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, width,
		// height,
		// 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		// glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGB8, width,
		// height,
		// 0, GL_RGB8, GL_UNSIGNED_BYTE, back.getTextureID());
		// glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGB8, width,
		// height,
		// 0, GL_RGB8, GL_UNSIGNED_BYTE, top.getTextureID());
		// glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGB8, width,
		// height,
		// 0, GL_RGB8, GL_UNSIGNED_BYTE, bottom.getTextureID());
		// glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGB8, width,
		// height,
		// 0, GL_RGB8, GL_UNSIGNED_BYTE, right.getTextureID());
		// glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGB8, width,
		// height,
		// 0, GL_RGB8, GL_UNSIGNED_BYTE, left.getTextureID());
		int a = 0;
		if ((a = glGetError()) != GL_NO_ERROR)
			System.out.println("d " + Debugger.getGLErrorName(a) + "; " + width
					+ "; " + height);

		cubemap.unbind();
		glDisable(GL_TEXTURE_CUBE_MAP);
		if (glGetError() != GL_NO_ERROR)
			System.out.println("e");
	}

	public FramebufferObject getFramebufferFront() {
		return back; // TODO: correct and add others
	}

	public void delete() {
		cubemap.delete();
		top.delete();
		bottom.delete();
		front.delete();
		back.delete();
		left.delete();
		right.delete();
	}

}
