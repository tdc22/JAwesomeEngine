package loader;

import java.io.File;
import java.io.IOException;

import anim.BoneAnimationSkeleton3;

public class AnimationLoader {
	public static BoneAnimationSkeleton3 load(String path) {
		BoneAnimationSkeleton3 animation = null;
		if (path.endsWith(".dae")) {
			try {
				animation = ColladaLoader.loadAnimation(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Could not load animation: " + path);
		}
		return animation;
	}
}