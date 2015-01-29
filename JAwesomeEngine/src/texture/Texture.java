package texture;

import org.lwjgl.opengl.GL11;

public class Texture {
	int textureid;

	public Texture(int textureid) {
		this.textureid = textureid;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureid);
	}

	public int getTextureID() {
		return textureid;
	}

	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
