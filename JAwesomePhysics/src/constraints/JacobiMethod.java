package constraints;

import java.util.Arrays;

public class JacobiMethod implements LinearEquationSolver {
	int dimension;
	int maxIterations;
	
	public JacobiMethod(int dimension, int maxIterations) {
		this.dimension = dimension;
		this.maxIterations = maxIterations;
	}

	@Override
	public float[] solve(float[][] A, float[] b) {
		float[] result = new float[dimension];
		Arrays.fill(result, 0);
		float[] nextResult = new float[dimension];
		
		for(int a = 0; a < maxIterations; a++) {
			for(int i = 0; i < dimension; i++) {
				float o = 0;
				for(int j = 0; j < dimension; j++) {
					if(j != i) {
						o += A[i][j] * result[j];
					}
				}
				nextResult[i] = (b[i] - o) / A[i][i];
			}
			result = nextResult;
			// TODO: check convergance
		}
		
		return result;
	}

}
