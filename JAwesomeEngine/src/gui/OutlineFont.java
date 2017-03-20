package gui;

public class OutlineFont extends Font {

	public OutlineFont() {
		super();
	}

	public OutlineFont(Font font) {
		super(font);
	}

	@Override
	public FontType getFontType() {
		return FontType.OutlineFont;
	}
}