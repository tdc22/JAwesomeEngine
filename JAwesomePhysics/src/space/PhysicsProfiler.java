package space;

public interface PhysicsProfiler {
	public void physicsStart();

	public void boradphaseNarrowphase();

	public void narrowphaseResolution();

	public void resolutionIntegration();

	public void physicsEnd();

	public long[] getValues();
}
