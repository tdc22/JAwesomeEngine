package texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;

public class Texture {
	int textureid;

	public Texture() {
		this.textureid = glGenTextures();
	}

	public Texture(int textureid) {
		this.textureid = textureid;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureid);
	}

	public int getTextureID() {
		return textureid;
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void delete() {
		glDeleteTextures(textureid);
	}
}
