package space;

public class SimplePhysicsProfiler implements PhysicsProfiler {
	long lastvalue;
	long[] values;

	public SimplePhysicsProfiler() {
		values = new long[5];
	}

	@Override
	public void physicsStart() {
		values[0] = getTime();
		lastvalue = values[0];
	}

	@Override
	public void boradphaseNarrowphase() {
		long time = getTime();
		values[1] = time - lastvalue;
		lastvalue = time;
	}

	@Override
	public void narrowphaseResolution() {
		long time = getTime();
		values[2] = time - lastvalue;
		lastvalue = time;
	}

	@Override
	public void resolutionIntegration() {
		long time = getTime();
		values[3] = time - lastvalue;
		lastvalue = time;
	}

	@Override
	public void physicsEnd() {
		values[4] = getTime() - lastvalue;
	}

	@Override
	public long[] getValues() {
		return values;
	}

	private long getTime() {
		return System.nanoTime() / 1000;
	}
}
