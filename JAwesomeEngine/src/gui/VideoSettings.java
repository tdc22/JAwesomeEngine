package gui;

public class VideoSettings {
	int resx, resy;
	float FOVy, zNear, zFar;

	public VideoSettings() {
		init(800, 600, 90f, 0.1f, 200);
	}

	public VideoSettings(int resx, int resy, float FOVy, float zNear,
			float zFar, boolean vsync, int aaSamples) {
		init(resx, resy, FOVy, zNear, zFar);
	}

	private void init(int resx, int resy, float FOVy, float zNear, float zFar) {
		this.resx = resx;
		this.resy = resy;
		this.FOVy = FOVy;
		this.zNear = zNear;
		this.zFar = zFar;
	}

	public float getFOVy() {
		return FOVy;
	}

	public float getZNear() {
		return zNear;
	}

	public float getZFar() {
		return zFar;
	}

	public int getResolutionX() {
		return resx;
	}

	public int getResolutionY() {
		return resy;
	}
}
