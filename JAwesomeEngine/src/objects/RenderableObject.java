package objects;

import java.nio.FloatBuffer;

public interface RenderableObject extends Renderable {
	public void delete();

	public FloatBuffer getMatrixBuffer();
}
