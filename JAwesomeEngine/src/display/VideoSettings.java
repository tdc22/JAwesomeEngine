package display;

import utils.DefaultValues;

public class VideoSettings {
	int resx, resy;
	float FOVy, zNear, zFar;

	public VideoSettings() {
		init(DefaultValues.DEFAULT_VIDEO_RESOLUTION_X,
				DefaultValues.DEFAULT_VIDEO_RESOLUTION_Y,
				DefaultValues.DEFAULT_VIDEO_FOVY,
				DefaultValues.DEFAULT_VIDEO_ZNEAR,
				DefaultValues.DEFAULT_VIDEO_ZFAR);
	}

	public VideoSettings(int resx, int resy) {
		init(resx, resy, DefaultValues.DEFAULT_VIDEO_FOVY,
				DefaultValues.DEFAULT_VIDEO_ZNEAR,
				DefaultValues.DEFAULT_VIDEO_ZFAR);
	}

	public VideoSettings(int resx, int resy, float FOVy, float zNear, float zFar) {
		init(resx, resy, FOVy, zNear, zFar);
	}

	public float getFOVy() {
		return FOVy;
	}

	public int getResolutionX() {
		return resx;
	}

	public int getResolutionY() {
		return resy;
	}

	public float getZFar() {
		return zFar;
	}

	public float getZNear() {
		return zNear;
	}

	private void init(int resx, int resy, float FOVy, float zNear, float zFar) {
		this.resx = resx;
		this.resy = resy;
		this.FOVy = FOVy;
		this.zNear = zNear;
		this.zFar = zFar;
	}
}
