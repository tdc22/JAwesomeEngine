package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.ShapedObject;
import shapedata.SphereStructure;
import vector.Vector2f;
import vector.Vector3f;

public class IsoSphere extends ShapedObject implements SphereStructure {
	float radius;
	
	public IsoSphere(float x, float y, float z, float radius, int subdivisions) {
		super();
		translateTo(x, y, z);
		init(radius, subdivisions);
	}
	
	public IsoSphere(Vector3f pos, float radius, int subdivisions) {
		super();
		translateTo(pos);
		init(radius, subdivisions);
	}
	
	private void init(float radius, int subdivisions) {
		this.radius = radius;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Integer[]> faces = new ArrayList<Integer[]>();
		
		vertices.add(VecMath.scale(new Vector3f(0.81649655, 0.0, -0.57735026), radius));
		vertices.add(VecMath.scale(new Vector3f(-0.81649655, 0.0, -0.57735026), radius));
		vertices.add(VecMath.scale(new Vector3f(0.0, 0.81649655, 0.57735026), radius));
		vertices.add(VecMath.scale(new Vector3f(0.0, -0.81649655, 0.57735026), radius));
		
		faces.add(new Integer[] {1, 0, 3, 0, 2, 0});
		faces.add(new Integer[] {0, 1, 2, 1, 3, 1});
		faces.add(new Integer[] {0, 2, 3, 2, 1, 2});
		faces.add(new Integer[] {0, 3, 1, 3, 2, 3});
		
		int vertexcount = 3;
		for(int i = 0; i < subdivisions; i++) {
			List<Integer[]> currfaces = new ArrayList<Integer[]>(faces);
			for(int j = currfaces.size()-1; j >= 0; j--) {
				Integer[] f = currfaces.get(j);
				Vector3f a = vertices.get(f[0]);
				Vector3f b = vertices.get(f[2]);
				Vector3f c = vertices.get(f[4]);
				
				//Triangles werden falsch subdivided, statt point in die mitte zu setzen, point an jede kante setzen
				//und 4 statt 3 triangles generieren. (in anderer schleife um redundanzen zu vermeiden)
				//siehe: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html
				Vector3f d = VecMath.setScale(VecMath.addition(VecMath.addition(a, b), c), radius);
//				Vector3f d = VecMath.scale(VecMath.addition(VecMath.addition(a, b), c), 1/3f);
				vertexcount++;
				vertices.add(d);
				
				faces.remove(j);
				faces.add(new Integer[] {f[4], f[0], vertexcount, f[0], f[2], f[0]});
				faces.add(new Integer[] {f[0], f[4], f[2], f[4], vertexcount, f[4]});
				faces.add(new Integer[] {f[0], f[2], vertexcount, f[2], f[4], f[2]});
			}
		}
		System.out.println(faces.size());
		
		for(Vector3f v : vertices)
			addVertex(v, Color.GRAY, new Vector2f(0, 0), VecMath.normalize(v));
		
		for(Integer[] f : faces)
			addTriangle(f[0], f[1], f[2], f[3], f[4], f[5]);
		
		this.prerender();
	}

	@Override
	public float getRadius() {
		return radius;
	}
}
