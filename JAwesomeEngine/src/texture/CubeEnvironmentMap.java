package texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
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
import game.StandardGame;
import objects.Camera;
import utils.DefaultValues;
import utils.ViewFrustum;
import vector.Vector3f;

public class CubeEnvironmentMap {
	Vector3f pos;
	FramebufferObject top, bottom, front, back, right, left;
	Texture cubemap;
	int width, height;
	ViewFrustum frustum;

	public CubeEnvironmentMap(StandardGame game, Vector3f pos) {
		init(game, pos, DefaultValues.DEFAULT_ENVIRONMENT_CUBEMAP_RESOLUTION_X,
				DefaultValues.DEFAULT_ENVIRONMENT_CUBEMAP_RESOLUTION_Y,
				DefaultValues.DEFAULT_ENVIRONMENT_CUBEMAP_ZNEAR,
				DefaultValues.DEFAULT_ENVIRONMENT_CUBEMAP_ZFAR);
	}

	public CubeEnvironmentMap(StandardGame game, Vector3f pos, int resX,
			int resY) {
		init(game, pos, resX, resY,
				DefaultValues.DEFAULT_ENVIRONMENT_CUBEMAP_ZNEAR,
				DefaultValues.DEFAULT_ENVIRONMENT_CUBEMAP_ZFAR);
	}

	public CubeEnvironmentMap(StandardGame game, Vector3f pos, int resX,
			int resY, float zNear, float zFar) {
		init(game, pos, resX, resY, zNear, zFar);
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

	private void init(StandardGame game, Vector3f pos, int resX, int resY,
			float zNear, float zFar) {
		this.width = resX;
		this.height = resY;
		frustum = new ViewFrustum(resX, resY, zNear, zFar, 90);

		cubemap = new CubeMap();
		cubemap.bind();

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

		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, width, height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, width, height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, width, height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, width, height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, width, height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, width, height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);

		top = new FramebufferObject(game, resX, resY, 0,
				new Camera(pos, 0, 90), new Texture(cubemap.getTextureID(),
						GL_TEXTURE_CUBE_MAP_POSITIVE_Y));
		bottom = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 0,
				-90), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_NEGATIVE_Y));
		left = new FramebufferObject(game, resX, resY, 0, new Camera(pos, -90,
				180), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_NEGATIVE_X));
		right = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 90,
				180), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_POSITIVE_X));
		front = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 180,
				180), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_NEGATIVE_Z));
		back = new FramebufferObject(game, resX, resY, 0, new Camera(pos, 0,
				180), new Texture(cubemap.getTextureID(),
				GL_TEXTURE_CUBE_MAP_POSITIVE_Z));

		cubemap.unbind();
	}

	public void updateTexture() {
		frustum.begin();

		top.updateTexture();
		bottom.updateTexture();
		front.updateTexture();
		back.updateTexture();
		left.updateTexture();
		right.updateTexture();

		frustum.end();
	}

	public FramebufferObject getFramebufferTop() {
		return top;
	}

	public FramebufferObject getFramebufferBottom() {
		return bottom;
	}

	public FramebufferObject getFramebufferFront() {
		return front;
	}

	public FramebufferObject getFramebufferBack() {
		return back;
	}

	public FramebufferObject getFramebufferLeft() {
		return left;
	}

	public FramebufferObject getFramebufferRight() {
		return right;
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
