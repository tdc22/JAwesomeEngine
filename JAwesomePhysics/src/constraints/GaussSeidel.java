package constraints;

import java.util.Arrays;

public class GaussSeidel implements LinearEquationSolver {
	int dimension;
	int maxIterations;
	
	public GaussSeidel(int dimension, int maxIterations) {
		this.dimension = dimension;
		this.maxIterations = maxIterations;
	}

	@Override
	public float[] solve(float[][] A, float[] b) {
		float[] result = new float[dimension];
		Arrays.fill(result, 0);
		
		for(int a = 0; a < maxIterations; a++) {
			for(int i = 0; i < dimension; i++) {
				float o = 0;
				for(int j = 0; j < dimension; j++) {
					if(j != i) {
						o += A[i][j] * result[j];
					}
				}
				result[i] = (1 / A[i][i]) * (b[i] - o);
			}
			// TODO: check convergance
		}
		
		return result;
	}

}
