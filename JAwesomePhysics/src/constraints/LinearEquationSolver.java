package constraints;

public interface LinearEquationSolver {
	// solves Ax = b for unknown x
	public float[] solve(float[][] A, float[] b);
}