package utils;

public class SimpleGameProfiler implements GameProfiler {
	long[] values;
	long lastvalue;

	public SimpleGameProfiler() {
		values = new long[4];
	}

	@Override
	public void frameStart() {
		values[0] = getTime();
		lastvalue = values[0];
	}

	@Override
	public void updateRender3d() {
		long time = getTime();
		values[1] = time - lastvalue;
		lastvalue = time;
	}

	@Override
	public void render3dRender2d() {
		long time = getTime();
		values[2] = time - lastvalue;
		lastvalue = time;
	}

	@Override
	public void frameEnd() {
		values[3] = getTime() - lastvalue;
	}

	@Override
	public long[] getValues() {
		return values;
	}

	private long getTime() {
		return System.nanoTime() / 1000;
	}
}