package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
	public static String readFile(File file) throws FileNotFoundException {
		BufferedReader reader = null;

		reader = new BufferedReader(new FileReader(file));
		StringBuilder content = new StringBuilder();

		try {
			String line = reader.readLine();
			while (line != null) {
				content.append(line);
				if ((line = reader.readLine()) != null) {
					content.append("\n");
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = content.toString();

		return result;
	}

	public static String readFile(String path) throws FileNotFoundException {
		return readFile(new File(path));
	}
}
