package OGLRotationTest;

import shape.Box;

public class RotationObject extends Box {
	float degree = 0;

	public RotationObject(float x, float y, float z, float halfsizeX, float halfsizeY, float halfsizeZ) {
		super(x, y, z, halfsizeX, halfsizeY, halfsizeZ);
	}

	@Override
	protected void initRender() {
		if (shadered)
			shader.bind();

		glPushMatrix();
		glTranslatef(rotcenter.x, rotcenter.y, rotcenter.z);
		glMultMatrix(buf);
		glTranslatef(-rotcenter.x, -rotcenter.y, -rotcenter.z);
		glRotatef(degree, 1, 0, 0);
	}

	public void rotateFurther(float rot) {
		degree += rot;
	}
}
