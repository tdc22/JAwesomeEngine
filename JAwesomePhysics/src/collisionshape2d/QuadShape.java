package collisionshape2d;

import math.ComplexMath;
import math.VecMath;
import objects.RigidBody2;
import shapedata2d.QuadStructure;
import vector.Vector2f;

public class QuadShape extends RigidBody2 implements QuadStructure {
	Vector2f halfsize;

	public QuadShape(float x, float y, float halfsizex, float halfsizey) {
		super();
		translate(x, y);
		halfsize = new Vector2f(halfsizex, halfsizey);
		init();
	}

	public QuadShape(float x, float y, Vector2f halfsize) {
		super();
		translate(x, y);
		this.halfsize = halfsize;
		init();
	}

	public QuadShape(Vector2f pos, float halfsizex, float halfsizey) {
		super();
		translate(pos);
		halfsize = new Vector2f(halfsizex, halfsizey);
		init();
	}

	public QuadShape(Vector2f pos, Vector2f halfsize) {
		super();
		translate(pos);
		this.halfsize = halfsize;
		init();
	}

	@Override
	public Vector2f getHalfSize() {
		return halfsize;
	}

	@Override
	public Vector2f getSize() {
		return VecMath.scale(halfsize, 2);
	}

	private void init() {
		float diag = (float) Math.sqrt(halfsize.x * halfsize.x + halfsize.y
				* halfsize.y);
		setAABB(new Vector2f(-diag, -diag), new Vector2f(diag, diag));
	}

	@Override
	public Vector2f supportPointLocal(Vector2f direction) {
		Vector2f v = ComplexMath
				.transform(this.getInverseRotation(), direction);
		System.out.println(v + "; " + this.getRotation() + "; " + this.getRotation().get2dRotationf() + "; " + this.getInverseRotation());
		return new Vector2f((v.x < 0 ? -1 : 1) * halfsize.x, (v.y < 0 ? -1 : 1)
				* halfsize.y);
	}
}