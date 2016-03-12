package constraints;

import objects.Constraint2;
import vector.Vector1f;
import vector.Vector2f;

public class ConstraintSolverErin2 {
	//Source: http://www.bulletphysics.com/ftp/pub/test/physics/papers/IterativeDynamics.pdf
	public void solveForces() {
		int n = 2; // Number of Bodies;
		int s = 2; //1? Number of constraints
		int sixN = 3*n; // 2 pos + 1 rot
		
		// TODO: calculate lambda
		// JBλ = η
		float[] lambda = new float[s];
		float[][] j = new float[sixN][s];
		// TODO: init j
		float[] F = new float[sixN];
		for(int i = 0; i < sixN; i++) {
			F[i] = 0;
		}
		for(int i = 0; i < s; i++) {
			int b1 = jmap(i, 1);
			int b2 = jmap(i, 2);
			if(b1 > 0) {
				F[b1] += jsp(j, i, 1) * lambda[i];
			}
			F[b2] += jsp(j, i, 2) * lambda[i];
		}
	}
	
	public void solveVelocities(Constraint2 c) {
		int n = 2; // Number of Bodies;
		int s = 1; //1? Number of constraints
		int sixN = 3*n; // 2 pos + 1 rot
		
		float[][] j = c.getJacobian();
		float[] V = new float[sixN];
		V[0] = c.getBodyA().getLinearVelocity().x;
		V[1] = c.getBodyA().getLinearVelocity().y;
		V[2] = c.getBodyA().getAngularVelocity().x;
		V[3] = c.getBodyB().getLinearVelocity().x;
		V[4] = c.getBodyB().getLinearVelocity().y;
		V[5] = c.getBodyB().getAngularVelocity().x;
		
		float[] C = new float[sixN];
		
		for(int i = 0; i < s; i++) {
			int b1 = jmap(i, 1);
			int b2 = jmap(i, 2);
			
			float[] sum = new float[3];
			for(int su = 0; su < 6; su++) {
				sum[su] = 0;
			}
			
			if(b1 > 0) {
				sum[0] += jsp(j, i, 0) * V[b1];
				sum[1] += jsp(j, i+1, 0) * V[b1+1];
				sum[2] += jsp(j, i+2, 0) * V[b1+2];
			}
			sum[3] += jsp(j, i, 1) * V[b2];
			sum[4] += jsp(j, i+1, 1) * V[b2+1];
			sum[5] += jsp(j, i+2, 1) * V[b2+2];
			C[0] = sum[0];
			C[1] = sum[1];
			C[2] = sum[2];
			C[3] = sum[3];
			C[4] = sum[4];
			C[5] = sum[5];
		}
		
		c.getBodyA().setLinearVelocity(new Vector2f(C[0], C[1]));
		c.getBodyA().setAngularVelocity(new Vector1f(C[2]));
		c.getBodyB().setLinearVelocity(new Vector2f(C[3], C[4]));
		c.getBodyB().setAngularVelocity(new Vector1f(C[5]));
	}
	
	private int jmap(int a, int b) {
		// Position in F[]
		return (a-1) * 3 + (b-1) * 2;
		//return (a-1) * 6 + (b-1) * 3;
	}
	
	private float jsp(float[][] j, int a, int b) {
		return j[a][b];
	}
}
