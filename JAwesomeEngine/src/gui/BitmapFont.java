package gui;

import texture.Texture;

public class BitmapFont extends Font {
	Texture bitmap;

	public BitmapFont(Texture bitmap) {
		super();
		this.bitmap = bitmap;
	}

	public BitmapFont(Font font, Texture bitmap) {
		super(font);
		this.bitmap = bitmap;
	}

	@Override
	public FontType getFontType() {
		return FontType.BitmapFont;
	}

	public Texture getBitmap() {
		return bitmap;
	}
}
