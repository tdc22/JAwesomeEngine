package loader;

import java.io.File;
import java.io.IOException;

import anim.BoneAnimationSkeleton3;
import objects.ShapedObject3;

public class AnimationLoader {
	public static BoneAnimationSkeleton3 load(String path, ShapedObject3 skin) {
		BoneAnimationSkeleton3 animation = null;
		if (path.endsWith(".dae")) {
			try {
				animation = ColladaLoader.loadAnimation(new File(path), skin);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Could not load animation: " + path);
		}
		return animation;
	}
}