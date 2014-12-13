package gjkRegionTest;

import objects.ShapedObject;
import utils.GLConstants;
import utils.Shader;
import vector.Vector3f;
import vector.Vector4f;

public class Points extends ShapedObject {
	int region;

	public Points(int reg, int shader) {
		rendermode = GLConstants.POINTS;
		// addVertex(new Vector3f(0, 0, 0), Color.GRAY, new Vector2f(0, 0), new
		// Vector3f(0, 1, 0));
		// addIndex(0);
		// translate(vec);
		prerender();

		region = reg;
		Vector3f color = new Vector3f();
		switch (region) {
		case 0:
			color.set(1, 1, 1);
			break;
		case 1:
			color.set(1, 0, 0);
			break;
		case 2:
			color.set(0, 1, 0);
			break;
		case 3:
			color.set(0, 0, 1);
			break;
		case 4:
			color.set(0, 1, 1);
			break;
		case 5:
			color.set(1, 0, 1);
			break;
		case 6:
			color.set(0.5, 0.5, 0.5);
			break;
		case 7:
			color.set(0, 0.5, 0.5);
			break;
		}

		setShader(new Shader(shader, "color", new Vector4f(color.x, color.y,
				color.z, 1f)));
	}

	public void update() {
		prerender();
	}
}
