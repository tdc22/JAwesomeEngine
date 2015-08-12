package gui;

import objects.ShapedObject2;
import utils.GLConstants;
import vector.Vector2f;

public class FontCharacter extends ShapedObject2 {
	Vector2f margin;

	public FontCharacter() {
		setRenderMode(GLConstants.LINES);
	}

	public Vector2f getMargin() {
		return margin;
	}

	public void setMargin(float marginx, float marginy) {
		margin = new Vector2f(marginx, marginy);
	}

	public void setMargin(Vector2f marg) {
		margin = marg;
	}
}