package debug_EPARegion;

import objects.ShapedObject;
import shader.Shader;
import utils.GLConstants;
import vector.Vector3f;
import vector.Vector4f;

public class Points extends ShapedObject {
	int region;

	public Points(Vector3f color, int shader) {
		rendermode = GLConstants.POINTS;
		// addVertex(new Vector3f(0, 0, 0), Color.GRAY, new Vector2f(0, 0), new
		// Vector3f(0, 1, 0));
		// addIndex(0);
		// translate(vec);
		prerender();

		setShader(new Shader(shader, "color", new Vector4f(color.x, color.y,
				color.z, 1f)));
	}

	public void update() {
		prerender();
	}
}
