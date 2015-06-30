package utils;

public interface GameProfiler {
	public void frameStart();

	public void updateRender3d();

	public void render3dRender2d();

	public void frameEnd();

	public long[] getValues();
}