package tool_normalEditor;

import shape2d.Circle;
import vector.Vector2f;
import vector.Vector3f;

public class NormalMarker extends Circle {
	Vector3f normal;

	public NormalMarker(Vector2f pos, Vector3f normal) {
		super(pos, 5, 36);
		this.normal = normal;
	}

}
