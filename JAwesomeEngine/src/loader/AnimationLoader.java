package loader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import anim.BoneAnimationSkeleton3;
import objects.ShapedObject3;

public class AnimationLoader {
	public static BoneAnimationSkeleton3 load(String path, ShapedObject3 skin,
			HashMap<Integer, Integer> vertexIndexMapping) {
		BoneAnimationSkeleton3 animation = null;
		if (path.endsWith(".dae")) {
			try {
				animation = ColladaLoader.loadAnimation(new File(path), skin, vertexIndexMapping);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Could not load animation: " + path);
		}
		return animation;
	}
}