package gui;

import gui.Font.FontType;
import loader.FontLoader;
import math.VecMath;
import objects.ShapedObject2;
import utils.DefaultValues;
import utils.GLConstants;
import vector.Vector2f;

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
		setFont(f);
		if (f.getFontType() == FontType.BitmapFont) {
			setRenderMode(GLConstants.TRIANGLES);
			setRenderHints(false, true, false);
		} else {
			setRenderMode(GLConstants.LINES);
			setRenderHints(true, false, false);
		}
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
		if (f.getFontType() == FontType.BitmapFont) {
			font = new BitmapFont(f, ((BitmapFont) f).getBitmap());
		} else {
			font = new OutlineFont(f);
		}
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

	private final Vector2f currPos = new Vector2f();

	public void setText(String text) {
		this.text = text;
		char[] chars = text.toCharArray();

		delete();
		currPos.set(0, 0);
		int indexCount = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			FontCharacter character = font.getCharacter(c);
			for (int v = 0; v < character.getVertices().size(); v++) {
				this.addVertex(VecMath.addition(currPos, character.getVertices().get(v)), character.getColor(v),
						character.getTextureCoordinate(v));
			}
			for (Integer index : character.getIndices()) {
				this.addIndex(indexCount + index);
			}
			indexCount = getIndices().get(getIndices().size() - 1) + 1;
			currPos.x += character.getMargin().x + charactermargin;
			if (c == '\n') {
				currPos.x = 0;
				currPos.y += 1;
			}
		}
		prerender();
	}
}