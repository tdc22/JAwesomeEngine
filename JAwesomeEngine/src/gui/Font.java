package gui;

import java.util.HashMap;

public class Font {
	HashMap<Character, FontCharacter> characters;

	public Font() {
		characters = new HashMap<Character, FontCharacter>();
	}

	public void addCharacter(char character, FontCharacter fontcharacter) {
		characters.put(character, fontcharacter);
	}

	public FontCharacter getCharacter(char character) {
		return characters.get(character);
	}
}
