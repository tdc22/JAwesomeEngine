package gui;

import static org.lwjgl.opengl.GL11.glTranslatef;
import loader.FontLoader;
import objects.RenderedObject;
import utils.DefaultValues;

public class Text extends RenderedObject {
	String text;
	Font font;
	float fontsize = 1, charactermargin, spacesize;

	public Text(String text, float x, float y, Font f) {
		init(text, x, y, f, DefaultValues.DEFAULT_FONT_SIZE,
				DefaultValues.DEFAULT_FONT_CHARACTER_MARGIN,
				DefaultValues.DEFAULT_FONT_SPACE_SIZE);
	}

	public Text(String text, float x, float y, Font f, float size) {
		init(text, x, y, f, size, DefaultValues.DEFAULT_FONT_CHARACTER_MARGIN,
				DefaultValues.DEFAULT_FONT_SPACE_SIZE);
	}

	public Text(String text, float x, float y, Font f, float size,
			float characterMargin, float spaceSize) {
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

	private void init(String text, float x, float y, Font f, float size,
			float characterMargin, float spaceSize) {
		this.text = text;
		setFont(f);
		setSpaceSize(spaceSize);
		setFontsize(size);
		setCharacterMargin(characterMargin);
		translateTo(x, y);
	}

	@Override
	public void render() {
		initRender();

		char[] chars = text.toCharArray();
		float lastmargin = 0;
		float linemargin = 0;
		int l = chars.length;
		glTranslatef(0, 1, 0);
		for (int i = 0; i < l; i++) {
			char c = chars[i];
			FontCharacter character = font.getCharacter(c);
			glTranslatef(lastmargin, 0, 0);
			character.render();
			lastmargin = character.getMargin().x + charactermargin;
			linemargin += lastmargin;
			if (c == '\n') {
				glTranslatef(-linemargin, 1, 0);
				linemargin = 0;
			}
		}

		endRender();
	}

	public void setCharacterMargin(float margin) {
		charactermargin = margin;
	}

	public void setFont(Font f) {
		font = f;
	}

	public void setFont(String fontname) {
		font = FontLoader.loadFont(fontname);
	}

	public void setFontsize(float size) {
		scale(size / fontsize);
		fontsize = size;
	}

	public void setSpaceSize(float size) {
		font.getCharacter(' ').setMargin(size, size);
	}

	public void setText(String text) {
		this.text = text;
	}
}