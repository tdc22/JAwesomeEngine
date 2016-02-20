package constraints;

public class ConstraintSolverErin {
	//Source: http://www.bulletphysics.com/ftp/pub/test/physics/papers/IterativeDynamics.pdf
	public void solve() {
		// TODO: calculate lambda
		// JBλ = η
		
		int n = 12; // 3 pos + 3 rot + 3 pos2 + 3 rot2
		int s = 2; // Number of constraints
		float[] F = new float[12];
		for(int i = 0; i < n; i++) {
			F[i] = 0;
		}
		for(int i = 0; i < s; i++) {
			int b1 = jmap(i, 1);
			int b2 = jmap(i, 2);
			if(b1 > 0) {
				F[b1] += jsp(i, 1) * lambda[i];
			}
			F[b2] += jsp(i, 2) * lambda[i];
		}
	}
	
	private int jmap() {
		
	}
}
