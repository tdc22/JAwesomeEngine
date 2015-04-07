package texture;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;

public class CubeMap extends Texture {
	public CubeMap() {
		super();
	}

	public CubeMap(int textureid) {
		super(textureid);
	}

	@Override
	public void bind() {
		glBindTexture(GL_TEXTURE_CUBE_MAP, textureid);
	}

	@Override
	public void unbind() {
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}
}