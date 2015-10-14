package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import objects.ShapedObject3;

public class ModelLoader {
	public static ShapedObject3 load(String path) {
		ShapedObject3 model = null;
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