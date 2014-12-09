package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import objects.ShapedObject;

public class ModelLoader {
	public static ShapedObject load(String path) {
		ShapedObject model = null;
		if (path.endsWith(".obj") || path.endsWith(".mobj")) {
			try {
				model = OBJLoader.loadModel(new File(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Could not load model: " + path);
		}
		model.prerender();
		return model;
	}
}