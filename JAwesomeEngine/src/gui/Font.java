package gui;

import java.awt.Color;
import java.util.HashMap;

public class Font {
	HashMap<Character, FontCharacter> characters;

	public Font() {
		characters = new HashMap<Character, FontCharacter>();
	}
	
	public Font(Font font) {
		characters = new HashMap<Character, FontCharacter>(font.getCharacters());
	}

	public void addCharacter(char character, FontCharacter fontcharacter) {
		characters.put(character, fontcharacter);
	}
	
	public HashMap<Character, FontCharacter> getCharacters() {
		return characters;
	}

	public FontCharacter getCharacter(char character) {
		return characters.get(character);
	}

	public void setColor(Color color) {
		for (FontCharacter fc : characters.values()) {
			fc.setColor(color);
		}
	}
	
	public void setSpaceSize(float size) {
		getCharacter(' ').setMargin(size, size);
	}
}
