package utils;

/**
 * @deprecated
 * @see #meineNeueMethode
 */
@Deprecated
public class StencilLightSourceOLD { // implements Runnable {
	/*
	 * Vector3f pos; float extend; boolean near; List<GameObject> objects;
	 * List<List<Vector3f[]>> edges, projecteds; List<Vector3f[]> objectedges,
	 * objectprojecteds; int currentobject = 0; Vector3f light;
	 * 
	 * // private boolean running = false; // private Thread t; // private int
	 * delay;
	 * 
	 * public StencilLightSource(Vector3f position, float extend, boolean near)
	 * { init(pos, extend, near); }
	 * 
	 * public StencilLightSource(float x, float y, float z, float extend,
	 * boolean near) { init(new Vector3f(x, y, z), extend, near); }
	 * 
	 * private void init(Vector3f pos, float extend, boolean near) { this.pos =
	 * pos; this.extend = extend; this.near = near;
	 * 
	 * objects = new ArrayList<GameObject>(); edges = new
	 * ArrayList<List<Vector3f[]>>(); projecteds = new
	 * ArrayList<List<Vector3f[]>>(); objectedges = new ArrayList<Vector3f[]>();
	 * objectprojecteds = new ArrayList<Vector3f[]>();
	 * 
	 * // t = new Thread(this); }
	 * 
	 * public void setPosition(float x, float y, float z) { pos.set(x, y, z); }
	 * 
	 * public void setPosition(Vector3f position) { pos = position; }
	 * 
	 * public Vector3f getPosition() { return pos; }
	 * 
	 * public void addObject(GameObject object) { objects.add(object); }
	 * 
	 * public void addObjects(List<GameObject> objects) {
	 * this.objects.addAll(objects); }
	 * 
	 * public void update() { // running = false; // t.start(); edges.clear();
	 * projecteds.clear(); for (int ob = 0; ob < objects.size(); ob++) {
	 * GameObject obj = objects.get(ob);
	 * 
	 * glMatrixMode(GL_MODELVIEW_MATRIX); glPushMatrix(); glLoadIdentity(); /*
	 * Vector3f rotcenter = obj.getRotationCenter(); glTranslatef(rotcenter.x,
	 * rotcenter.y, rotcenter.z); glMultMatrix(obj.getMatrixBuffer());
	 * glTranslatef(-rotcenter.x, -rotcenter.y, -rotcenter.z);
	 *//*
		 * Vector3f rotcenter = obj.getRotationCenter();
		 * glTranslatef(rotcenter.x, rotcenter.y, rotcenter.z); Matrix4f mat =
		 * obj.getMatrix(); FloatBuffer fb = BufferUtils.createFloatBuffer(16 *
		 * 4); FloatBuffer fb2 = BufferUtils.createFloatBuffer(16 * 4);
		 * 
		 * mat.store(fb2); float x = mat.m30; float y = mat.m31; float z =
		 * mat.m32; fb2.rewind(); mat.negate(); mat.m33 = 1; mat.m30 = x;
		 * mat.m31 = y; mat.m32 = z;
		 * 
		 * mat.store(fb); fb.rewind(); glMultMatrix(fb);
		 * 
		 * mat.load(fb2);
		 * 
		 * glTranslatef(-rotcenter.x, -rotcenter.y, -rotcenter.z);
		 * 
		 * computeShadowVolume(((ShapedObject) obj).getPolys());
		 * 
		 * glPopMatrix(); } }
		 * 
		 * /* public void startUpdating(int delay) { running = true; this.delay
		 * = delay; t.start(); }
		 * 
		 * public void stopUpdating() { running = false; }
		 * 
		 * @Override public void run() { do { for(int ob = 0; ob <
		 * objects.size(); ob++) { GameObject obj = objects.get(ob); //FALSCH
		 * computeShadowVolume(((ShapedObject)obj).getPolys()); } if(running) {
		 * try { Thread.sleep(delay); } catch (InterruptedException e) {
		 * e.printStackTrace(); } } } while(running); }
		 *//*
		 * 
		 * public void render() { glColor3f(1.0f, 1.0f, 1.0f);
		 * glBegin(GL_QUADS); // Top Face glVertex3f(pos.x - 0.1f, pos.y + 0.1f,
		 * pos.z - 0.1f); glVertex3f(pos.x - 0.1f, pos.y + 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y + 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y + 0.1f, pos.z - 0.1f); // Front Face
		 * glVertex3f(pos.x - 0.1f, pos.y - 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y - 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y + 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x - 0.1f, pos.y + 0.1f, pos.z + 0.1f); // Right face
		 * glVertex3f(pos.x + 0.1f, pos.y - 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y + 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y + 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y - 0.1f, pos.z + 0.1f); // Back Face
		 * glVertex3f(pos.x - 0.1f, pos.y - 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x - 0.1f, pos.y + 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y + 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y - 0.1f, pos.z - 0.1f); // Left Face
		 * glVertex3f(pos.x - 0.1f, pos.y - 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x - 0.1f, pos.y - 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x - 0.1f, pos.y + 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x - 0.1f, pos.y + 0.1f, pos.z - 0.1f); // Bottom Face
		 * glVertex3f(pos.x - 0.1f, pos.y - 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y - 0.1f, pos.z - 0.1f);
		 * glVertex3f(pos.x + 0.1f, pos.y - 0.1f, pos.z + 0.1f);
		 * glVertex3f(pos.x - 0.1f, pos.y - 0.1f, pos.z + 0.1f); glEnd();
		 * 
		 * glColorMask(false, false, false, false); glDepthMask(false);
		 * 
		 * for (int o = 0; o < objects.size(); o++) { GameObject obj =
		 * objects.get(o);
		 * 
		 * objectedges.clear(); objectprojecteds.clear();
		 * 
		 * glPushMatrix();
		 * 
		 * Matrix4f matr = obj.getMatrix(); FloatBuffer fbr =
		 * BufferUtils.createFloatBuffer(16 * 4); FloatBuffer fb2r =
		 * BufferUtils.createFloatBuffer(16 * 4);
		 * 
		 * matr.store(fb2r); float xr = matr.m30; float yr = matr.m31; float zr
		 * = matr.m32; fb2r.rewind(); matr.negate(); matr.m33 = 1; matr.m30 =
		 * xr; matr.m31 = yr; matr.m32 = zr;
		 * 
		 * matr.store(fbr); fbr.rewind(); glMultMatrix(fbr);
		 * 
		 * matr.load(fb2r);
		 * 
		 * for (int e = 0; e < edges.get(o).size(); e++) { Vector3f[] edge =
		 * edges.get(o).get(e); objectedges.add(edge); } for (int p = 0; p <
		 * projecteds.get(o).size(); p++) { Vector3f[] projected =
		 * projecteds.get(o).get(p); objectprojecteds.add(projected); }
		 * 
		 * currentobject = o; drawShadow(); glPopMatrix(); } endDrawShadows();
		 * 
		 * /* for(int o = 0; o < objects.size(); o++) { GameObject obj =
		 * objects.get(o);
		 * 
		 * objectedges.clear(); objectprojecteds.clear();
		 * 
		 * glPushMatrix();
		 * 
		 * Matrix4f matr = obj.getMatrix(); FloatBuffer fbr =
		 * BufferUtils.createFloatBuffer(16*4); FloatBuffer fb2r =
		 * BufferUtils.createFloatBuffer(16*4);
		 * 
		 * matr.store(fb2r); float xr = matr.m30; float yr = matr.m31; float zr
		 * = matr.m32; fb2r.rewind(); matr.negate(); matr.m33 = 1; matr.m30 =
		 * xr; matr.m31 = yr; matr.m32 = zr;
		 * 
		 * matr.store(fbr); fbr.rewind(); glMultMatrix(fbr);
		 * 
		 * matr.load(fb2r);
		 * 
		 * for(int e = 0; e < edges.get(o).size(); e++) { Vector3f[] edge =
		 * edges.get(o).get(e); objectedges.add(edge); } for(int p = 0; p <
		 * projecteds.get(o).size(); p++) { Vector3f[] projected =
		 * projecteds.get(o).get(p); objectprojecteds.add(projected); }
		 * 
		 * currentobject = o; drawShadowVolume(); drawCaps(); glPopMatrix(); }
		 * endDrawShadows();
		 *//*
			 * }
			 * 
			 * public void drawShadow() { glEnable(GL_STENCIL_TEST);
			 * glStencilFunc(GL_ALWAYS, 1, 0xFFFFFFF); glEnable(GL_CULL_FACE);
			 * 
			 * if (near) { glCullFace(GL_FRONT); glStencilOp(GL_KEEP,
			 * GL_INCR_WRAP, GL_KEEP); drawCaps(); drawShadowVolume();
			 * 
			 * glCullFace(GL_BACK); glStencilOp(GL_KEEP, GL_DECR_WRAP, GL_KEEP);
			 * drawCaps(); drawShadowVolume(); } else { glFrontFace(GL_CCW);
			 * drawShadowVolume(); glStencilOp(GL_KEEP, GL_KEEP, GL_DECR);
			 * glFrontFace(GL_CW); drawShadowVolume(); glStencilOp(GL_KEEP,
			 * GL_KEEP, GL_INCR); glFrontFace(GL_CCW); }
			 * 
			 * /* glFrontFace(GL_CCW); drawShadowVolume(); //
			 * glStencilOp(GL_KEEP, GL_KEEP, GL_DECR); glFrontFace(GL_CW);
			 * drawShadowVolume(); // glStencilOp(GL_KEEP, GL_KEEP, GL_INCR);
			 * glFrontFace(GL_CCW);
			 */

	/*
	 * glEnable(GL_STENCIL_TEST); glStencilFunc(GL_ALWAYS, 1, 0xFFFFFFF);
	 * glStencilOp(GL_KEEP, GL_KEEP, GL_INCR); glEnable(GL_CULL_FACE);
	 * glFrontFace(GL_CCW); drawShadowVolume(); // glStencilOp(GL_KEEP, GL_KEEP,
	 * GL_DECR); glFrontFace(GL_CW); drawShadowVolume(); // glFrontFace(GL_CCW);
	 *//*
		 * }
		 * 
		 * public void endDrawShadows() { glColorMask(true, true, true, true);
		 * glStencilFunc(GL_NOTEQUAL, 0, 0xFFFFFFFF); glColor4f(0.0f, 0.0f,
		 * 0.0f, 0.3f); glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA,
		 * GL_ONE_MINUS_SRC_ALPHA); glPushMatrix(); glLoadIdentity();
		 * glBegin(GL_TRIANGLE_STRIP); glVertex3f(-0.1f, 0.1f, -0.1f);
		 * glVertex3f(-0.1f, -0.1f, -0.1f); glVertex3f(0.1f, 0.1f, -0.1f);
		 * glVertex3f(0.1f, -0.1f, -0.1f); glEnd(); glPopMatrix();
		 * glDepthMask(true); glDisable(GL_STENCIL_TEST); }
		 * 
		 * public void computeShadowVolume(List<List<Vector3f>> vertices) {
		 * light = computeTranslation();
		 * 
		 * List<Vector3f[]> tedges = new ArrayList<Vector3f[]>();
		 * List<Vector3f[]> tprojs = new ArrayList<Vector3f[]>(); for
		 * (List<Vector3f> shape : vertices) { if (VecMath.vectorScalarproduct(
		 * VecMath.vectorSubstraction(light, shape.get(0)),
		 * VecMath.computeNormal(shape.get(0), shape.get(1), shape.get(2))) >=
		 * 0) { // Wenn shape beleuchtet --> Nachbarshapes werden überprüft
		 * 
		 * for (List<Vector3f> shape2 : vertices) { Vector<Vector3f> samecoords
		 * = new Vector<Vector3f>(); int samecoord = 0; int p1 = -2, p2 = -2;
		 * for (int i = 0; i < shape.size(); i++) { for (Vector3f shape2points :
		 * shape2) { Vector3f shapepoints = shape.get(i); if (shapepoints.x ==
		 * shape2points.x && shapepoints.y == shape2points.y && shapepoints.z ==
		 * shape2points.z) { samecoords.add(shapepoints); if (samecoord == 0) p1
		 * = i; else p2 = i; samecoord++; } } } if (samecoord == 2) { // Wenn
		 * Shape2 ein Nachbarshape ist, wird auch da die // beleuchtung
		 * überprüft... if (VecMath.vectorScalarproduct(VecMath
		 * .vectorSubstraction(light, shape2.get(0)),
		 * VecMath.computeNormal(shape2.get(0), shape2.get(1), shape2.get(2))) <
		 * 0) { Vector3f[] edge = new Vector3f[2]; // Wenn es nicht beleuchtet
		 * ist edge[0] = samecoords.elementAt(0); edge[1] =
		 * samecoords.elementAt(1); if (p2 - p1 != 1) { Vector3f swap = edge[0];
		 * edge[0] = edge[1]; edge[1] = swap; } tedges.add(edge); } } } } }
		 * edges.add(tedges);
		 * 
		 * // Silhouette ist jetzt berechnet, Vektoren müssen erweitert werden
		 * for (Vector3f[] spoint : tedges) { Vector3f[] proj = new Vector3f[2];
		 * for (int i = 0; i < 2; i++) { proj[i] =
		 * VecMath.vectorSubstraction(spoint[i], light); proj[i] =
		 * VecMath.vectorMultSkalar(proj[i], extend); proj[i] =
		 * VecMath.vectorAddition(proj[i], light); } tprojs.add(proj); }
		 * projecteds.add(tprojs); }
		 * 
		 * public void drawShadowVolume() { glColor3f(1.0f, 1.0f, 1.0f); for
		 * (int q = 0; q < objectedges.size(); q++) { Vector3f[] sil =
		 * objectedges.get(q); Vector3f[] proj = objectprojecteds.get(q);
		 * 
		 * glBegin(GL_TRIANGLES); glVertex3f(proj[1].x, proj[1].y, proj[1].z);
		 * glVertex3f(sil[1].x, sil[1].y, sil[1].z); glVertex3f(sil[0].x,
		 * sil[0].y, sil[0].z); glEnd(); glBegin(GL_TRIANGLES);
		 * glVertex3f(proj[0].x, proj[0].y, proj[0].z); glVertex3f(proj[1].x,
		 * proj[1].y, proj[1].z); glVertex3f(sil[0].x, sil[0].y, sil[0].z);
		 * glEnd(); } }
		 * 
		 * public void drawCaps() { List<List<Vector3f>> polys = ((ShapedObject)
		 * objects.get(currentobject)) .getPolys(); for (int p = 0; p <
		 * polys.size(); p++) { List<Vector3f> poly = polys.get(p); if
		 * (VecMath.vectorScalarproduct(VecMath.vectorSubstraction(light,
		 * poly.get(0)), VecMath.computeNormal(poly.get(0), poly.get(1),
		 * poly.get(2))) > 0) { glBegin(GL_TRIANGLES); glVertex3f(poly.get(2).x,
		 * poly.get(2).y, poly.get(2).z); glVertex3f(poly.get(1).x,
		 * poly.get(1).y, poly.get(1).z); glVertex3f(poly.get(0).x,
		 * poly.get(0).y, poly.get(0).z); glEnd(); } if
		 * (VecMath.vectorScalarproduct(VecMath.vectorSubstraction(light,
		 * poly.get(0)), VecMath.computeNormal(poly.get(0), poly.get(1),
		 * poly.get(2))) < 0) { Vector3f aim2 = VecMath.vectorAddition(VecMath
		 * .vectorMultSkalar( VecMath.vectorSubstraction(poly.get(0), light),
		 * extend), light); Vector3f aim1 = VecMath.vectorAddition(VecMath
		 * .vectorMultSkalar( VecMath.vectorSubstraction(poly.get(1), light),
		 * extend), light); Vector3f aim0 = VecMath.vectorAddition(VecMath
		 * .vectorMultSkalar( VecMath.vectorSubstraction(poly.get(2), light),
		 * extend), light); glBegin(GL_TRIANGLES); glVertex3f(aim0.x, aim0.y,
		 * aim0.z); glVertex3f(aim1.x, aim1.y, aim1.z); glVertex3f(aim2.x,
		 * aim2.y, aim2.z); glEnd(); } } }
		 * 
		 * public Vector3f computeTranslation() { FloatBuffer buf =
		 * BufferUtils.createFloatBuffer(16); glGetFloat(GL_MODELVIEW_MATRIX,
		 * buf);
		 * 
		 * double[][] matrixarray = new double[4][4]; int index = 0; for (int mx
		 * = 0; mx < 4; mx++) { for (int my = 0; my < 4; my++) {
		 * matrixarray[mx][my] = buf.get(index); index++; } }
		 * 
		 * double[] punktvektor = new double[4]; punktvektor[0] = pos.x;
		 * punktvektor[1] = pos.y; punktvektor[2] = pos.z; punktvektor[3] = 1;
		 * 
		 * double[] rotated = VecMath.matrixMultVector(matrixarray,
		 * punktvektor);
		 * 
		 * Vector3f result = new Vector3f((float) rotated[0], (float)
		 * rotated[1], (float) rotated[2]);
		 * 
		 * return result; }
		 */
}
