package convexhull;

public class HACD {
	// source:
	// 1. (use 2)
	// http://www.researchgate.net/publication/221129055_A_simple_and_efficient_approach_for_3D_mesh_approximate_convex_decomposition
	// 2. http://khaledmammou.com/AllPublications/icip2009.pdf
	// 3.
	// https://code.google.com/p/v-hacd/source/browse/src/VHACD_Lib/src/VHACD.cpp
//	double m_depth = 20;
//	boolean m_cancel = false;
//	boolean m_pca = false;
//	double m_concavity = 0.001;
//	int m_mode = 1; // 0: voxel-based (recommended), 1: tetrahedron-based
//	short m_planeDownsampling = 4;
//	int m_convexhullDownsampling = 4;
//	List<HACDMesh> m_convexHulls;
//	Vector3d m_barycenter;
//	Matrix3d m_rot;
//	PrimitiveSet m_pset;
//	HACDVolume m_volume;
//	int m_dim = 64;
//	int m_resolution = 100000;
//	boolean m_convexhullApproximation = true;
//	double m_volumeCH0 = 0;
//	double m_alpha = 0.05;
//	double m_beta = 0.05;
//	double m_delta = 0.05;
//	double m_gamma = 0.0005;
//	int m_maxNumVerticesPerCH = 64;
//	double m_minVolumePerCH = 0.0001;
//
//	private final int AXIS_X = 0;
//	private final int AXIS_Y = 0;
//	private final int AXIS_Z = 0;
//
//	public CompoundObject3 computeConvexDecomposition(Vector3d[] vertices,
//			Integer[] triangles) {
//		m_barycenter = new Vector3d();
//		m_rot = new Matrix3d();
//		System.out.println("START ALIGNMESH");
//		alignMesh((Vector3d[]) vertices, triangles, 1, 1);
//		System.out.println("START VOXELIZEMESH");
//		voxelizeMesh((Vector3d[]) vertices, triangles, 1, 1);
//		System.out.println("START COMPUTEPRIMITIVESET");
//		computePrimitiveSet();
//		System.out.println("START COMPUTEACD");
//		computeACD();
//		// mergeConvexHulls();
//		// simplifyConvexHulls();
//
//		System.out.println(m_convexHulls.size());
//
//		return null;
//	}
//
//	private void computeACD() {
//		m_convexHulls = new ArrayList<HACDMesh>();
//
//		List<PrimitiveSet> parts = new ArrayList<PrimitiveSet>();
//		List<PrimitiveSet> inputParts = new ArrayList<PrimitiveSet>();
//		List<PrimitiveSet> temp = new ArrayList<PrimitiveSet>();
//
//		inputParts.add(m_pset);
//
//		List<Plane> planes = new ArrayList<Plane>();
//		List<Plane> planesRef = new ArrayList<Plane>();
//		int sub = 0;
//		boolean firstIteration = true;
//		double m_volumeCH0 = 1;
//		while (sub++ < m_depth && inputParts.size() > 0 && !m_cancel) {
//			System.out.println(sub + "; " + m_depth + "; " + inputParts.size());
//			double maxConcavity = 0;
//			int nInputParts = inputParts.size();
//
//			for (int p = 0; p < nInputParts && !m_cancel; ++p) {
//				PrimitiveSet pSet = inputParts.get(p);
//				inputParts.set(p, null);
//				double volume = pSet.computeVolume();
//				pSet.computeBB();
//				pSet.computePrincipalAxes();
//				if (m_pca)
//					pSet.alignToPrincipalAxes();
//
//				pSet.computeConvexHull(pSet.getConvexHull());
//				double volumeCH = Math
//						.abs(pSet.getConvexHull().computeVolume());
//				if (firstIteration) {
//					m_volumeCH0 = volumeCH;
//				}
//
//				double concavity = computeConcavity(volume, volumeCH,
//						m_volumeCH0);
//				double error = 1.01 * pSet.computeMaxVolumeError()
//						/ m_volumeCH0;
//				double localConcavity = (volumeCH > 0.0) ? computeLocalConcavity(
//						volume, volumeCH) : 0.0;
//
//				if (firstIteration) {
//					firstIteration = false;
//				}
//
//				if (concavity > m_concavity && concavity > error) {
//					Vector3d preferredCuttingDirection = new Vector3d();
//					double w = computePreferredCuttingDirection(pSet,
//							preferredCuttingDirection);
//					planes.clear();
//
//					if (m_mode == 0) {
//						VoxelSet vset = (VoxelSet) pSet;
//						computeAxesAlignedClippingPlanes(vset,
//								m_planeDownsampling, planes);
//					} else {
//						TetrahedronSet tset = (TetrahedronSet) pSet;
//						computeAxesAlignedClippingPlanes(tset,
//								m_planeDownsampling, planes);
//					}
//
//					Plane bestPlane = null;
//					double minConcavity = Double.MAX_VALUE;
//					computeBestClippingPlane(pSet, planes,
//							preferredCuttingDirection, w, m_alpha, m_beta,
//							m_delta, m_convexhullDownsampling, bestPlane,
//							minConcavity);
//					if (!m_cancel
//							&& (m_planeDownsampling > 1 || m_convexhullDownsampling > 1)) {
//						planesRef.clear();
//
//						if (m_mode == 0) {
//							VoxelSet vset = (VoxelSet) pSet;
//							refineAxesAlignedClippingPlanes(vset, bestPlane,
//									m_planeDownsampling, planesRef);
//						} else {
//							TetrahedronSet tset = (TetrahedronSet) pSet;
//							refineAxesAlignedClippingPlanes(tset, bestPlane,
//									m_planeDownsampling, planesRef);
//						}
//
//						computeBestClippingPlane(pSet, planesRef,
//								preferredCuttingDirection, w, concavity
//										* m_alpha, concavity * m_beta,
//								concavity * m_delta, 1, // convexhullDownsampling
//														// = 1
//								bestPlane, minConcavity);
//					}
//					if (m_cancel) {
//						pSet = null;
//						break;
//					} else {
//						if (maxConcavity < minConcavity) {
//							maxConcavity = minConcavity;
//						}
//						PrimitiveSet bestLeft = pSet.create();
//						PrimitiveSet bestRight = pSet.create();
//						temp.add(bestLeft);
//						temp.add(bestRight);
//						pSet.clip(bestPlane, bestRight, bestLeft);
//						if (m_pca) {
//							bestRight.revertAlignToPrincipalAxes();
//							bestLeft.revertAlignToPrincipalAxes();
//						}
//						pSet = null;
//					}
//				} else {
//					if (m_pca) {
//						pSet.revertAlignToPrincipalAxes();
//					}
//					parts.add(pSet);
//				}
//			}
//
//			if (m_cancel) {
//				temp.clear();
//			} else {
//				inputParts = new ArrayList<PrimitiveSet>(temp);
//				temp.clear();
//			}
//		}
//
//		parts.addAll(inputParts);
//
//		if (m_cancel) {
//			parts.clear();
//			return;
//		}
//
//		int nConvexHulls = parts.size();
//		m_convexHulls.clear();
//		for (int p = 0; p < nConvexHulls && !m_cancel; ++p) {
//			m_convexHulls.add(new HACDMesh());
//			parts.get(p).computeConvexHull(m_convexHulls.get(p));
//			int nv = m_convexHulls.get(p).GetNPoints();
//			double x, y, z;
//			for (int i = 0; i < nv; ++i) {
//				Vector3d pt = m_convexHulls.get(p).getPoint(i);
//				x = pt.x;
//				y = pt.y;
//				z = pt.z;
//				double[][] mat = m_rot.getArray();
//				pt.x = mat[0][0] * x + mat[0][1] * y + mat[0][2] * z
//						+ m_barycenter.x;
//				pt.y = mat[1][0] * x + mat[1][1] * y + mat[1][2] * z
//						+ m_barycenter.y;
//				pt.z = mat[2][0] * x + mat[2][1] * y + mat[2][2] * z
//						+ m_barycenter.z;
//			}
//		}
//
//		parts.clear();
//
//		if (m_cancel) {
//			m_convexHulls.clear();
//			return;
//		}
//
//		// TODO: Transform HACDConvexHull to ConvexHull!!!
//		return;
//	}
//
//	public void alignMesh(Vector3d[] points, Integer[] triangles,
//			int stridePoints, int strideTriangles) {
//		if (m_cancel || !m_pca) {
//			return;
//		}
//		m_dim = (int) (Math.pow((double) m_resolution, 1.0 / 3.0) + 0.5);
//		HACDVolume volume = new HACDVolume();
//
//		volume.voxelize(points, triangles, stridePoints, strideTriangles,
//				m_barycenter, m_rot.getArray(), m_dim);
//		int n = volume.getNPrimitivesOnSurf()
//				+ volume.getNPrimitivesInsideSurf();
//		if (m_cancel) {
//			return;
//		}
//		volume.alignToPrincipalAxes(m_rot.getArray());
//	}
//
//	public void voxelizeMesh(Vector3d[] points, Integer[] triangles,
//			int stridePoints, int strideTriangles) {
//		if (m_cancel) {
//			return;
//		}
//
//		m_volume = null;
//		int iteration = 0;
//		int maxIteration = 5;
//		while (iteration++ < maxIteration && !m_cancel) {
//			System.out.println("Iteration: " + iteration + "; maxIteration: " + maxIteration);
//			System.out.println("0");
//			m_volume = new HACDVolume();
//			System.out.println("1");
//			m_volume.voxelize(points, triangles, stridePoints, strideTriangles,
//					m_barycenter, m_rot.getArray(), m_dim);
//			System.out.println("a");
//
//			int n = m_volume.getNPrimitivesOnSurf()
//					+ m_volume.getNPrimitivesInsideSurf();
//			System.out.println("b");
//
//			double a = Math.pow((double) (m_resolution) / n, 0.33);
//			int dim_next = (int) (m_dim * a + 0.5);
//			System.out.println("c");
//			if (n < m_resolution && iteration < maxIteration
//					&& m_volume.getNPrimitivesOnSurf() < m_resolution / 8
//					&& m_dim != dim_next) {
//				m_volume = null;
//				m_dim = dim_next;
//			} else {
//				break;
//			}
//			System.out.println("d");
//		}
//	}
//
//	private void computePrimitiveSet() {
//		if (m_cancel) {
//			return;
//		}
//
//		if (m_mode == 0) {
//			VoxelSet vset = new VoxelSet();
//			m_volume.convert(vset);
//			m_pset = vset;
//		} else {
//			TetrahedronSet tset = new TetrahedronSet();
//			m_volume.convert(tset);
//			m_pset = tset;
//		}
//
//		m_volume = null;
//	}
//
//	private double computePreferredCuttingDirection(PrimitiveSet tSet,
//			Vector3d dir) {
//		double ex = tSet.getEigenValue(AXIS_X);
//		double ey = tSet.getEigenValue(AXIS_Y);
//		double ez = tSet.getEigenValue(AXIS_Z);
//		double vx = (ey - ez) * (ey - ez);
//		double vy = (ex - ez) * (ex - ez);
//		double vz = (ex - ey) * (ex - ey);
//		// TODO: check if the vector gets set right
//		if (vx < vy && vx < vz) {
//			double e = ey * ey + ez * ez;
//			dir.set(1, 0, 0);
//			return (e == 0.0) ? 0.0 : 1.0 - vx / e;
//		} else if (vy < vx && vy < vz) {
//			double e = ex * ex + ez * ez;
//			dir.set(0, 1, 0);
//			return (e == 0.0) ? 0.0 : 1.0 - vy / e;
//		} else {
//			double e = ex * ex + ey * ey;
//			dir.set(0, 0, 1);
//			return (e == 0.0) ? 0.0 : 1.0 - vz / e;
//		}
//	}
//
//	private Plane[] computeAxesAlignedClippingPlanes(VoxelSet vset,
//			short downsampling, List<Plane> planes) {
//		Vector3d minV = vset.getMinBBVoxels();
//		Vector3d maxV = vset.getMaxBBVoxels();
//		Vector3d pt;
//		Plane plane = new Plane();
//		short i0 = (short) minV.x;
//		short i1 = (short) maxV.x;
//		plane.m_a = 1.0;
//		plane.m_b = 0.0;
//		plane.m_c = 0.0;
//		plane.m_axis = AXIS_X;
//		for (short i = i0; i <= i1; i += downsampling) {
//			pt = vset.getPoint(new Vector3d(i + 0.5, 0.0, 0.0));
//			plane.m_d = -pt.x;
//			plane.m_index = i;
//			planes.add(plane);
//		}
//		short j0 = (short) minV.y;
//		short j1 = (short) maxV.y;
//		plane.m_a = 0.0;
//		plane.m_b = 1.0;
//		plane.m_c = 0.0;
//		plane.m_axis = AXIS_Y;
//		for (short j = j0; j <= j1; j += downsampling) {
//			pt = vset.getPoint(new Vector3d(0.0, j + 0.5, 0.0));
//			plane.m_d = -pt.y;
//			plane.m_index = j;
//			planes.add(plane);
//		}
//		short k0 = (short) minV.z;
//		short k1 = (short) maxV.z;
//		plane.m_a = 0.0;
//		plane.m_b = 0.0;
//		plane.m_c = 1.0;
//		plane.m_axis = AXIS_Z;
//		for (short k = k0; k <= k1; k += downsampling) {
//			pt = vset.getPoint(new Vector3d(0.0, 0.0, k + 0.5));
//			plane.m_d = -pt.z;
//			plane.m_index = k;
//			planes.add(plane);
//		}
//		return null;
//	}
//
//	private Plane[] computeAxesAlignedClippingPlanes(TetrahedronSet tset,
//			short downsampling, List<Plane> planes) {
//		Vector3d minV = tset.getMinBB();
//		Vector3d maxV = tset.getMaxBB();
//		double scale = tset.getScale();
//		short i0 = 0;
//		short j0 = 0;
//		short k0 = 0;
//		short i1 = (short) ((maxV.x - minV.x) / scale + 0.5);
//		short j1 = (short) ((maxV.y - minV.y) / scale + 0.5);
//		short k1 = (short) ((maxV.z - minV.z) / scale + 0.5);
//
//		Plane plane = new Plane();
//		plane.m_a = 1.0;
//		plane.m_b = 0.0;
//		plane.m_c = 0.0;
//		plane.m_axis = AXIS_X;
//		for (short i = i0; i <= i1; i += downsampling) {
//			double x = minV.x + scale * i;
//			plane.m_d = -x;
//			plane.m_index = i;
//			planes.add(plane);
//		}
//		plane.m_a = 0.0;
//		plane.m_b = 1.0;
//		plane.m_c = 0.0;
//		plane.m_axis = AXIS_Y;
//		for (short j = j0; j <= j1; j += downsampling) {
//			double y = minV.y + scale * j;
//			plane.m_d = -y;
//			plane.m_index = j;
//			planes.add(plane);
//		}
//		plane.m_a = 0.0;
//		plane.m_b = 0.0;
//		plane.m_c = 1.0;
//		plane.m_axis = AXIS_Z;
//		for (short k = k0; k <= k1; k += downsampling) {
//			double z = minV.z + scale * k;
//			plane.m_d = -z;
//			plane.m_index = k;
//			planes.add(plane);
//		}
//		return null;
//	}
//
//	private Plane[] refineAxesAlignedClippingPlanes(VoxelSet vset,
//			Plane bestPlane, short downsampling, List<Plane> planes) {
//		Vector3d minV = vset.getMinBBVoxels();
//		Vector3d maxV = vset.getMaxBBVoxels();
//		Vector3d pt;
//		Plane plane = new Plane();
//
//		if (bestPlane.m_axis == AXIS_X) {
//			short i0 = (short) Math.max(minV.x, bestPlane.m_index
//					- downsampling);
//			short i1 = (short) Math.min(maxV.x, bestPlane.m_index
//					+ downsampling);
//			plane.m_a = 1.0;
//			plane.m_b = 0.0;
//			plane.m_c = 0.0;
//			plane.m_axis = AXIS_X;
//			for (short i = i0; i <= i1; ++i) {
//				pt = vset.getPoint(new Vector3d(i + 0.5, 0.0, 0.0));
//				plane.m_d = -pt.x;
//				plane.m_index = i;
//				planes.add(plane);
//			}
//		} else if (bestPlane.m_axis == AXIS_Y) {
//			short j0 = (short) Math.max(minV.y, bestPlane.m_index
//					- downsampling);
//			short j1 = (short) Math.min(maxV.y, bestPlane.m_index
//					+ downsampling);
//			plane.m_a = 0.0;
//			plane.m_b = 1.0;
//			plane.m_c = 0.0;
//			plane.m_axis = AXIS_Y;
//			for (short j = j0; j <= j1; ++j) {
//				pt = vset.getPoint(new Vector3d(0.0, j + 0.5, 0.0));
//				plane.m_d = -pt.y;
//				plane.m_index = j;
//				planes.add(plane);
//			}
//		} else {
//			short k0 = (short) Math.max(minV.z, bestPlane.m_index
//					- downsampling);
//			short k1 = (short) Math.min(maxV.z, bestPlane.m_index
//					+ downsampling);
//			plane.m_a = 0.0;
//			plane.m_b = 0.0;
//			plane.m_c = 1.0;
//			plane.m_axis = AXIS_Z;
//			for (short k = k0; k <= k1; ++k) {
//				pt = vset.getPoint(new Vector3d(0.0, 0.0, k + 0.5));
//				plane.m_d = -pt.z;
//				plane.m_index = k;
//				planes.add(plane);
//			}
//		}
//		return null;
//	}
//
//	private Plane[] refineAxesAlignedClippingPlanes(TetrahedronSet tset,
//			Plane bestPlane, short downsampling, List<Plane> planes) {
//		Vector3d minV = tset.getMinBB();
//		Vector3d maxV = tset.getMaxBB();
//		double scale = tset.getScale();
//		Plane plane = new Plane();
//
//		if (bestPlane.m_axis == AXIS_X) {
//			short i0 = (short) Math.max(0, bestPlane.m_index - downsampling);
//			short i1 = (short) Math.min((maxV.x - minV.x) / scale + 0.5,
//					bestPlane.m_index + downsampling);
//			plane.m_a = 1.0;
//			plane.m_b = 0.0;
//			plane.m_c = 0.0;
//			plane.m_axis = AXIS_X;
//			for (short i = i0; i <= i1; ++i) {
//				double x = minV.x + scale * i;
//				plane.m_d = -x;
//				plane.m_index = i;
//				planes.add(plane);
//			}
//		} else if (bestPlane.m_axis == AXIS_Y) {
//			short j0 = (short) Math.max(0, bestPlane.m_index - downsampling);
//			short j1 = (short) Math.min((maxV.y - minV.y) / scale + 0.5,
//					bestPlane.m_index + downsampling);
//			plane.m_a = 0.0;
//			plane.m_b = 1.0;
//			plane.m_c = 0.0;
//			plane.m_axis = AXIS_Y;
//			for (short j = j0; j <= j1; ++j) {
//				double y = minV.y + scale * j;
//				plane.m_d = -y;
//				plane.m_index = j;
//				planes.add(plane);
//			}
//		} else {
//			short k0 = (short) Math.max(0, bestPlane.m_index - downsampling);
//			short k1 = (short) Math.min((maxV.z - minV.z) / scale + 0.5,
//					bestPlane.m_index + downsampling);
//			plane.m_a = 0.0;
//			plane.m_b = 0.0;
//			plane.m_c = 1.0;
//			plane.m_axis = AXIS_Z;
//			for (short k = k0; k <= k1; ++k) {
//				double z = minV.z + scale * k;
//				plane.m_d = -z;
//				plane.m_index = k;
//				planes.add(plane);
//			}
//		}
//		return null;
//	}
//
//	private double computeLocalConcavity(double volume, double volumeCH) {
//		return Math.abs(volumeCH - volume) / volumeCH;
//	}
//
//	private double computeConcavity(double volume, double volumeCH,
//			double volume0) {
//		return Math.abs(volumeCH - volume) / volume0;
//	}
//
//	private double computeVolume4(Vector3d a, Vector3d b, Vector3d c, Vector3d d) {
//		// (a-d) * ((b-d) ^ (c-d));
//		return VecMath.dotproduct(
//				VecMath.subtraction(a, d),
//				VecMath.crossproduct(VecMath.subtraction(b, d),
//						VecMath.subtraction(c, d)));
//	}
//
//	private void diagonalize(double[][] A, double[][] Q, double[][] D) {
//		// A must be a symmetric matrix.
//		// returns Q and D such that
//		// Diagonal matrix D = QT * A * Q; and A = Q*D*QT
//		int maxsteps = 24; // certainly wont need that many.
//		int k0, k1, k2;
//		double[] o = new double[3];
//		double[] m = new double[3];
//		double[] q = { 0.0, 0.0, 0.0, 1.0 };
//		double[] jr = new double[4];
//		double sqw, sqx, sqy, sqz;
//		double tmp1, tmp2, mq;
//		double[][] AQ = new double[3][3];
//		double thet, sgn, t, c;
//		for (int i = 0; i < maxsteps; ++i) {
//			// quat to matrix
//			sqx = q[0] * q[0];
//			sqy = q[1] * q[1];
//			sqz = q[2] * q[2];
//			sqw = q[3] * q[3];
//			Q[0][0] = (sqx - sqy - sqz + sqw);
//			Q[1][1] = (-sqx + sqy - sqz + sqw);
//			Q[2][2] = (-sqx - sqy + sqz + sqw);
//			tmp1 = q[0] * q[1];
//			tmp2 = q[2] * q[3];
//			Q[1][0] = 2.0 * (tmp1 + tmp2);
//			Q[0][1] = 2.0 * (tmp1 - tmp2);
//			tmp1 = q[0] * q[2];
//			tmp2 = q[1] * q[3];
//			Q[2][0] = 2.0 * (tmp1 - tmp2);
//			Q[0][2] = 2.0 * (tmp1 + tmp2);
//			tmp1 = q[1] * q[2];
//			tmp2 = q[0] * q[3];
//			Q[2][1] = 2.0 * (tmp1 + tmp2);
//			Q[1][2] = 2.0 * (tmp1 - tmp2);
//
//			// AQ = A * Q
//			AQ[0][0] = Q[0][0] * A[0][0] + Q[1][0] * A[0][1] + Q[2][0]
//					* A[0][2];
//			AQ[0][1] = Q[0][1] * A[0][0] + Q[1][1] * A[0][1] + Q[2][1]
//					* A[0][2];
//			AQ[0][2] = Q[0][2] * A[0][0] + Q[1][2] * A[0][1] + Q[2][2]
//					* A[0][2];
//			AQ[1][0] = Q[0][0] * A[0][1] + Q[1][0] * A[1][1] + Q[2][0]
//					* A[1][2];
//			AQ[1][1] = Q[0][1] * A[0][1] + Q[1][1] * A[1][1] + Q[2][1]
//					* A[1][2];
//			AQ[1][2] = Q[0][2] * A[0][1] + Q[1][2] * A[1][1] + Q[2][2]
//					* A[1][2];
//			AQ[2][0] = Q[0][0] * A[0][2] + Q[1][0] * A[1][2] + Q[2][0]
//					* A[2][2];
//			AQ[2][1] = Q[0][1] * A[0][2] + Q[1][1] * A[1][2] + Q[2][1]
//					* A[2][2];
//			AQ[2][2] = Q[0][2] * A[0][2] + Q[1][2] * A[1][2] + Q[2][2]
//					* A[2][2];
//			// D = Qt * AQ
//			D[0][0] = AQ[0][0] * Q[0][0] + AQ[1][0] * Q[1][0] + AQ[2][0]
//					* Q[2][0];
//			D[0][1] = AQ[0][0] * Q[0][1] + AQ[1][0] * Q[1][1] + AQ[2][0]
//					* Q[2][1];
//			D[0][2] = AQ[0][0] * Q[0][2] + AQ[1][0] * Q[1][2] + AQ[2][0]
//					* Q[2][2];
//			D[1][0] = AQ[0][1] * Q[0][0] + AQ[1][1] * Q[1][0] + AQ[2][1]
//					* Q[2][0];
//			D[1][1] = AQ[0][1] * Q[0][1] + AQ[1][1] * Q[1][1] + AQ[2][1]
//					* Q[2][1];
//			D[1][2] = AQ[0][1] * Q[0][2] + AQ[1][1] * Q[1][2] + AQ[2][1]
//					* Q[2][2];
//			D[2][0] = AQ[0][2] * Q[0][0] + AQ[1][2] * Q[1][0] + AQ[2][2]
//					* Q[2][0];
//			D[2][1] = AQ[0][2] * Q[0][1] + AQ[1][2] * Q[1][1] + AQ[2][2]
//					* Q[2][1];
//			D[2][2] = AQ[0][2] * Q[0][2] + AQ[1][2] * Q[1][2] + AQ[2][2]
//					* Q[2][2];
//			o[0] = D[1][2];
//			o[1] = D[0][2];
//			o[2] = D[0][1];
//			m[0] = Math.abs(o[0]);
//			m[1] = Math.abs(o[1]);
//			m[2] = Math.abs(o[2]);
//
//			k0 = (m[0] > m[1] && m[0] > m[2]) ? 0 : (m[1] > m[2]) ? 1 : 2; // index
//																			// of
//																			// largest
//																			// element
//																			// of
//																			// offdiag
//			k1 = (k0 + 1) % 3;
//			k2 = (k0 + 2) % 3;
//			if (o[k0] == 0.0) {
//				break; // diagonal already
//			}
//			thet = (D[k2][k2] - D[k1][k1]) / (2.0 * o[k0]);
//			sgn = (thet > 0.0) ? 1.0 : -1.0;
//			thet *= sgn; // make it positive
//			t = sgn
//					/ (thet + ((thet < 1.E6) ? Math.sqrt(thet * thet + 1.0)
//							: thet)); // sign(T)/(|T|+sqrt(T^2+1))
//			c = 1.0 / Math.sqrt(t * t + 1.0); // c= 1/(t^2+1) , t=s/c
//			if (c == 1.0) {
//				break; // no room for improvement - reached machine precision.
//			}
//			jr[0] = jr[1] = jr[2] = jr[3] = 0.0;
//			jr[k0] = sgn * Math.sqrt((1.0 - c) / 2.0); // using 1/2 angle
//														// identity sin(a/2) =
//														// sqrt((1-cos(a))/2)
//			jr[k0] *= -1.0; // since our quat-to-matrix convention was for v*M
//							// instead of M*v
//			jr[3] = Math.sqrt(1.0 - jr[k0] * jr[k0]);
//			if (jr[3] == 1.0) {
//				break; // reached limits of floating point precision
//			}
//			q[0] = (q[3] * jr[0] + q[0] * jr[3] + q[1] * jr[2] - q[2] * jr[1]);
//			q[1] = (q[3] * jr[1] - q[0] * jr[2] + q[1] * jr[3] + q[2] * jr[0]);
//			q[2] = (q[3] * jr[2] + q[0] * jr[1] - q[1] * jr[0] + q[2] * jr[3]);
//			q[3] = (q[3] * jr[3] - q[0] * jr[0] - q[1] * jr[1] - q[2] * jr[2]);
//			mq = Math.sqrt(q[0] * q[0] + q[1] * q[1] + q[2] * q[2] + q[3]
//					* q[3]);
//			q[0] /= mq;
//			q[1] /= mq;
//			q[2] /= mq;
//			q[3] /= mq;
//		}
//	}
//
//	private void computeBestClippingPlane(PrimitiveSet inputPSet,
//			List<Plane> planes, Vector3d preferredCuttingDirection, double w,
//			double alpha, double beta, double delta,
//			int convexhullDownsampling, Plane bestPlane, double minConcavity) {
//		boolean useConvexhullApproximation = true;
//
//		if (m_cancel) {
//			return;
//		}
//		int nPrimitives = inputPSet.getNPrimitives();
//		int iBest = -1;
//		int nPlanes = planes.size();
//		boolean cancel = false;
//		int done = 0;
//		double minTotal = Double.MAX_VALUE;
//		double minBalance = Double.MAX_VALUE;
//		double minSymmetry = Double.MAX_VALUE;
//		double minLocalConcavity = Double.MAX_VALUE;
//		minConcavity = Double.MAX_VALUE;
//
//		List<Vector3d> chsPtsLeft = new ArrayList<Vector3d>();
//		List<Vector3d> chsPtsRight = new ArrayList<Vector3d>();
//		HACDMesh[] chs = new HACDMesh[2];
//		PrimitiveSet onSurfacePSet = inputPSet.create();
//		inputPSet.selectOnSurface(onSurfacePSet);
//
//		PrimitiveSet[] psets = null;
//		if (!m_convexhullApproximation) {
//			psets = new PrimitiveSet[2];
//			psets[0] = inputPSet.create();
//			psets[1] = inputPSet.create();
//		}
//
//		for (int x = 0; x < nPlanes; ++x) {
//			Plane plane = planes.get(x);
//			HACDMesh leftCH = chs[0];
//			HACDMesh rightCH = chs[1];
//			rightCH.clearPoints();
//			leftCH.clearPoints();
//			rightCH.clearTriangles();
//			leftCH.clearTriangles();
//
//			double volumeLeftCH1 = 0;
//			double volumeRightCH1 = 0;
//			if (m_convexhullApproximation) {
//				List<Vector3d> leftCHPts = chsPtsLeft;
//				List<Vector3d> rightCHPts = chsPtsRight; // TODO: CHECK THIS
//															// AGAIN!!!!
//				rightCHPts.clear();
//				leftCHPts.clear();
//				onSurfacePSet.intersect(plane, rightCHPts, leftCHPts,
//						convexhullDownsampling * 32);
//				inputPSet.getConvexHull().clip(plane, rightCHPts, leftCHPts);
//				rightCH.computeConvexHull(rightCHPts, rightCHPts.size());
//				leftCH.computeConvexHull(leftCHPts, leftCHPts.size());
//				HACDMesh leftCH1 = null;
//				HACDMesh rightCH1 = null;
//				VoxelSet right = null;
//				VoxelSet left = null;
//				onSurfacePSet.clip(plane, right, left);
//				right.computeConvexHull(rightCH1, convexhullDownsampling);
//				left.computeConvexHull(leftCH1, convexhullDownsampling);
//
//				volumeLeftCH1 = leftCH1.computeVolume();
//				volumeRightCH1 = rightCH1.computeVolume();
//			} else {
//				PrimitiveSet right = psets[0];
//				PrimitiveSet left = psets[1];
//				onSurfacePSet.clip(plane, right, left);
//				right.computeConvexHull(rightCH, convexhullDownsampling);
//				left.computeConvexHull(leftCH, convexhullDownsampling);
//			}
//			double volumeLeftCH = leftCH.computeVolume();
//			double volumeRightCH = rightCH.computeVolume();
//
//			// compute clipped volumes
//			double volumeLeft = 0.0;
//			double volumeRight = 0.0;
//
//			inputPSet.computeClippedVolumes(plane, volumeRight, volumeLeft);
//
//			double concavityLeft = computeConcavity(volumeLeft, volumeLeftCH,
//					m_volumeCH0);
//			double concavityRight = computeConcavity(volumeRight,
//					volumeRightCH, m_volumeCH0);
//			double localConcavityLeft = computeLocalConcavity(volumeLeft,
//					volumeLeftCH);
//			double localConcavityRight = computeLocalConcavity(volumeRight,
//					volumeRightCH);
//			double concavity = (concavityLeft + concavityRight);
//			double localConcavity = delta * (concavityLeft + concavityRight);
//
//			// compute cost
//			double concavityLeft1 = computeConcavity(volumeLeft, volumeLeftCH1,
//					m_volumeCH0);
//			double concavityRight1 = computeConcavity(volumeRight,
//					volumeRightCH1, m_volumeCH0);
//			double localConcavityLeft1 = computeLocalConcavity(volumeLeft,
//					volumeLeftCH1);
//			double localConcavityRight1 = computeLocalConcavity(volumeRight,
//					volumeRightCH1);
//			double concavity1 = (concavityLeft1 + concavityRight1);
//			double localConcavity1 = delta * (concavityLeft1 + concavityRight1);
//			double balance = alpha
//					* Math.pow(Math.pow(volumeLeft - volumeRight, 2.0), 0.5)
//					/ m_volumeCH0;
//			double d = w
//					* (preferredCuttingDirection.x * plane.m_a
//							+ preferredCuttingDirection.y * plane.m_b + preferredCuttingDirection.z
//							* plane.m_c);
//			double symmetry = beta * d;
//			double total = concavity + balance + symmetry + localConcavity;
//
//			double total1 = concavity1 + balance + symmetry + localConcavity1;
//			double err = 0.0;
//			if (useConvexhullApproximation) {
//				err = Math.abs(total - total1) / total1;
//			}
//			if (total < minTotal || (total == minTotal && x < iBest)) {
//				minConcavity = concavity;
//				minBalance = balance;
//				minSymmetry = symmetry;
//				minLocalConcavity = localConcavity;
//				bestPlane = plane;
//				minTotal = total;
//				iBest = x;
//			}
//			++done;
//		}
//	}
//
//	private void computeConvexHull(HACDMesh ch1, HACDMesh ch2,
//			List<Vector3d> pts, HACDMesh combinedCH) {
//		// pts.Resize(0);
//		// addPoints(ch1, pts);
//		// addPoints(ch2, pts);
//		//
//		// btConvexHullComputer ch;
//		// ch.compute((double[])pts.Data(), 3 * sizeof(double), (int)pts.Size(),
//		// -1.0, -1.0);
//		// combinedCH->ResizePoints (0);
//		// combinedCH->ResizeTriangles(0);
//		// for (int v = 0; v < ch.vertices.size(); v++)
//		// {
//		// combinedCH.addPoint(Vector3d(ch.vertices[v].getX(),
//		// ch.vertices[v].getY(), ch.vertices[v].getZ()));
//		// }
//		// const int nt = ch.faces.size();
//		// for (int t = 0; t < nt; ++t)
//		// {
//		// const btConvexHullComputer::Edge * sourceEdge =
//		// &(ch.edges[ch.faces[t]]);
//		// int a = sourceEdge->getSourceVertex();
//		// int b = sourceEdge->getTargetVertex();
//		// const btConvexHullComputer::Edge * edge =
//		// sourceEdge->getNextEdgeOfFace();
//		// int c = edge->getTargetVertex();
//		// while (c != a)
//		// {
//		// combinedCH.addTriangle(Vec3<int>(a, b, c));
//		// edge = edge.getNextEdgeOfFace();
//		// b = c;
//		// c = edge->getTargetVertex();
//		// }
//		// }
//	}
//
//	private void mergeConvexHulls() {
//		if (m_cancel) {
//			return;
//		}
//
//		int nConvexHulls = m_convexHulls.size();
//		int iteration = 0;
//		if (nConvexHulls > 1 && !m_cancel) {
//			int nConvexHulls0 = nConvexHulls;
//			double threshold = m_gamma;
//			List<Vector3d> pts = new ArrayList<Vector3d>();
//			HACDMesh combinedCH = new HACDMesh();
//			boolean iterate = true;
//
//			while (iterate && !m_cancel) {
//				int bestp1 = 0;
//				int bestp2 = 0;
//				double bestCost = m_volumeCH0;
//				for (int p1 = 0; (p1 < nConvexHulls0 - 1) && (!m_cancel); ++p1) {
//					if (m_convexHulls.get(p1) != null && !m_cancel) {
//						double volume1 = m_convexHulls.get(p1).computeVolume();
//						int p2 = p1 + 1;
//						while (p2 < nConvexHulls0) {
//							if (p1 != p2 && m_convexHulls.get(p2) != null
//									&& !m_cancel) {
//								double volume2 = m_convexHulls.get(p2)
//										.computeVolume();
//
//								computeConvexHull(m_convexHulls.get(p1),
//										m_convexHulls.get(p2), pts, combinedCH);
//
//								double combinedVolumeCH = combinedCH
//										.computeVolume();
//								double combinedVolume = volume1 + volume2;
//								double cost = computeConcavity(combinedVolume,
//										combinedVolumeCH, m_volumeCH0);
//
//								if (cost < bestCost) {
//									bestCost = cost;
//									bestp1 = p1;
//									bestp2 = p2;
//								}
//							}
//							++p2;
//						}
//					}
//				}
//				if (bestCost < threshold && !m_cancel) {
//					HACDMesh cch = new HACDMesh();
//					computeConvexHull(m_convexHulls.get(bestp1),
//							m_convexHulls.get(bestp2), pts, cch);
//					m_convexHulls.remove(bestp1);
//					m_convexHulls.remove(bestp2);
//					m_convexHulls.add(cch);
//					iterate = true;
//					--nConvexHulls;
//				} else {
//					iterate = false;
//				}
//			}
//			if (!m_cancel) {
//				List<HACDMesh> temp = new ArrayList<HACDMesh>(nConvexHulls);
//				for (int p1 = 0; p1 < nConvexHulls0; ++p1) {
//					if (m_convexHulls.get(p1) != null) {
//						temp.add(m_convexHulls.get(p1));
//					}
//				}
//				m_convexHulls = temp;
//			}
//		}
//	}
//
//	private void simplifyConvexHull(HACDMesh ch, int nvertices, double minVolume) {
//		if (nvertices <= 4) {
//			return;
//		}
//		ICHull icHull;
//		icHull.addPoints(ch.getPointsBuffer(), ch.getNPoints());
//		icHull.process(nvertices, minVolume);
//		TMMesh mesh = icHull.getMesh();
//		int nT = mesh.GetNTriangles();
//		int nV = mesh.GetNVertices();
//		ch.resizePoints(nV);
//		ch.resizeTriangles(nT);
//		mesh.GetIFS(ch.getPointsBuffer(), ch.getTrianglesBuffer());
//	}
//
//	private void simplifyConvexHulls() {
//		if (m_cancel || m_maxNumVerticesPerCH < 4) {
//			return;
//		}
//		int nConvexHulls = m_convexHulls.size();
//
//		for (int i = 0; i < nConvexHulls && !m_cancel; ++i) {
//			simplifyConvexHull(m_convexHulls.get(i), m_maxNumVerticesPerCH,
//					m_volumeCH0 * m_minVolumePerCH);
//		}
//	}
//
//	private class Plane {
//		double m_a;
//		double m_b;
//		double m_c;
//		double m_d;
//		int m_axis;
//		short m_index;
//	}
//
//	private class Tetrahedron {
//		Vector3d[] m_pts = new Vector3d[4];
//		int m_data;
//	}
//
//	private class Voxel {
//		short[] m_coord = new short[3];
//		short m_data;
//	}
//
//	private abstract class PrimitiveSet {
//		public abstract double computeVolume();
//
//		public abstract void computeBB();
//
//		public abstract void computePrincipalAxes();
//
//		public abstract void alignToPrincipalAxes();
//
//		public abstract HACDMesh getConvexHull();
//
//		public abstract void computeConvexHull(HACDMesh convexHull);
//
//		public abstract double computeMaxVolumeError();
//
//		public abstract double getEigenValue(int axis);
//
//		public abstract PrimitiveSet create();
//
//		public abstract void revertAlignToPrincipalAxes();
//
//		public abstract void clip(Plane p, PrimitiveSet positivePart,
//				PrimitiveSet negativePart);
//
//		public abstract int getNPrimitives();
//
//		public abstract void selectOnSurface(PrimitiveSet onSurfP);
//
//		public abstract void intersect(Plane plane,
//				List<Vector3d> positivePoints, List<Vector3d> negativePoints,
//				int sampling);
//
//		public abstract void computeConvexHull(HACDMesh mesh, int sampling);
//
//		public abstract void computeClippedVolumes(Plane plane,
//				double positiveVolume, double negativeVolume);
//	}
//
//	final int PRIMITIVE_UNDEFINED = 0;
//	final int PRIMITIVE_OUTSIDE_SURFACE = 1;
//	final int PRIMITIVE_INSIDE_SURFACE = 2;
//	final int PRIMITIVE_ON_SURFACE = 3;
//
//	private class HACDMesh {
//		List<Vector3d> m_points = new ArrayList<Vector3d>();
//		List<Integer[]> m_triangles = new ArrayList<Integer[]>();
//
//		public void clearPoints() {
//			m_points.clear();
//		}
//
//		public void clearTriangles() {
//			m_triangles.clear();
//		}
//
//		public double computeVolume() {
//			return 0; // TODO
//		}
//
//		public int GetNPoints() {
//			return m_points.size();
//		}
//
//		public Vector3d getPoint(int index) {
//			return m_points.get(index);
//		}
//
//		public void addPoint(Vector3d point) {
//			m_points.add(point);
//		}
//
//		public void addTriangle(Integer[] triangle) {
//			m_triangles.add(triangle);
//		}
//
//		public void computeConvexHull(List<Vector3d> points, int nPts) {
//			// TODO
//		}
//
//		public void clip(Plane p, List<Vector3d> positivePart,
//				List<Vector3d> negativePart) {
//			// TODO
//		}
//	}
//
//	private class TetrahedronSet extends PrimitiveSet {
//		Vector3d m_minBB, m_maxBB, m_barycenter;
//		double[][] m_Q, m_D;
//		double m_scale;
//		int m_numTetrahedraOnSurface, m_numTetrahedraInsideSurface;
//		HACDMesh m_convexHull;
//		List<Tetrahedron> m_tetrahedra;
//
//		public TetrahedronSet() {
//			m_minBB = new Vector3d(0, 0, 0);
//			m_maxBB = new Vector3d(1, 1, 1);
//			m_barycenter = new Vector3d(0, 0, 0);
//			m_scale = 1.0;
//			m_Q = new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
//			m_D = new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
//		}
//
//		public Vector3d getMinBB() {
//			return m_minBB;
//		}
//
//		public Vector3d getMaxBB() {
//			return m_maxBB;
//		}
//
//		public double getScale() {
//			return m_scale;
//		}
//
//		@Override
//		public double computeVolume() {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return 0.0;
//			double volume = 0.0;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				Tetrahedron tetrahedron = m_tetrahedra.get(v);
//				volume += Math.abs(computeVolume4(tetrahedron.m_pts[0],
//						tetrahedron.m_pts[1], tetrahedron.m_pts[2],
//						tetrahedron.m_pts[3]));
//			}
//			return volume / 6.0;
//		}
//
//		@Override
//		public void computeBB() {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//
//			m_minBB.x = m_maxBB.x = m_tetrahedra.get(0).m_pts[0].x;
//			m_barycenter.x = 0.0;
//			m_minBB.y = m_maxBB.y = m_tetrahedra.get(0).m_pts[0].y;
//			m_barycenter.y = 0.0;
//			m_minBB.z = m_maxBB.z = m_tetrahedra.get(0).m_pts[0].z;
//			m_barycenter.z = 0.0;
//			for (int p = 0; p < nTetrahedra; ++p) {
//				for (int i = 0; i < 4; ++i) {
//					if (m_minBB.x > m_tetrahedra.get(p).m_pts[i].x)
//						m_minBB.x = m_tetrahedra.get(p).m_pts[i].x;
//					if (m_maxBB.x < m_tetrahedra.get(p).m_pts[i].x)
//						m_maxBB.x = m_tetrahedra.get(p).m_pts[i].x;
//					m_barycenter.x += m_tetrahedra.get(p).m_pts[i].x;
//					if (m_minBB.y > m_tetrahedra.get(p).m_pts[i].y)
//						m_minBB.y = m_tetrahedra.get(p).m_pts[i].y;
//					if (m_maxBB.y < m_tetrahedra.get(p).m_pts[i].y)
//						m_maxBB.y = m_tetrahedra.get(p).m_pts[i].y;
//					m_barycenter.y += m_tetrahedra.get(p).m_pts[i].y;
//					if (m_minBB.z > m_tetrahedra.get(p).m_pts[i].z)
//						m_minBB.z = m_tetrahedra.get(p).m_pts[i].z;
//					if (m_maxBB.z < m_tetrahedra.get(p).m_pts[i].z)
//						m_maxBB.z = m_tetrahedra.get(p).m_pts[i].z;
//					m_barycenter.z += m_tetrahedra.get(p).m_pts[i].z;
//				}
//			}
//			m_barycenter.scale(1 / ((double) 4 * nTetrahedra));
//		}
//
//		@Override
//		public void computePrincipalAxes() {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//			double[][] covMat = { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 },
//					{ 0.0, 0.0, 0.0 } };
//			double x, y, z;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				Tetrahedron tetrahedron = m_tetrahedra.get(v);
//				for (int i = 0; i < 4; ++i) {
//					x = tetrahedron.m_pts[i].x - m_barycenter.x;
//					y = tetrahedron.m_pts[i].y - m_barycenter.y;
//					z = tetrahedron.m_pts[i].z - m_barycenter.z;
//					covMat[0][0] += x * x;
//					covMat[1][1] += y * y;
//					covMat[2][2] += z * z;
//					covMat[0][1] += x * y;
//					covMat[0][2] += x * z;
//					covMat[1][2] += y * z;
//				}
//			}
//			double n = nTetrahedra * 4.0;
//			covMat[0][0] /= n;
//			covMat[1][1] /= n;
//			covMat[2][2] /= n;
//			covMat[0][1] /= n;
//			covMat[0][2] /= n;
//			covMat[1][2] /= n;
//			covMat[1][0] = covMat[0][1];
//			covMat[2][0] = covMat[0][2];
//			covMat[2][1] = covMat[1][2];
//			diagonalize(covMat, m_Q, m_D);
//		}
//
//		@Override
//		public void alignToPrincipalAxes() {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//			double x, y, z;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				Tetrahedron tetrahedron = m_tetrahedra.get(v);
//				for (int i = 0; i < 4; ++i) {
//					x = tetrahedron.m_pts[i].x - m_barycenter.x;
//					y = tetrahedron.m_pts[i].y - m_barycenter.y;
//					z = tetrahedron.m_pts[i].z - m_barycenter.z;
//					tetrahedron.m_pts[i].x = m_Q[0][0] * x + m_Q[1][0] * y
//							+ m_Q[2][0] * z + m_barycenter.x;
//					tetrahedron.m_pts[i].y = m_Q[0][1] * x + m_Q[1][1] * y
//							+ m_Q[2][1] * z + m_barycenter.y;
//					tetrahedron.m_pts[i].z = m_Q[0][2] * x + m_Q[1][2] * y
//							+ m_Q[2][2] * z + m_barycenter.z;
//				}
//			}
//			computeBB();
//		}
//
//		@Override
//		public HACDMesh getConvexHull() {
//			return m_convexHull;
//		}
//
//		@Override
//		public void computeConvexHull(HACDMesh convexHull) {
//			computeConvexHull(convexHull, 1);
//		}
//
//		@Override
//		public double computeMaxVolumeError() {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return 0.0;
//			double volume = 0.0;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				Tetrahedron tetrahedron = m_tetrahedra.get(v);
//				if (tetrahedron.m_data == PRIMITIVE_ON_SURFACE) {
//					volume += Math.abs(computeVolume4(tetrahedron.m_pts[0],
//							tetrahedron.m_pts[1], tetrahedron.m_pts[2],
//							tetrahedron.m_pts[3]));
//				}
//			}
//			return volume / 6.0;
//		}
//
//		@Override
//		public double getEigenValue(int axis) {
//			return m_D[axis][axis];
//		}
//
//		@Override
//		public PrimitiveSet create() {
//			return new TetrahedronSet();
//		}
//
//		public boolean add(Tetrahedron tetrahedron) {
//			double v = computeVolume4(tetrahedron.m_pts[0],
//					tetrahedron.m_pts[1], tetrahedron.m_pts[2],
//					tetrahedron.m_pts[3]);
//
//			double EPS = 0.0000000001;
//			if (Math.abs(v) < EPS) {
//				return false;
//			} else if (v < 0.0) {
//				Vector3d tmp = tetrahedron.m_pts[0];
//				tetrahedron.m_pts[0] = tetrahedron.m_pts[1];
//				tetrahedron.m_pts[1] = tmp;
//			}
//
//			m_tetrahedra.add(tetrahedron);
//			return true;
//		}
//
//		@Override
//		public void revertAlignToPrincipalAxes() {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//			double x, y, z;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				Tetrahedron tetrahedron = m_tetrahedra.get(v);
//				for (int i = 0; i < 4; ++i) {
//					x = tetrahedron.m_pts[i].x - m_barycenter.x;
//					y = tetrahedron.m_pts[i].y - m_barycenter.y;
//					z = tetrahedron.m_pts[i].z - m_barycenter.z;
//					tetrahedron.m_pts[i].x = m_Q[0][0] * x + m_Q[0][1] * y
//							+ m_Q[0][2] * z + m_barycenter.x;
//					tetrahedron.m_pts[i].y = m_Q[1][0] * x + m_Q[1][1] * y
//							+ m_Q[1][2] * z + m_barycenter.y;
//					tetrahedron.m_pts[i].z = m_Q[2][0] * x + m_Q[2][1] * y
//							+ m_Q[2][2] * z + m_barycenter.z;
//				}
//			}
//			computeBB();
//		}
//
//		@Override
//		public void clip(Plane plane, PrimitiveSet positivePartP,
//				PrimitiveSet negativePartP) {
//			TetrahedronSet positivePart = (TetrahedronSet) positivePartP;
//			TetrahedronSet negativePart = (TetrahedronSet) negativePartP;
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//			positivePart.m_tetrahedra.clear();
//			negativePart.m_tetrahedra.clear();
//			negativePart.m_scale = positivePart.m_scale = m_scale;
//			negativePart.m_numTetrahedraOnSurface = positivePart.m_numTetrahedraOnSurface = 0;
//			negativePart.m_numTetrahedraInsideSurface = positivePart.m_numTetrahedraInsideSurface = 0;
//			negativePart.m_barycenter = m_barycenter;
//			positivePart.m_barycenter = m_barycenter;
//			negativePart.m_minBB = m_minBB;
//			positivePart.m_minBB = m_minBB;
//			negativePart.m_maxBB = m_maxBB;
//			positivePart.m_maxBB = m_maxBB;
//			for (int i = 0; i < 3; ++i) {
//				for (int j = 0; j < 3; ++j) {
//					negativePart.m_Q[i][j] = positivePart.m_Q[i][j] = m_Q[i][j];
//					negativePart.m_D[i][j] = positivePart.m_D[i][j] = m_D[i][j];
//				}
//			}
//
//			Tetrahedron tetrahedron;
//			double delta, alpha;
//			int[] sign = new int[4];
//			int npos, nneg;
//			Vector3d[] posPts = new Vector3d[10];
//			Vector3d[] negPts = new Vector3d[10];
//			Vector3d P0, P1, M;
//			Vector3d n = new Vector3d(plane.m_a, plane.m_b, plane.m_c);
//			int[][] edges = new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 },
//					{ 1, 2 }, { 1, 3 }, { 2, 3 } };
//			double dist;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				tetrahedron = m_tetrahedra.get(v);
//				npos = nneg = 0;
//				for (int i = 0; i < 4; ++i) {
//					dist = plane.m_a * tetrahedron.m_pts[i].x + plane.m_b
//							* tetrahedron.m_pts[i].y + plane.m_c
//							* tetrahedron.m_pts[i].z + plane.m_d;
//					if (dist > 0.0) {
//						sign[i] = 1;
//						posPts[npos] = tetrahedron.m_pts[i];
//						++npos;
//					} else {
//						sign[i] = -1;
//						negPts[nneg] = tetrahedron.m_pts[i];
//						++nneg;
//					}
//				}
//
//				if (npos == 4) {
//					positivePart.add(tetrahedron);
//					if (tetrahedron.m_data == PRIMITIVE_ON_SURFACE) {
//						++positivePart.m_numTetrahedraOnSurface;
//					} else {
//						++positivePart.m_numTetrahedraInsideSurface;
//					}
//				} else if (nneg == 4) {
//					negativePart.add(tetrahedron);
//					if (tetrahedron.m_data == PRIMITIVE_ON_SURFACE) {
//						++negativePart.m_numTetrahedraOnSurface;
//					} else {
//						++negativePart.m_numTetrahedraInsideSurface;
//					}
//				} else {
//					int nnew = 0;
//					for (int j = 0; j < 6; ++j) {
//						if (sign[edges[j][0]] * sign[edges[j][1]] == -1) {
//							P0 = tetrahedron.m_pts[edges[j][0]];
//							P1 = tetrahedron.m_pts[edges[j][1]];
//							delta = VecMath.dotproduct(
//									VecMath.subtraction(P0, P1), n);
//							alpha = -(plane.m_d + VecMath.dotproduct(n, P1))
//									/ delta;
//							assert (alpha >= 0.0 && alpha <= 1.0);
//							M = VecMath.addition(VecMath.scale(P0, alpha),
//									VecMath.scale(P1, (1 - alpha)));
//							// for(int xx = 0; xx < 3; ++xx)
//							// {
//							// assert(M[xx] + EPS >= m_minBB[xx]);
//							// assert(M[xx] <= m_maxBB[xx] + EPS);
//							// }
//							posPts[npos++] = M;
//							negPts[nneg++] = M;
//							++nnew;
//						}
//					}
//					negativePart.addClippedTetrahedra(negPts, nneg);
//					positivePart.addClippedTetrahedra(posPts, npos);
//				}
//			}
//		}
//
//		public void addClippedTetrahedra(Vector3d[] pts, int nPts) {
//			int[][] tetF = new int[][] { { 0, 1, 2 }, { 2, 1, 3 }, { 3, 1, 0 },
//					{ 3, 0, 2 } };
//			if (nPts < 4) {
//				return;
//			} else if (nPts == 4) {
//				Tetrahedron tetrahedron = new Tetrahedron();
//				tetrahedron.m_data = PRIMITIVE_ON_SURFACE;
//				tetrahedron.m_pts[0] = pts[0];
//				tetrahedron.m_pts[1] = pts[1];
//				tetrahedron.m_pts[2] = pts[2];
//				tetrahedron.m_pts[3] = pts[3];
//				if (add(tetrahedron)) {
//					++m_numTetrahedraOnSurface;
//				}
//			} else if (nPts == 5) {
//				int[][] tet = new int[][] { { 0, 1, 2, 3 }, { 1, 2, 3, 4 },
//						{ 0, 2, 3, 4 }, { 0, 1, 3, 4 }, { 0, 1, 2, 4 }, };
//				int[] rem = new int[] { 4, 0, 1, 2, 3 };
//				double maxVol = 0.0;
//				int h0 = -1;
//				Tetrahedron tetrahedron0 = new Tetrahedron();
//				tetrahedron0.m_data = PRIMITIVE_ON_SURFACE;
//				for (int h = 0; h < 5; ++h) {
//					double v = computeVolume4(pts[tet[h][0]], pts[tet[h][1]],
//							pts[tet[h][2]], pts[tet[h][3]]);
//					if (v > maxVol) {
//						h0 = h;
//						tetrahedron0.m_pts[0] = pts[tet[h][0]];
//						tetrahedron0.m_pts[1] = pts[tet[h][1]];
//						tetrahedron0.m_pts[2] = pts[tet[h][2]];
//						tetrahedron0.m_pts[3] = pts[tet[h][3]];
//						maxVol = v;
//					} else if (-v > maxVol) {
//						h0 = h;
//						tetrahedron0.m_pts[0] = pts[tet[h][1]];
//						tetrahedron0.m_pts[1] = pts[tet[h][0]];
//						tetrahedron0.m_pts[2] = pts[tet[h][2]];
//						tetrahedron0.m_pts[3] = pts[tet[h][3]];
//						maxVol = -v;
//					}
//				}
//				if (h0 == -1)
//					return;
//				if (add(tetrahedron0)) {
//					++m_numTetrahedraOnSurface;
//				} else {
//					return;
//				}
//				int a = rem[h0];
//				maxVol = 0.0;
//				int h1 = -1;
//				Tetrahedron tetrahedron1 = new Tetrahedron();
//				tetrahedron1.m_data = PRIMITIVE_ON_SURFACE;
//				for (int h = 0; h < 4; ++h) {
//					double v = computeVolume4(pts[a],
//							tetrahedron0.m_pts[tetF[h][0]],
//							tetrahedron0.m_pts[tetF[h][1]],
//							tetrahedron0.m_pts[tetF[h][2]]);
//					if (v > maxVol) {
//						h1 = h;
//						tetrahedron1.m_pts[0] = pts[a];
//						tetrahedron1.m_pts[1] = tetrahedron0.m_pts[tetF[h][0]];
//						tetrahedron1.m_pts[2] = tetrahedron0.m_pts[tetF[h][1]];
//						tetrahedron1.m_pts[3] = tetrahedron0.m_pts[tetF[h][2]];
//						maxVol = v;
//					}
//				}
//				if (h1 == -1 && add(tetrahedron1)) {
//					++m_numTetrahedraOnSurface;
//				}
//			} else if (nPts == 6) {
//
//				int[][] tet = new int[][] { { 2, 3, 4, 5 }, { 1, 3, 4, 5 },
//						{ 1, 2, 4, 5 }, { 1, 2, 3, 5 }, { 1, 2, 3, 4 },
//						{ 0, 3, 4, 5 }, { 0, 2, 4, 5 }, { 0, 2, 3, 5 },
//						{ 0, 2, 3, 4 }, { 0, 1, 4, 5 }, { 0, 1, 3, 5 },
//						{ 0, 1, 3, 4 }, { 0, 1, 2, 5 }, { 0, 1, 2, 4 },
//						{ 0, 1, 2, 3 } };
//				int[][] rem = new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 },
//						{ 0, 4 }, { 0, 5 }, { 1, 2 }, { 1, 3 }, { 1, 4 },
//						{ 1, 5 }, { 2, 3 }, { 2, 4 }, { 2, 5 }, { 3, 4 },
//						{ 3, 5 }, { 4, 5 } };
//				double maxVol = 0.0;
//				int h0 = -1;
//				Tetrahedron tetrahedron0 = new Tetrahedron();
//				tetrahedron0.m_data = PRIMITIVE_ON_SURFACE;
//				for (int h = 0; h < 15; ++h) {
//					double v = computeVolume4(pts[tet[h][0]], pts[tet[h][1]],
//							pts[tet[h][2]], pts[tet[h][3]]);
//					if (v > maxVol) {
//						h0 = h;
//						tetrahedron0.m_pts[0] = pts[tet[h][0]];
//						tetrahedron0.m_pts[1] = pts[tet[h][1]];
//						tetrahedron0.m_pts[2] = pts[tet[h][2]];
//						tetrahedron0.m_pts[3] = pts[tet[h][3]];
//						maxVol = v;
//					} else if (-v > maxVol) {
//						h0 = h;
//						tetrahedron0.m_pts[0] = pts[tet[h][1]];
//						tetrahedron0.m_pts[1] = pts[tet[h][0]];
//						tetrahedron0.m_pts[2] = pts[tet[h][2]];
//						tetrahedron0.m_pts[3] = pts[tet[h][3]];
//						maxVol = -v;
//					}
//				}
//				if (h0 == -1)
//					return;
//				if (add(tetrahedron0)) {
//					++m_numTetrahedraOnSurface;
//				} else {
//					return;
//				}
//
//				int a0 = rem[h0][0];
//				int a1 = rem[h0][1];
//				int h1 = -1;
//				Tetrahedron tetrahedron1 = new Tetrahedron();
//				tetrahedron1.m_data = PRIMITIVE_ON_SURFACE;
//				maxVol = 0.0;
//				for (int h = 0; h < 4; ++h) {
//					double v = computeVolume4(pts[a0],
//							tetrahedron0.m_pts[tetF[h][0]],
//							tetrahedron0.m_pts[tetF[h][1]],
//							tetrahedron0.m_pts[tetF[h][2]]);
//					if (v > maxVol) {
//						h1 = h;
//						tetrahedron1.m_pts[0] = pts[a0];
//						tetrahedron1.m_pts[1] = tetrahedron0.m_pts[tetF[h][0]];
//						tetrahedron1.m_pts[2] = tetrahedron0.m_pts[tetF[h][1]];
//						tetrahedron1.m_pts[3] = tetrahedron0.m_pts[tetF[h][2]];
//						maxVol = v;
//					}
//				}
//				if (h1 != -1 && add(tetrahedron1)) {
//					++m_numTetrahedraOnSurface;
//				} else {
//					h1 = -1;
//				}
//				maxVol = 0.0;
//				int h2 = -1;
//				Tetrahedron tetrahedron2 = new Tetrahedron();
//				tetrahedron2.m_data = PRIMITIVE_ON_SURFACE;
//				for (int h = 0; h < 4; ++h) {
//					double v = computeVolume4(pts[a0],
//							tetrahedron0.m_pts[tetF[h][0]],
//							tetrahedron0.m_pts[tetF[h][1]],
//							tetrahedron0.m_pts[tetF[h][2]]);
//					if (h == h1)
//						continue;
//					if (v > maxVol) {
//						h2 = h;
//						tetrahedron2.m_pts[0] = pts[a1];
//						tetrahedron2.m_pts[1] = tetrahedron0.m_pts[tetF[h][0]];
//						tetrahedron2.m_pts[2] = tetrahedron0.m_pts[tetF[h][1]];
//						tetrahedron2.m_pts[3] = tetrahedron0.m_pts[tetF[h][2]];
//						maxVol = v;
//					}
//				}
//				if (h1 != -1) {
//					for (int h = 0; h < 4; ++h) {
//						double v = computeVolume4(pts[a1],
//								tetrahedron1.m_pts[tetF[h][0]],
//								tetrahedron1.m_pts[tetF[h][1]],
//								tetrahedron1.m_pts[tetF[h][2]]);
//						if (h == 1)
//							continue;
//						if (v > maxVol) {
//							h2 = h;
//							tetrahedron2.m_pts[0] = pts[a1];
//							tetrahedron2.m_pts[1] = tetrahedron1.m_pts[tetF[h][0]];
//							tetrahedron2.m_pts[2] = tetrahedron1.m_pts[tetF[h][1]];
//							tetrahedron2.m_pts[3] = tetrahedron1.m_pts[tetF[h][2]];
//							maxVol = v;
//						}
//					}
//				}
//				if (h2 != -1 && add(tetrahedron2)) {
//					++m_numTetrahedraOnSurface;
//				}
//			}
//		}
//
//		@Override
//		public int getNPrimitives() {
//			return m_tetrahedra.size();
//		}
//
//		@Override
//		public void selectOnSurface(PrimitiveSet onSurfP) {
//			TetrahedronSet onSurf = (TetrahedronSet) onSurfP;
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//			onSurf.m_tetrahedra.clear();
//			onSurf.m_scale = m_scale;
//			onSurf.m_numTetrahedraOnSurface = 0;
//			onSurf.m_numTetrahedraInsideSurface = 0;
//			onSurf.m_barycenter = m_barycenter;
//			onSurf.m_minBB = m_minBB;
//			onSurf.m_maxBB = m_maxBB;
//			for (int i = 0; i < 3; ++i) {
//				for (int j = 0; j < 3; ++j) {
//					onSurf.m_Q[i][j] = m_Q[i][j];
//					onSurf.m_D[i][j] = m_D[i][j];
//				}
//			}
//			Tetrahedron tetrahedron;
//			for (int v = 0; v < nTetrahedra; ++v) {
//				tetrahedron = m_tetrahedra.get(v);
//				if (tetrahedron.m_data == PRIMITIVE_ON_SURFACE) {
//					onSurf.m_tetrahedra.add(tetrahedron);
//					++onSurf.m_numTetrahedraOnSurface;
//				}
//			}
//		}
//
//		@Override
//		public void intersect(Plane plane, List<Vector3d> positivePoints,
//				List<Vector3d> negativePoints, int sampling) {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//		}
//
//		@Override
//		public void computeConvexHull(HACDMesh mesh, int sampling) {
//			int CLUSTER_SIZE = 65536;
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//
//			List<Vector3d> cpoints = new ArrayList<Vector3d>();
//
//			Vector3d[] points = new Vector3d[CLUSTER_SIZE];
//			int p = 0;
//			while (p < nTetrahedra) {
//				int q = 0;
//				int s = 0;
//				while (q < CLUSTER_SIZE && p < nTetrahedra) {
//					if (m_tetrahedra.get(p).m_data == PRIMITIVE_ON_SURFACE) {
//						++s;
//						if (s == sampling) {
//							s = 0;
//							for (int a = 0; a < 4; ++a) {
//								points[q++] = m_tetrahedra.get(p).m_pts[a];
//							}
//						}
//					}
//					++p;
//				}
//				List<Vector3f> lPoints = new ArrayList<Vector3f>();
//				for (Vector3d point : points) {
//					lPoints.add(new Vector3f(point.getXf(), point.getYf(),
//							point.getZf()));
//				}
//				List<Vector3f> chVertices = Quickhull
//						.computeConvexHullVertices(lPoints);
//				for (int v = 0; v < chVertices.size(); v++) {
//					cpoints.add(new Vector3d(chVertices.get(v).getX(),
//							chVertices.get(v).getY(), chVertices.get(v).getZ()));
//				}
//			}
//			points = new Vector3d[CLUSTER_SIZE];
//
//			points = (Vector3d[]) cpoints.toArray();
//			List<Vector3f> lPoints = new ArrayList<Vector3f>();
//			for (Vector3d point : points) {
//				lPoints.add(new Vector3f(point.getXf(), point.getYf(), point
//						.getZf()));
//			}
//			List<Quickhull.Triangle> chFaces = new ArrayList<Quickhull.Triangle>();
//			List<Vector3f> chVertices = Quickhull.computeConvexHullVertices(
//					lPoints, chFaces);
//			mesh.clearPoints();
//			mesh.clearTriangles();
//			for (int v = 0; v < chVertices.size(); v++) {
//				mesh.addPoint(new Vector3d(chVertices.get(v).getX(), chVertices
//						.get(v).getY(), chVertices.get(v).getZ()));
//			}
//			int nt = chFaces.size();
//			for (int t = 0; t < nt; ++t) {
//				mesh.addTriangle(new Integer[] {
//						chVertices.indexOf(chFaces.get(t).a),
//						chVertices.indexOf(chFaces.get(t).b),
//						chVertices.indexOf(chFaces.get(t).c) });
//				// const btConvexHullComputer::Edge * sourceEdge =
//				// &(ch.edges[ch.faces[t]]);
//				// int a = sourceEdge->getSourceVertex();
//				// int b = sourceEdge->getTargetVertex();
//				// const btConvexHullComputer::Edge * edge =
//				// sourceEdge->getNextEdgeOfFace();
//				// int c = edge->getTargetVertex();
//				// while (c != a)
//				// {
//				// meshCH.AddTriangle(Vec3<int>(a, b, c));
//				// edge = edge->getNextEdgeOfFace();
//				// b = c;
//				// c = edge->getTargetVertex();
//				// }
//			}
//		}
//
//		@Override
//		public void computeClippedVolumes(Plane plane, double positiveVolume,
//				double negativeVolume) {
//			int nTetrahedra = m_tetrahedra.size();
//			if (nTetrahedra == 0)
//				return;
//		}
//	}
//
//	private class VoxelSet extends PrimitiveSet {
//		Vector3d m_minBB, m_minBBVoxels, m_maxBBVoxels, m_minBBPts, m_maxBBPts,
//				m_barycenterPCA;
//		int m_numVoxelsOnSurface, m_numVoxelsInsideSurface;
//		int[] m_dim;
//		double[][] m_Q, m_D;
//		double m_scale, m_unitVolume;
//		List<Voxel> m_voxels;
//		char[] m_data;
//
//		public VoxelSet() {
//
//		}
//
//		public char getVoxel(int i, int j, int k) {
//			return m_data[i + j * m_dim[0] + k * m_dim[0] * m_dim[1]];
//		}
//
//		public Vector3d getPoint(Vector3d voxel) {
//			return new Vector3d(voxel.x * m_scale + m_minBB.x, voxel.y
//					* m_scale + m_minBB.y, voxel.z * m_scale + m_minBB.z);
//		}
//
//		public Vector3d getPoint(Voxel voxel) {
//			return new Vector3d(voxel.m_coord[0] * m_scale + m_minBB.x,
//					voxel.m_coord[1] * m_scale + m_minBB.y, voxel.m_coord[2]
//							* m_scale + m_minBB.z);
//		}
//
//		public void getPoints(Voxel voxel, Vector3d[] pts) {
//			short i = voxel.m_coord[0];
//			short j = voxel.m_coord[1];
//			short k = voxel.m_coord[2];
//			pts[0].x = (i - 0.5) * m_scale + m_minBB.x;
//			pts[1].x = (i + 0.5) * m_scale + m_minBB.x;
//			pts[2].x = (i + 0.5) * m_scale + m_minBB.x;
//			pts[3].x = (i - 0.5) * m_scale + m_minBB.x;
//			pts[4].x = (i - 0.5) * m_scale + m_minBB.x;
//			pts[5].x = (i + 0.5) * m_scale + m_minBB.x;
//			pts[6].x = (i + 0.5) * m_scale + m_minBB.x;
//			pts[7].x = (i - 0.5) * m_scale + m_minBB.x;
//			pts[0].y = (j - 0.5) * m_scale + m_minBB.y;
//			pts[1].y = (j - 0.5) * m_scale + m_minBB.y;
//			pts[2].y = (j + 0.5) * m_scale + m_minBB.y;
//			pts[3].y = (j + 0.5) * m_scale + m_minBB.y;
//			pts[4].y = (j - 0.5) * m_scale + m_minBB.y;
//			pts[5].y = (j - 0.5) * m_scale + m_minBB.y;
//			pts[6].y = (j + 0.5) * m_scale + m_minBB.y;
//			pts[7].y = (j + 0.5) * m_scale + m_minBB.y;
//			pts[0].z = (k - 0.5) * m_scale + m_minBB.z;
//			pts[1].z = (k - 0.5) * m_scale + m_minBB.z;
//			pts[2].z = (k - 0.5) * m_scale + m_minBB.z;
//			pts[3].z = (k - 0.5) * m_scale + m_minBB.z;
//			pts[4].z = (k + 0.5) * m_scale + m_minBB.z;
//			pts[5].z = (k + 0.5) * m_scale + m_minBB.z;
//			pts[6].z = (k + 0.5) * m_scale + m_minBB.z;
//			pts[7].z = (k + 0.5) * m_scale + m_minBB.z;
//		}
//
//		public Vector3d getMinBBVoxels() {
//			return m_minBBVoxels;
//		}
//
//		public Vector3d getMaxBBVoxels() {
//			return m_maxBBVoxels;
//		}
//
//		@Override
//		public double computeVolume() {
//			return m_unitVolume * m_voxels.size();
//		}
//
//		@Override
//		public void computeBB() {
//			int nVoxels = m_voxels.size();
//			if (nVoxels == 0)
//				return;
//			m_minBBVoxels.x = m_voxels.get(0).m_coord[0];
//			m_maxBBVoxels.x = m_voxels.get(0).m_coord[0];
//			m_minBBVoxels.y = m_voxels.get(0).m_coord[1];
//			m_maxBBVoxels.y = m_voxels.get(0).m_coord[1];
//			m_minBBVoxels.z = m_voxels.get(0).m_coord[2];
//			m_maxBBVoxels.z = m_voxels.get(0).m_coord[2];
//			Vector3d bary = new Vector3d(0, 0, 0);
//			for (int p = 0; p < nVoxels; ++p) {
//				Voxel v = m_voxels.get(p);
//				bary.x += v.m_coord[0];
//				if (m_minBBVoxels.x > v.m_coord[0])
//					m_minBBVoxels.x = v.m_coord[0];
//				if (m_maxBBVoxels.x < v.m_coord[0])
//					m_maxBBVoxels.x = v.m_coord[0];
//				bary.y += v.m_coord[1];
//				if (m_minBBVoxels.y > v.m_coord[1])
//					m_minBBVoxels.y = v.m_coord[1];
//				if (m_maxBBVoxels.y < v.m_coord[1])
//					m_maxBBVoxels.y = v.m_coord[1];
//				bary.z += v.m_coord[2];
//				if (m_minBBVoxels.z > v.m_coord[2])
//					m_minBBVoxels.z = v.m_coord[2];
//				if (m_maxBBVoxels.z < v.m_coord[2])
//					m_maxBBVoxels.z = v.m_coord[2];
//			}
//			bary.scale(1 / (double) nVoxels);
//			m_minBBPts.x = m_minBBVoxels.x * m_scale + m_minBB.x;
//			m_maxBBPts.x = m_maxBBVoxels.x * m_scale + m_minBB.x;
//			m_barycenter.x = (short) (bary.x + 0.5);
//			m_minBBPts.y = m_minBBVoxels.y * m_scale + m_minBB.y;
//			m_maxBBPts.y = m_maxBBVoxels.y * m_scale + m_minBB.y;
//			m_barycenter.y = (short) (bary.y + 0.5);
//			m_minBBPts.z = m_minBBVoxels.z * m_scale + m_minBB.z;
//			m_maxBBPts.z = m_maxBBVoxels.z * m_scale + m_minBB.z;
//			m_barycenter.z = (short) (bary.z + 0.5);
//		}
//
//		@Override
//		public void computePrincipalAxes() {
//			int nVoxels = m_voxels.size();
//			if (nVoxels == 0)
//				return;
//			m_barycenterPCA.set(0, 0, 0);
//			for (int v = 0; v < nVoxels; ++v) {
//				Voxel voxel = m_voxels.get(v);
//				m_barycenterPCA.x += voxel.m_coord[0];
//				m_barycenterPCA.y += voxel.m_coord[1];
//				m_barycenterPCA.z += voxel.m_coord[2];
//			}
//			m_barycenterPCA.scale(1 / (double) nVoxels);
//
//			double[][] covMat = new double[][] { { 0.0, 0.0, 0.0 },
//					{ 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
//			double x, y, z;
//			for (int v = 0; v < nVoxels; ++v) {
//				Voxel voxel = m_voxels.get(v);
//				x = voxel.m_coord[0] - m_barycenter.x;
//				y = voxel.m_coord[1] - m_barycenter.y;
//				z = voxel.m_coord[2] - m_barycenter.z;
//				covMat[0][0] += x * x;
//				covMat[1][1] += y * y;
//				covMat[2][2] += z * z;
//				covMat[0][1] += x * y;
//				covMat[0][2] += x * z;
//				covMat[1][2] += y * z;
//			}
//			covMat[0][0] /= nVoxels;
//			covMat[1][1] /= nVoxels;
//			covMat[2][2] /= nVoxels;
//			covMat[0][1] /= nVoxels;
//			covMat[0][2] /= nVoxels;
//			covMat[1][2] /= nVoxels;
//			covMat[1][0] = covMat[0][1];
//			covMat[2][0] = covMat[0][2];
//			covMat[2][1] = covMat[1][2];
//			diagonalize(covMat, m_Q, m_D);
//		}
//
//		@Override
//		public void alignToPrincipalAxes() {
//			// ROT???????
//			short i0 = (short) m_dim[0];
//			short j0 = (short) m_dim[1];
//			short k0 = (short) m_dim[2];
//			Vector3d barycenter = new Vector3d(0, 0, 0);
//			int nVoxels = 0;
//			for (short i = 0; i < i0; ++i) {
//				for (short j = 0; j < j0; ++j) {
//					for (short k = 0; k < k0; ++k) {
//						char value = getVoxel(i, j, k);
//						if (value == PRIMITIVE_INSIDE_SURFACE
//								|| value == PRIMITIVE_ON_SURFACE) {
//							barycenter.x += i;
//							barycenter.y += j;
//							barycenter.z += k;
//							++nVoxels;
//						}
//					}
//				}
//			}
//			barycenter.scale(1 / (double) nVoxels);
//
//			double[][] covMat = new double[][] { { 0.0, 0.0, 0.0 },
//					{ 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
//			double x, y, z;
//			for (short i = 0; i < i0; ++i) {
//				for (short j = 0; j < j0; ++j) {
//					for (short k = 0; k < k0; ++k) {
//						char value = getVoxel(i, j, k);
//						if (value == PRIMITIVE_INSIDE_SURFACE
//								|| value == PRIMITIVE_ON_SURFACE) {
//							x = i - barycenter.x;
//							y = j - barycenter.y;
//							z = k - barycenter.z;
//							covMat[0][0] += x * x;
//							covMat[1][1] += y * y;
//							covMat[2][2] += z * z;
//							covMat[0][1] += x * y;
//							covMat[0][2] += x * z;
//							covMat[1][2] += y * z;
//						}
//					}
//				}
//			}
//			covMat[1][0] = covMat[0][1];
//			covMat[2][0] = covMat[0][2];
//			covMat[2][1] = covMat[1][2];
//			double[][] D = new double[3][3];
//			diagonalize(covMat, rot, D);
//		}
//
//		@Override
//		public HACDMesh getConvexHull() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public void computeConvexHull(HACDMesh convexHull) {
//			computeConvexHull(convexHull, 1);
//		}
//
//		@Override
//		public double computeMaxVolumeError() {
//			return m_unitVolume * m_numVoxelsOnSurface;
//		}
//
//		@Override
//		public double getEigenValue(int axis) {
//			return m_D[axis][axis];
//		}
//
//		@Override
//		public PrimitiveSet create() {
//			return new VoxelSet();
//		}
//
//		@Override
//		public void revertAlignToPrincipalAxes() {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void clip(Plane plane, PrimitiveSet positivePartP,
//				PrimitiveSet negativePartP) {
//			VoxelSet positivePart = (VoxelSet) positivePartP;
//			VoxelSet negativePart = (VoxelSet) negativePartP;
//			int nVoxels = m_voxels.size();
//			if (nVoxels == 0)
//				return;
//
//			negativePart.m_minBB.x = positivePart.m_minBB.x = m_minBB.x;
//			negativePart.m_minBB.y = positivePart.m_minBB.y = m_minBB.y;
//			negativePart.m_minBB.z = positivePart.m_minBB.z = m_minBB.z;
//
//			positivePart.m_voxels.clear();
//			negativePart.m_voxels.clear();
//			negativePart.m_scale = positivePart.m_scale = m_scale;
//			negativePart.m_unitVolume = positivePart.m_unitVolume = m_unitVolume;
//			negativePart.m_numVoxelsOnSurface = positivePart.m_numVoxelsOnSurface = 0;
//			negativePart.m_numVoxelsInsideSurface = positivePart.m_numVoxelsInsideSurface = 0;
//
//			double d;
//			Vector3d pt;
//			Voxel voxel;
//			double d0 = m_scale;
//			for (int v = 0; v < nVoxels; ++v) {
//				voxel = m_voxels.get(v);
//				pt = getPoint(voxel);
//				d = plane.m_a * pt.x + plane.m_b * pt.y + plane.m_c * pt.z
//						+ plane.m_d;
//				if (d >= 0.0) {
//					if (voxel.m_data == PRIMITIVE_ON_SURFACE || d <= d0) {
//						voxel.m_data = PRIMITIVE_ON_SURFACE;
//						positivePart.m_voxels.add(voxel);
//						++positivePart.m_numVoxelsOnSurface;
//					} else {
//						positivePart.m_voxels.add(voxel);
//						++positivePart.m_numVoxelsInsideSurface;
//					}
//				} else {
//					if (voxel.m_data == PRIMITIVE_ON_SURFACE || -d <= d0) {
//						voxel.m_data = PRIMITIVE_ON_SURFACE;
//						negativePart.m_voxels.add(voxel);
//						++negativePart.m_numVoxelsOnSurface;
//					} else {
//						negativePart.m_voxels.add(voxel);
//						++negativePart.m_numVoxelsInsideSurface;
//					}
//				}
//			}
//		}
//
//		@Override
//		public int getNPrimitives() {
//			return m_voxels.size();
//		}
//
//		@Override
//		public void selectOnSurface(PrimitiveSet onSurfP) {
//			VoxelSet onSurf = (VoxelSet) onSurfP;
//			int nVoxels = m_voxels.size();
//			if (nVoxels == 0)
//				return;
//
//			onSurf.m_minBB.x = m_minBB.x;
//			onSurf.m_minBB.y = m_minBB.y;
//			onSurf.m_minBB.z = m_minBB.z;
//
//			onSurf.m_voxels.clear();
//			onSurf.m_scale = m_scale;
//			onSurf.m_unitVolume = m_unitVolume;
//			onSurf.m_numVoxelsOnSurface = 0;
//			onSurf.m_numVoxelsInsideSurface = 0;
//			Voxel voxel;
//			for (int v = 0; v < nVoxels; ++v) {
//				voxel = m_voxels.get(v);
//				if (voxel.m_data == PRIMITIVE_ON_SURFACE) {
//					onSurf.m_voxels.add(voxel);
//					++onSurf.m_numVoxelsOnSurface;
//				}
//			}
//		}
//
//		@Override
//		public void intersect(Plane plane, List<Vector3d> positivePoints,
//				List<Vector3d> negativePoints, int sampling) {
//			int nVoxels = m_voxels.size();
//			if (nVoxels == 0)
//				return;
//			double d0 = m_scale;
//			double d;
//			Vector3d[] pts = new Vector3d[8];
//			Vector3d pt;
//			Voxel voxel;
//			int sp = 0;
//			int sn = 0;
//			for (int v = 0; v < nVoxels; ++v) {
//				voxel = m_voxels.get(v);
//				pt = getPoint(voxel);
//				d = plane.m_a * pt.x + plane.m_b * pt.y + plane.m_c * pt.z
//						+ plane.m_d;
//				// if (d >= 0.0 && d <= d0) positivePts.PushBack(pt);
//				// else if (d < 0.0 && -d <= d0) negativePts.PushBack(pt);
//				if (d >= 0.0) {
//					if (d <= d0) {
//						getPoints(voxel, pts);
//						for (int k = 0; k < 8; ++k) {
//							positivePoints.add(pts[k]);
//						}
//					} else {
//						if (++sp == sampling) {
//							// positivePts.PushBack(pt);
//							getPoints(voxel, pts);
//							for (int k = 0; k < 8; ++k) {
//								positivePoints.add(pts[k]);
//							}
//							sp = 0;
//						}
//					}
//				} else {
//					if (-d <= d0) {
//						getPoints(voxel, pts);
//						for (int k = 0; k < 8; ++k) {
//							negativePoints.add(pts[k]);
//						}
//					} else {
//						if (++sn == sampling) {
//							// negativePts.PushBack(pt);
//							getPoints(voxel, pts);
//							for (int k = 0; k < 8; ++k) {
//								negativePoints.add(pts[k]);
//							}
//							sn = 0;
//						}
//					}
//				}
//			}
//		}
//
//		@Override
//		public void computeConvexHull(HACDMesh mesh, int sampling) {
//			
//			//TODO
//			int CLUSTER_SIZE = 65536;
//			int nVoxels = m_voxels.size();
//	        if (nVoxels == 0)
//	            return;
//
//	        List<Vector3d> cpoints;
//
//	        Vector3d[] points = new Vector3d[CLUSTER_SIZE];
//	        int p = 0;
//	        int s = 0;
//	        short i, j, k;
//	        while (p < nVoxels)
//	        {
//	        	int q = 0;
//	            while (q < CLUSTER_SIZE && p < nVoxels)
//	            {
//	                if (m_voxels.get(p).m_data == PRIMITIVE_ON_SURFACE)
//	                {
//	                    ++s;
//	                    if (s == sampling)
//	                    {
//	                        s = 0;
//	                        i = m_voxels.get(p).m_coord[0];
//	                        j = m_voxels.get(p).m_coord[1];
//	                        k = m_voxels.get(p).m_coord[2];
//	                        Vector3d p0 = new Vector3d((i - 0.5) * m_scale, (j - 0.5) * m_scale, (k - 0.5) * m_scale);
//	                        Vector3d p1 = new Vector3d((i + 0.5) * m_scale, (j - 0.5) * m_scale, (k - 0.5) * m_scale);
//	                        Vector3d p2 = new Vector3d((i + 0.5) * m_scale, (j + 0.5) * m_scale, (k - 0.5) * m_scale);
//	                        Vector3d p3 = new Vector3d((i - 0.5) * m_scale, (j + 0.5) * m_scale, (k - 0.5) * m_scale);
//	                        Vector3d p4 = new Vector3d((i - 0.5) * m_scale, (j - 0.5) * m_scale, (k + 0.5) * m_scale);
//	                        Vector3d p5 = new Vector3d((i + 0.5) * m_scale, (j - 0.5) * m_scale, (k + 0.5) * m_scale);
//	                        Vector3d p6 = new Vector3d((i + 0.5) * m_scale, (j + 0.5) * m_scale, (k + 0.5) * m_scale);
//	                        Vector3d p7 = new Vector3d((i - 0.5) * m_scale, (j + 0.5) * m_scale, (k + 0.5) * m_scale);
//	                        points[q++] = VecMath.addition(p0, m_minBB);
//	                        points[q++] = VecMath.addition(p1, m_minBB);
//	                        points[q++] = VecMath.addition(p2, m_minBB);
//	                        points[q++] = VecMath.addition(p3, m_minBB);
//	                        points[q++] = VecMath.addition(p4, m_minBB);
//	                        points[q++] = VecMath.addition(p5, m_minBB);
//	                        points[q++] = VecMath.addition(p6, m_minBB);
//	                        points[q++] = VecMath.addition(p7, m_minBB);
//	                    }
//	                }
//	                ++p;
//	            }
//	            btConvexHullComputer ch;
//	            ch.compute((double)points, 3 * sizeof(double), (int)q, -1.0, -1.0);
//	            for (int v = 0; v < ch.vertices.size(); v++)
//	            {
//	                cpoints.add(new Vector3d(ch.vertices[v].getX(), ch.vertices[v].getY(), ch.vertices[v].getZ()));
//	            }
//	        }
//	        points = new Vector3d[CLUSTER_SIZE];
//
//	        points = cpoints.data();
//	        btConvexHullComputer ch;
//	        ch.compute((double *)points, 3 * sizeof(double), (int)cpoints.Size(), -1.0, -1.0);
//	        mesh.clearPoints();
//	        mesh.clearTriangles();
//	        for (int v = 0; v < ch.vertices.size(); v++)
//	        {
//	            mesh.addPoint(new Vector3d(ch.vertices[v].getX(), ch.vertices[v].getY(), ch.vertices[v].getZ()));
//	        }
//	        int nt = ch.faces.size();
//	        for (int t = 0; t < nt; ++t)
//	        {
//	            const btConvexHullComputer::Edge * sourceEdge = &(ch.edges[ch.faces[t]]);
//	            int a = sourceEdge.getSourceVertex();
//	            int b = sourceEdge.getTargetVertex();
//	            const btConvexHullComputer::Edge * edge = sourceEdge.getNextEdgeOfFace();
//	            int c = edge.getTargetVertex();
//	            while (c != a)
//	            {
//	                meshCH.AddTriangle(Vec3<int>(a, b, c));
//	                edge = edge.getNextEdgeOfFace();
//	                b = c;
//	                c = edge.getTargetVertex();
//	            }
//	        }
//		}
//
//		@Override
//		public void computeClippedVolumes(Plane plane, double positiveVolume,
//				double negativeVolume) {
//			negativeVolume = 0.0;
//			positiveVolume = 0.0;
//			int nVoxels = m_voxels.size();
//			if (nVoxels == 0)
//				return;
//			double d;
//			Vector3d pt;
//			int nPositiveVoxels = 0;
//			for (int v = 0; v < nVoxels; ++v) {
//				pt = getPoint(m_voxels.get(v));
//				d = plane.m_a * pt.x + plane.m_b * pt.y + plane.m_c * pt.z
//						+ plane.m_d;
//				nPositiveVoxels += (d >= 0.0) ? 1 : 0;
//			}
//			int nNegativeVoxels = nVoxels - nPositiveVoxels;
//			positiveVolume = m_unitVolume * nPositiveVoxels;
//			negativeVolume = m_unitVolume * nNegativeVoxels;
//		}
//	}
//
//	private class HACDVolume {
//		int m_numVoxelsOnSurface, m_numVoxelsInsideSurface,
//				m_numVoxelsOutsideSurface;
//		Vector3d m_maxBB, m_minBB, m_dim;
//		int[] m_data;
//		double m_scale;
//
//		public HACDVolume() {
//			m_dim = new Vector3d();
//		}
//
//		public int getNPrimitivesOnSurf() {
//			return m_numVoxelsOnSurface;
//		}
//
//		public int getNPrimitivesInsideSurf() {
//			return m_numVoxelsInsideSurface;
//		}
//
//		public void alignToPrincipalAxes(double[][] rot) {
//			short i0 = (short) m_dim.x;
//			short j0 = (short) m_dim.y;
//			short k0 = (short) m_dim.z;
//			Vector3d barycenter = new Vector3d(0, 0, 0);
//			int nVoxels = 0;
//			for (short i = 0; i < i0; ++i) {
//				for (short j = 0; j < j0; ++j) {
//					for (short k = 0; k < k0; ++k) {
//						int value = getVoxel(i, j, k);
//						if (value == PRIMITIVE_INSIDE_SURFACE
//								|| value == PRIMITIVE_ON_SURFACE) {
//							barycenter.x += i;
//							barycenter.y += j;
//							barycenter.z += k;
//							++nVoxels;
//						}
//					}
//				}
//			}
//			barycenter.scale(1 / (double) nVoxels);
//
//			double[][] covMat = new double[][] { { 0.0, 0.0, 0.0 },
//					{ 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
//			double x, y, z;
//			for (short i = 0; i < i0; ++i) {
//				for (short j = 0; j < j0; ++j) {
//					for (short k = 0; k < k0; ++k) {
//						int value = getVoxel(i, j, k);
//						if (value == PRIMITIVE_INSIDE_SURFACE
//								|| value == PRIMITIVE_ON_SURFACE) {
//							x = i - barycenter.x;
//							y = j - barycenter.y;
//							z = k - barycenter.z;
//							covMat[0][0] += x * x;
//							covMat[1][1] += y * y;
//							covMat[2][2] += z * z;
//							covMat[0][1] += x * y;
//							covMat[0][2] += x * z;
//							covMat[1][2] += y * z;
//						}
//					}
//				}
//			}
//			covMat[1][0] = covMat[0][1];
//			covMat[2][0] = covMat[0][2];
//			covMat[2][1] = covMat[1][2];
//			double[][] D = new double[3][3];
//			diagonalize(covMat, rot, D);
//		}
//
//		public int getVoxel(int i, int j, int k) {
//			int id = (int) (i + j * m_dim.x + k * m_dim.x * m_dim.y) % m_data.length;
////			if (id < 0) System.out.println((i + j * m_dim.x + k * m_dim.x * m_dim.y) + " (" + i + "; " + j + "; " + k + "); " + m_data.length);
//			return m_data[Math.abs(id)];
//		}
//
//		public void setVoxel(int i, int j, int k, int set) {
//			m_data[(int) (i + j * m_dim.x + k * m_dim.x * m_dim.y)] = set;
//		}
//
//		public Vector3d computeAlignedPoint(Vector3d[] points, int idx,
//				Vector3d barycenter, double[][] rot) {
//			double x = points[(idx + 0)%points.length].x - barycenter.x;
//			double y = points[(idx + 1)%points.length].y - barycenter.y;
//			double z = points[(idx + 2)%points.length].z - barycenter.z;
//			return new Vector3d(rot[0][0] * x + rot[1][0] * y + rot[2][0] * z,
//					rot[0][1] * x + rot[1][1] * y + rot[2][1] * z, rot[0][2]
//							* x + rot[1][2] * y + rot[2][2] * z);
//		}
//
//		public void computeBB(Vector3d[] points, int stridePoints,
//				Vector3d barycenter, double[][] rot) {
//			Vector3d pt = computeAlignedPoint(points, 0, barycenter, rot);
//			m_maxBB = pt;
//			m_minBB = pt;
//			for (int v = 1; v < points.length; ++v) {
//				pt = computeAlignedPoint(points, v * stridePoints, barycenter,
//						rot);
//				if (pt.x < m_minBB.x)
//					m_minBB.x = pt.x;
//				else if (pt.x > m_maxBB.x)
//					m_maxBB.x = pt.x;
//				if (pt.y < m_minBB.y)
//					m_minBB.y = pt.y;
//				else if (pt.y > m_maxBB.y)
//					m_maxBB.y = pt.y;
//				if (pt.z < m_minBB.z)
//					m_minBB.z = pt.z;
//				else if (pt.z > m_maxBB.z)
//					m_maxBB.z = pt.z;
//			}
//		}
//
//		public void voxelize(Vector3d[] points, Integer[] triangles,
//				int stridePoints, int strideTriangles, Vector3d barycenter,
//				double[][] rot, int dim) {
//			if (points.length == 0)
//				return;
//			computeBB(points, stridePoints, barycenter, rot);
//
//			double[] d = new double[] { m_maxBB.x - m_minBB.x,
//					m_maxBB.y - m_minBB.y, m_maxBB.z - m_minBB.z };
//			double r;
//			if (d[0] > d[1] && d[0] > d[2]) {
//				r = d[0];
//				m_dim.x = dim;
//				m_dim.y = 2 + (int) (dim * d[1] / d[0]);
//				m_dim.z = 2 + (int) (dim * d[2] / d[0]);
//			} else if (d[1] > d[0] && d[1] > d[2]) {
//				r = d[1];
//				m_dim.y = dim;
//				m_dim.x = 2 + (int) (dim * d[0] / d[1]);
//				m_dim.z = 2 + (int) (dim * d[2] / d[1]);
//			} else {
//				r = d[2];
//				m_dim.z = dim;
//				m_dim.x = 2 + (int) (dim * d[0] / d[2]);
//				m_dim.y = 2 + (int) (dim * d[1] / d[2]);
//			}
//
//			m_scale = r / (dim - 1);
//			double invScale = (dim - 1) / r;
//
//			allocate();
//			m_numVoxelsOnSurface = 0;
//			m_numVoxelsInsideSurface = 0;
//			m_numVoxelsOutsideSurface = 0;
//
//			Vector3d[] p = new Vector3d[3];
//			int i, j, k;
//			int i0 = 0, j0 = 0, k0 = 0;
//			int i1 = 0, j1 = 0, k1 = 0;
//			Vector3d boxcenter = new Vector3d();
//			Vector3d pt;
//			Vector3d boxhalfsize = new Vector3d(0.5, 0.5, 0.5);
//			System.out.println("start for");
//			for (int t = 0, ti = 0; t < triangles.length; ++t, ti += strideTriangles) {
//				System.out.println(t + "; " + triangles.length);
//				int[] tri = new int[] { triangles[ti + 0], triangles[ti + 1],
//						triangles[ti + 2] };
//				for (int c = 0; c < 3; ++c) {
//					System.out.println("h");
//					pt = computeAlignedPoint(points, tri[c] * stridePoints,
//							barycenter, rot);
//					System.out.println("h2");
//					p[c] = new Vector3d();
//					p[c].x = (pt.x - m_minBB.x) * invScale;
//					p[c].y = (pt.y - m_minBB.y) * invScale;
//					p[c].z = (pt.z - m_minBB.z) * invScale;
//					i = (int) (p[c].x + 0.5);
//					j = (int) (p[c].y + 0.5);
//					k = (int) (p[c].z + 0.5);
////					assert (i < m_dim.x && i >= 0 && j < m_dim.y && j >= 0
////							&& k < m_dim.z && k >= 0);
//
//					if (c == 0) {
//						i0 = i1 = i;
//						j0 = j1 = j;
//						k0 = k1 = k;
//					} else {
//						if (i < i0)
//							i0 = i;
//						if (j < j0)
//							j0 = j;
//						if (k < k0)
//							k0 = k;
//						if (i > i1)
//							i1 = i;
//						if (j > j1)
//							j1 = j;
//						if (k > k1)
//							k1 = k;
//					}
//				}
//				if (i0 > 0)
//					--i0;
//				if (j0 > 0)
//					--j0;
//				if (k0 > 0)
//					--k0;
//				if (i1 < m_dim.x)
//					++i1;
//				if (j1 < m_dim.y)
//					++j1;
//				if (k1 < m_dim.z)
//					++k1;
//				for (i = i0; i < i1; ++i) {
//					boxcenter.x = (double) i;
//					for (j = j0; j < j1; ++j) {
//						boxcenter.y = (double) j;
//						for (k = k0; k < k1; ++k) {
//							boxcenter.z = (double) k;
//							// System.out.println("i " + boxcenter);
//							int res = triBoxOverlap(boxcenter, boxhalfsize,
//									p[0], p[1], p[2]);
//							int value = getVoxel(i, j, k);
//							if (res == 1 && value == PRIMITIVE_UNDEFINED) {
//								value = PRIMITIVE_ON_SURFACE;
//								++m_numVoxelsOnSurface;
//							}
//						}
//					}
//				}
//			}
//			fillOutsideSurface(0, 0, 0, (int) m_dim.x, (int) m_dim.y, 1);
//			fillOutsideSurface(0, 0, (int) m_dim.z - 1, (int) m_dim.x,
//					(int) m_dim.y, (int) m_dim.z);
//			fillOutsideSurface(0, 0, 0, (int) m_dim.x, 1, (int) m_dim.z);
//			fillOutsideSurface(0, (int) m_dim.y - 1, 0, (int) m_dim.x,
//					(int) m_dim.y, (int) m_dim.z);
//			fillOutsideSurface(0, 0, 0, 1, (int) m_dim.y, (int) m_dim.z);
//			fillOutsideSurface((int) m_dim.x - 1, 0, 0, (int) m_dim.x,
//					(int) m_dim.y, (int) m_dim.z);
//			fillInsideSurface();
//		}
//
//		public void fillOutsideSurface(int i0, int j0, int k0, int i1, int j1,
//				int k1) {
//			short[][] neighbours = new short[][] { { 1, 0, 0 }, { 0, 1, 0 },
//					{ 0, 0, 1 }, { -1, 0, 0 }, { 0, -1, 0 }, { 0, 0, -1 } };
//			LinkedList<Vector3d> fifo = new LinkedList<Vector3d>();
//			Vector3d current = new Vector3d();
//			short a, b, c;
//			for (int i = i0; i < i1; ++i) {
//				for (int j = j0; j < j1; ++j) {
//					for (int k = k0; k < k1; ++k) {
//
//						if (getVoxel(i, j, k) == PRIMITIVE_UNDEFINED) {
//							current.x = (short) i;
//							current.y = (short) j;
//							current.z = (short) k;
//							fifo.push(current);
//							setVoxel((int) current.x, (int) current.y,
//									(int) current.z, PRIMITIVE_OUTSIDE_SURFACE);
//							++m_numVoxelsOutsideSurface;
//							while (fifo.size() > 0) {
//								current = fifo.poll();
//								fifo.pop();
//								for (int h = 0; h < 6; ++h) {
//									a = (short) (current.x + neighbours[h][0]);
//									b = (short) (current.y + neighbours[h][1]);
//									c = (short) (current.z + neighbours[h][2]);
//									if (a < 0 || a >= (int) m_dim.x || b < 0
//											|| b >= (int) m_dim.y || c < 0
//											|| c >= (int) m_dim.z) {
//										continue;
//									}
//									int v = getVoxel(a, b, c);
//									if (v == PRIMITIVE_UNDEFINED) {
//										v = PRIMITIVE_OUTSIDE_SURFACE;
//										++m_numVoxelsOutsideSurface;
//										fifo.push(new Vector3d(a, b, c));
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//
//		public void fillInsideSurface() {
//			int i0 = (int) m_dim.z;
//			int j0 = (int) m_dim.y;
//			int k0 = (int) m_dim.z;
//			for (int i = 0; i < i0; ++i) {
//				for (int j = 0; j < j0; ++j) {
//					for (int k = 0; k < k0; ++k) {
//						int v = getVoxel(i, j, k);
//						if (v == PRIMITIVE_UNDEFINED) {
//							v = PRIMITIVE_INSIDE_SURFACE;
//							++m_numVoxelsInsideSurface;
//						}
//					}
//				}
//			}
//		}
//
//		public void convert(VoxelSet vset) {
//			vset.m_minBB.x = m_minBB.x;
//			vset.m_minBB.y = m_minBB.y;
//			vset.m_minBB.z = m_minBB.z;
//			vset.m_voxels = new ArrayList<Voxel>();
//			vset.m_scale = m_scale;
//			vset.m_unitVolume = m_scale * m_scale * m_scale;
//			short i0 = (short) m_dim.x;
//			short j0 = (short) m_dim.y;
//			short k0 = (short) m_dim.z;
//			Voxel voxel = new Voxel();
//			vset.m_numVoxelsOnSurface = 0;
//			vset.m_numVoxelsInsideSurface = 0;
//			for (short i = 0; i < i0; ++i) {
//				for (short j = 0; j < j0; ++j) {
//					for (short k = 0; k < k0; ++k) {
//						int value = getVoxel(i, j, k);
//						if (value == PRIMITIVE_INSIDE_SURFACE) {
//							voxel.m_coord[0] = i;
//							voxel.m_coord[1] = j;
//							voxel.m_coord[2] = k;
//							voxel.m_data = PRIMITIVE_INSIDE_SURFACE;
//							vset.m_voxels.add(voxel);
//							++vset.m_numVoxelsInsideSurface;
//						} else if (value == PRIMITIVE_ON_SURFACE) {
//							voxel.m_coord[0] = i;
//							voxel.m_coord[1] = j;
//							voxel.m_coord[2] = k;
//							voxel.m_data = PRIMITIVE_ON_SURFACE;
//							vset.m_voxels.add(voxel);
//							++vset.m_numVoxelsOnSurface;
//						}
//					}
//				}
//			}
//		}
//
//		public void convert(TetrahedronSet tset) {
//			tset.m_tetrahedra = new ArrayList<Tetrahedron>();
//			tset.m_scale = m_scale;
//			short i0 = (short) m_dim.x;
//			short j0 = (short) m_dim.y;
//			short k0 = (short) m_dim.z;
//			tset.m_numTetrahedraOnSurface = 0;
//			tset.m_numTetrahedraInsideSurface = 0;
//			Tetrahedron tetrahedron = new Tetrahedron();
//			for (short i = 0; i < i0; ++i) {
//				for (short j = 0; j < j0; ++j) {
//					for (short k = 0; k < k0; ++k) {
//						int value = getVoxel(i, j, k);
//						if (value == PRIMITIVE_INSIDE_SURFACE
//								|| value == PRIMITIVE_ON_SURFACE) {
//							tetrahedron.m_data = value;
//							Vector3d p1 = new Vector3d((i - 0.5) * m_scale
//									+ m_minBB.x, (j - 0.5) * m_scale
//									+ m_minBB.y, (k - 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p2 = new Vector3d((i + 0.5) * m_scale
//									+ m_minBB.x, (j - 0.5) * m_scale
//									+ m_minBB.y, (k - 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p3 = new Vector3d((i + 0.5) * m_scale
//									+ m_minBB.x, (j + 0.5) * m_scale
//									+ m_minBB.y, (k - 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p4 = new Vector3d((i - 0.5) * m_scale
//									+ m_minBB.x, (j + 0.5) * m_scale
//									+ m_minBB.y, (k - 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p5 = new Vector3d((i - 0.5) * m_scale
//									+ m_minBB.x, (j - 0.5) * m_scale
//									+ m_minBB.y, (k + 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p6 = new Vector3d((i + 0.5) * m_scale
//									+ m_minBB.x, (j - 0.5) * m_scale
//									+ m_minBB.y, (k + 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p7 = new Vector3d((i + 0.5) * m_scale
//									+ m_minBB.x, (j + 0.5) * m_scale
//									+ m_minBB.y, (k + 0.5) * m_scale
//									+ m_minBB.z);
//							Vector3d p8 = new Vector3d((i - 0.5) * m_scale
//									+ m_minBB.x, (j + 0.5) * m_scale
//									+ m_minBB.y, (k + 0.5) * m_scale
//									+ m_minBB.z);
//
//							tetrahedron.m_pts[0] = p2;
//							tetrahedron.m_pts[1] = p4;
//							tetrahedron.m_pts[2] = p7;
//							tetrahedron.m_pts[3] = p5;
//							tset.m_tetrahedra.add(tetrahedron);
//
//							tetrahedron.m_pts[0] = p6;
//							tetrahedron.m_pts[1] = p2;
//							tetrahedron.m_pts[2] = p7;
//							tetrahedron.m_pts[3] = p5;
//							tset.m_tetrahedra.add(tetrahedron);
//
//							tetrahedron.m_pts[0] = p3;
//							tetrahedron.m_pts[1] = p4;
//							tetrahedron.m_pts[2] = p7;
//							tetrahedron.m_pts[3] = p2;
//							tset.m_tetrahedra.add(tetrahedron);
//
//							tetrahedron.m_pts[0] = p1;
//							tetrahedron.m_pts[1] = p4;
//							tetrahedron.m_pts[2] = p2;
//							tetrahedron.m_pts[3] = p5;
//							tset.m_tetrahedra.add(tetrahedron);
//
//							tetrahedron.m_pts[0] = p8;
//							tetrahedron.m_pts[1] = p5;
//							tetrahedron.m_pts[2] = p7;
//							tetrahedron.m_pts[3] = p4;
//							tset.m_tetrahedra.add(tetrahedron);
//							if (value == PRIMITIVE_INSIDE_SURFACE) {
//								tset.m_numTetrahedraInsideSurface += 5;
//							} else {
//								tset.m_numTetrahedraOnSurface += 5;
//							}
//						}
//					}
//				}
//			}
//		}
//
//		public void allocate() {
//			m_data = null;
//			int size = (int) (m_dim.x * m_dim.y * m_dim.z);
//			m_data = new int[size];
//		}
//
//		public int triBoxOverlap(Vector3d boxcenter, Vector3d boxhalfsize,
//				Vector3d triver0, Vector3d triver1, Vector3d triver2) {
//			Vector3d v0, v1, v2;
//			double min, max, p0, p1, p2, rad, fex, fey, fez; // -NJMP- "d" local
//																// variable
//																// removed
//			Vector3d normal, e0, e1, e2;
//
//			/* This is the fastest branch on Sun */
//			/* move everything so that the boxcenter is in (0,0,0) */
//
//			v0 = VecMath.subtraction(triver0, boxcenter);
//			v1 = VecMath.subtraction(triver1, boxcenter);
//			v2 = VecMath.subtraction(triver2, boxcenter);
//
//			/* compute triangle edges */
//			e0 = VecMath.subtraction(v1, v0); /* tri edge 0 */
//			e1 = VecMath.subtraction(v2, v1); /* tri edge 1 */
//			e2 = VecMath.subtraction(v0, v2); /* tri edge 2 */
//
//			/* Bullet 3: */
//			/* test the 9 tests first (this was faster) */
//			fex = Math.abs(e0.x);
//			fey = Math.abs(e0.y);
//			fez = Math.abs(e0.z);
//
//			// AXISTEST_X01(e0.z, e0.y, fez, fey);
//			p0 = e0.z * v0.y - e0.y * v0.z;
//			p2 = e0.z * v2.y - e0.y * v2.z;
//			if (p0 < p2) {
//				min = p0;
//				max = p2;
//			} else {
//				min = p2;
//				max = p0;
//			}
//			rad = fez * boxhalfsize.y + fey * boxhalfsize.z;
//			if (min > rad || max < -rad)
//				return 0;
//			// AXISTEST_Y02(e0.z, e0.x, fez, fex);
//			p0 = -e0.z * v0.x + e0.x * v0.z;
//			p2 = -e0.z * v2.x + e0.x * v2.z;
//			if (p0 < p2) {
//				min = p0;
//				max = p2;
//			} else {
//				min = p2;
//				max = p0;
//			}
//			rad = fez * boxhalfsize.x + fex * boxhalfsize.z;
//			if (min > rad || max < -rad)
//				return 0;
//			// AXISTEST_Z12(e0.y, e0.x, fey, fex);
//			p1 = e0.y * v1.x - e0.x * v1.y;
//			p2 = e0.y * v2.x - e0.x * v2.y;
//			if (p2 < p1) {
//				min = p2;
//				max = p1;
//			} else {
//				min = p1;
//				max = p2;
//			}
//			rad = fey * boxhalfsize.x + fex * boxhalfsize.y;
//			if (min > rad || max < -rad)
//				return 0;
//
//			fex = Math.abs(e1.x);
//			fey = Math.abs(e1.y);
//			fez = Math.abs(e1.z);
//
//			// AXISTEST_X01(e1.z, e1.y, fez, fey);
//			p0 = e1.z * v0.y - e1.y * v0.z;
//			p2 = e1.z * v2.y - e1.y * v2.z;
//			if (p0 < p2) {
//				min = p0;
//				max = p2;
//			} else {
//				min = p2;
//				max = p0;
//			}
//			rad = fez * boxhalfsize.y + fey * boxhalfsize.z;
//			if (min > rad || max < -rad)
//				return 0;
//			// AXISTEST_Y02(e1.z, e1.x, fez, fex);
//			p0 = -e1.z * v0.x + e1.x * v0.z;
//			p2 = -e1.z * v2.x + e1.x * v2.z;
//			if (p0 < p2) {
//				min = p0;
//				max = p2;
//			} else {
//				min = p2;
//				max = p0;
//			}
//			rad = fez * boxhalfsize.x + fex * boxhalfsize.z;
//			if (min > rad || max < -rad)
//				return 0;
//			// AXISTEST_Z0(e1.y, e1.x, fey, fex);
//			p0 = e1.y * v0.x - e1.x * v0.y;
//			p1 = e1.y * v1.x - e1.x * v1.y;
//			if (p0 < p1) {
//				min = p0;
//				max = p1;
//			} else {
//				min = p1;
//				max = p0;
//			}
//			rad = fey * boxhalfsize.x + fex * boxhalfsize.y;
//			if (min > rad || max < -rad)
//				return 0;
//
//			fex = Math.abs(e2.x);
//			fey = Math.abs(e2.y);
//			fez = Math.abs(e2.z);
//
//			// AXISTEST_X2(e2.z, e2.y, fez, fey);
//			p0 = e2.z * v0.y - e2.y * v0.z;
//			p1 = e2.z * v1.y - e2.y * v1.z;
//			if (p0 < p1) {
//				min = p0;
//				max = p1;
//			} else {
//				min = p1;
//				max = p0;
//			}
//			rad = fez * boxhalfsize.y + fey * boxhalfsize.z;
//			if (min > rad || max < -rad)
//				return 0;
//			// AXISTEST_Y1(e2.z, e2.x, fez, fex);
//			p0 = -e2.z * v0.x + e2.x * v0.z;
//			p1 = -e2.z * v1.x + e2.x * v1.z;
//			if (p0 < p1) {
//				min = p0;
//				max = p1;
//			} else {
//				min = p1;
//				max = p0;
//			}
//			rad = fez * boxhalfsize.x + fex * boxhalfsize.z;
//			if (min > rad || max < -rad)
//				return 0;
//			// AXISTEST_Z12(e2.y, e2.x, fey, fex);
//			p1 = e2.y * v1.x - e2.x * v1.y;
//			p2 = e2.y * v2.x - e2.x * v2.y;
//			if (p2 < p1) {
//				min = p2;
//				max = p1;
//			} else {
//				min = p1;
//				max = p2;
//			}
//			rad = fey * boxhalfsize.x + fex * boxhalfsize.y;
//			if (min > rad || max < -rad)
//				return 0;
//
//			/* Bullet 1: */
//			/* first test overlap in the {x,y,z}-directions */
//			/*
//			 * find min, max of the triangle each direction, and test for
//			 * overlap in
//			 */
//			/*
//			 * that direction -- this is equivalent to testing a minimal AABB
//			 * around
//			 */
//			/* the triangle against the AABB */
//
//			/* test in X-direction */
//			FINDMINMAX(v0.x, v1.x, v2.x, min, max);
//			if (min > boxhalfsize.x || max < -boxhalfsize.x)
//				return 0;
//
//			/* test in Y-direction */
//			FINDMINMAX(v0.y, v1.y, v2.y, min, max);
//			if (min > boxhalfsize.y || max < -boxhalfsize.y)
//				return 0;
//
//			/* test in Z-direction */
//			FINDMINMAX(v0.z, v1.z, v2.z, min, max);
//			if (min > boxhalfsize.z || max < -boxhalfsize.z)
//				return 0;
//
//			/* Bullet 2: */
//			/* test if the box intersects the plane of the triangle */
//			/* compute plane equation of triangle: normal*x+d=0 */
//			normal = VecMath.crossproduct(e0, e1);
//
//			if (!planeBoxOverlap(normal, v0, boxhalfsize))
//				return 0;
//			return 1; /* box and triangle overlaps */
//		}
//
//		private void FINDMINMAX(double x0, double x1, double x2, double min,
//				double max) {
//			min = max = x0;
//			if (x1 < min)
//				min = x1;
//			if (x1 > max)
//				max = x1;
//			if (x2 < min)
//				min = x2;
//			if (x2 > max)
//				max = x2;
//		}
//
//		public boolean planeBoxOverlap(Vector3d normal, Vector3d vert,
//				Vector3d maxbox) {
//			Vector3d vmin = new Vector3d();
//			Vector3d vmax = new Vector3d();
//			double v;
//			v = vert.x;
//			if (normal.x > 0.0) {
//				vmin.x = -maxbox.x - v;
//				vmax.x = maxbox.x - v;
//			} else {
//				vmin.x = maxbox.x - v;
//				vmax.x = -maxbox.x - v;
//			}
//			v = vert.y;
//			if (normal.y > 0.0) {
//				vmin.y = -maxbox.y - v;
//				vmax.y = maxbox.y - v;
//			} else {
//				vmin.y = maxbox.y - v;
//				vmax.y = -maxbox.y - v;
//			}
//			v = vert.z;
//			if (normal.z > 0.0) {
//				vmin.z = -maxbox.z - v;
//				vmax.z = maxbox.z - v;
//			} else {
//				vmin.z = maxbox.z - v;
//				vmax.z = -maxbox.z - v;
//			}
//			if (VecMath.dotproduct(normal, vmin) > 0.0)
//				return false;
//			if (VecMath.dotproduct(normal, vmax) >= 0.0)
//				return true;
//			return false;
//		}
//	}
//
//	private class ICHull {
//
//	}
}