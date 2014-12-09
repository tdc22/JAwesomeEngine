package gui;

import objects.ShapedObject2;

import org.lwjgl.opengl.GL11;

import vector.Vector2f;

public class FontCharacter extends ShapedObject2 {
	Vector2f margin;

	public FontCharacter() {
		setRenderMode(GL11.GL_LINES);
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