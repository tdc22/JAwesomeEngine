package gui;

import loader.FontLoader;
import math.VecMath;
import objects.ShapedObject2;
import utils.DefaultValues;
import utils.GLConstants;
import vector.Vector3f;

public class Text extends ShapedObject2 {
	String text;
	Font font;
	float fontsize = 1, charactermargin, spacesize;

	public Text(String text, float x, float y, Font f) {
		init(text, x, y, f, DefaultValues.DEFAULT_FONT_SIZE, DefaultValues.DEFAULT_FONT_CHARACTER_MARGIN,
				DefaultValues.DEFAULT_FONT_SPACE_SIZE);
	}

	public Text(String text, float x, float y, Font f, float size) {
		init(text, x, y, f, size, DefaultValues.DEFAULT_FONT_CHARACTER_MARGIN, DefaultValues.DEFAULT_FONT_SPACE_SIZE);
	}

	public Text(String text, float x, float y, Font f, float size, float characterMargin, float spaceSize) {
		init(text, x, y, f, size, characterMargin, spaceSize);
	}

	public float getCharacterMargin() {
		return charactermargin;
	}

	public Font getFont() {
		return font;
	}

	public float getFontsize() {
		return fontsize;
	}

	public String getText() {
		return text;
	}

	private void init(String text, float x, float y, Font f, float size, float characterMargin, float spaceSize) {
		setRenderMode(GLConstants.LINES);
		setFont(f);
		setSpaceSize(spaceSize);
		setFontsize(size);
		setCharacterMargin(characterMargin);
		setText(text);
		translateTo(x, y);
	}

	public void setCharacterMargin(float margin) {
		charactermargin = margin;
	}

	public void setFont(Font f) {
		font = new Font(f);
	}

	public void setFont(String fontname) {
		font = FontLoader.loadFont(fontname);
	}

	public void setFontsize(float size) {
		scale(size / fontsize);
		fontsize = size;
	}

	public void setSpaceSize(float size) {
		font.setSpaceSize(size);
	}

	public void setText(String text) {
		this.text = text;
		char[] chars = text.toCharArray();

		delete();
		Vector3f position = new Vector3f();
		int indexCount = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			FontCharacter character = font.getCharacter(c);
			for (Vector3f v : character.getVertices()) {
				this.addVertex(VecMath.addition(position, v));
			}
			for (Integer index : character.getIndices()) {
				this.addIndex(indexCount + index);
			}
			indexCount = getIndices().get(getIndices().size() - 1) + 1;
			position.x += character.getMargin().x + charactermargin;
			if (c == '\n') {
				position.x = 0;
				position.y += 1;
			}
		}
		prerender();
	}
}