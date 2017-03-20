package gui;

import objects.ShapedObject2;
import vector.Vector2f;

public class FontCharacter extends ShapedObject2 {
	Vector2f margin;

	public FontCharacter() {
		margin = new Vector2f();
	}

	public Vector2f getMargin() {
		return margin;
	}

	public void setMargin(float marginx, float marginy) {
		margin.set(marginx, marginy);
	}

	public void setMargin(Vector2f marg) {
		margin = marg;
	}
}