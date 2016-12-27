package misc;

import constraints.GaussSeidel;
import constraints.JacobiMethod;
import constraints.SuccessiveOverRelaxation;

public class LinearEquationSolverTest {
	/**
	 * The following test is based on the example of:
	 * https://en.wikipedia.org/wiki/Jacobi_method
	 */
	private static int iterations = 10;

	public static void main(String[] args) {
		float[][] A = new float[][] { { 10, -1, 2, 0 }, { -1, 11, -1, 3 }, { 2, -1, 10, -1 }, { 0, 3, -1, 8 } };
		float[] b = new float[] { 6, 25, -11, 15 };

		System.out.println("Solve Ax = b for unknown x with values:");
		System.out.print("A = ");
		for (int y = 0; y < A.length; y++) {
			for (int x = 0; x < A[0].length; x++) {
				System.out.print(A[x][y] + " ");
			}
			System.out.println();
		}
		printVector("b", b);
		System.out.println();
		System.out.println();

		System.out.println("Gauss-Seidel:");
		GaussSeidel gauss = new GaussSeidel(4, iterations);
		float[] gaussResult = gauss.solve(A, b);
		printVector("x", gaussResult);
		System.out.println();

		System.out.println("Jacobi method:");
		JacobiMethod jacobi = new JacobiMethod(4, iterations);
		float[] jacobiResult = jacobi.solve(A, b);
		printVector("x", jacobiResult);
		System.out.println();

		System.out.println("Successive over-relaxation:");
		SuccessiveOverRelaxation sor = new SuccessiveOverRelaxation(4, 1, iterations);
		float[] sorResult = sor.solve(A, b);
		printVector("x", sorResult);
		System.out.println();
	}

	private static void printVector(String vecname, float[] vector) {
		System.out.print(vecname + " =");
		for (float a : vector)
			System.out.print(" " + a);
	}
}
