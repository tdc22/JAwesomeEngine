package objects;

import constraints.GaussSeidel;
import constraints.LinearEquationSolver;
import matrix.Matrix1f;
import quaternion.Complexf;
import vector.Vector1f;
import vector.Vector2f;

public abstract class Constraint2 extends Constraint<Vector2f, Vector1f, Complexf, Matrix1f> {
	LinearEquationSolver solver;
	
	public Constraint2(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyA, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> bodyB) {
		super(bodyA, bodyB);
		solver = new GaussSeidel(6, 20);
	}
	
	public void solve(float delta) {
		float[] v = new float[] {bodyA.getLinearVelocity().x, bodyA.getLinearVelocity().y, bodyA.getAngularVelocity().x,
				bodyB.getLinearVelocity().x, bodyB.getLinearVelocity().y, bodyB.getAngularVelocity().x};
		float[][] jacobian = calculateJacobian();
		System.out.println("A: ");
		for(float[] a : jacobian) {
			for(float b : a) {
				System.out.print(b + "; ");
			}
			System.out.println();
		}
		System.out.print("b: ");
		for(float f : v) System.out.print(f + "; ");
		System.out.println();
		float[] result = solver.solve(jacobian, v);
		bodyA.applyCentralImpulse(new Vector2f(result[0], result[1]));
		bodyA.applyTorqueImpulse(new Vector1f(result[2]));
		bodyB.applyCentralImpulse(new Vector2f(result[3], result[4]));
		bodyB.applyTorqueImpulse(new Vector1f(result[5]));
		System.out.print("result: ");
		for(float f : result) System.out.print(f + "; ");
		System.out.println();
	}
}