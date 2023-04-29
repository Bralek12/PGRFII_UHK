package render;

import solids.Axis;
import solids.Solid;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import model.Part;
import model.TypeTopology;
import model.Vertex;
import raster.CropView;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Mat4PerspRH;
import transforms.Point3D;

public class Renderer {
	private Mat4 model = new Mat4Identity();
	private Mat4 view;
	private Mat4 projection;
	private RasterizerTriangle rasterizerTriangle;
	private DDALine rasterizerLine;
	private RasterizerPoint rasterizerPoint;
	private boolean wireframe = false;

	/**
	 * 
	 * @param testVisibility
	 */
	public Renderer(CropView testVisibility) {
		rasterizerTriangle = new RasterizerTriangle(testVisibility);
		rasterizerLine = new DDALine(testVisibility);
		rasterizerPoint = new RasterizerPoint(testVisibility);
		projection = new Mat4PerspRH(Math.PI / 4, testVisibility.getWidth() / testVisibility.getHeight(), 1, 200);
	}

	public void render(List<Solid> solids) {
		for (Solid solid : solids) {
			for (Part parts : solid.getParts()) {
				if (parts.getType() == TypeTopology.TRIANGLES) {
					Vertex a, b, c;
					Col color;
					for (int i = 0; i < parts.getCount(); i++) {

						a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + parts.getStart()));
						b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + parts.getStart() + 1));
						c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(3 * i + parts.getStart() + 2));

						double rA = a.getColor().getR();
						double rB = b.getColor().getR();
						double rC = c.getColor().getR();

						double gA = a.getColor().getG();
						double gB = b.getColor().getG();
						double gC = c.getColor().getG();

						double bA = a.getColor().getB();
						double bB = b.getColor().getB();
						double bC = c.getColor().getB();

						color = new Col((rA + rB + rC) / 3, (gA + gB + gC) / 3, (bA + bB + bC) / 3);

						if (solid instanceof Axis) {
							Point3D aP = a.getPoint();
							aP = aP.mul(view).mul(projection);
							a = a.withPoint(aP);

							Point3D bP = b.getPoint();
							bP = bP.mul(view).mul(projection);
							b = b.withPoint(bP);

							Point3D cP = c.getPoint();
							cP = cP.mul(view).mul(projection);
							c = c.withPoint(cP);
						} else {
							Point3D aP = a.getPoint();
							aP = aP.mul(model).mul(view).mul(projection);
							a = a.withPoint(aP);

							Point3D bP = b.getPoint();
							bP = bP.mul(model).mul(view).mul(projection);
							b = b.withPoint(bP);

							Point3D cP = c.getPoint();
							cP = cP.mul(model).mul(view).mul(projection);
							c = c.withPoint(cP);
						}

						if (wireframe) {
							renderLine(a, b, color);
							renderLine(c, b, color);
							renderLine(c, a, color);
						} else {
							renderTriangle(a, b, c, color);
						}

					}
				} else if (parts.getType() == TypeTopology.LINES) {
					Vertex a, b;
					Col color;
					for (int i = 0; i < parts.getCount(); i++) {
						a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(parts.getStart() + i * 2));
						b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(parts.getStart() + i * 2 + 1));

						double rA = a.getColor().getR();
						double rB = b.getColor().getR();
						double gA = a.getColor().getG();
						double gB = b.getColor().getG();
						double bA = a.getColor().getB();
						double bB = b.getColor().getB();

						color = new Col((rA + rB) / 2, (gA + gB) / 2, (bA + bB) / 2);

						if (solid instanceof Axis) {
							Point3D aP = a.getPoint();
							aP = aP.mul(view).mul(projection);
							a = a.withPoint(aP);

							Point3D bP = b.getPoint();
							bP = bP.mul(view).mul(projection);
							b = b.withPoint(bP);
						} else {
							Point3D aP = a.getPoint();
							aP = aP.mul(model).mul(view).mul(projection);
							a = a.withPoint(aP);

							Point3D bP = b.getPoint();
							bP = bP.mul(model).mul(view).mul(projection);
							b = b.withPoint(bP);

						}

						renderLine(a, b, color);
					}
				} else if (parts.getType() == TypeTopology.POINTS) {
					Vertex a;
					for (int i = 0; i < parts.getCount(); i++) {

						a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(i + parts.getStart()));
						a = a.withPoint(a.getPoint().mul(model).mul(view).mul(projection));

						renderPoint(a);
					}
				}
			}
		}
	}

	private void renderTriangle(Vertex a, Vertex b, Vertex c, Col color) {
		Point3D aPoint = a.getPoint();
		Point3D bPoint = b.getPoint();
		Point3D cPoint = c.getPoint();

		double aX = aPoint.getX(), aY = aPoint.getY(), aZ = aPoint.getZ(), aW = aPoint.getW();
		double bX = bPoint.getX(), bY = bPoint.getY(), bZ = bPoint.getZ(), bW = bPoint.getW();
		double cX = cPoint.getX(), cY = cPoint.getY(), cZ = cPoint.getZ(), cW = cPoint.getW();

		if ((aX > aW && bX > bW && cX > cW) || (aX < -aW && bX < -bW && cX < -cW)
				|| (aY > aW && bY > bW && cY > cW) || (aY < -aW && bY < -bW && cY < -cW)
				|| (aZ > aW && bZ > bW && cZ > cW) || (aZ < 0 && bZ < 0 && cZ < 0)) {
			return;
		}

		// Sort vertices by their Z values in descending order
		Vertex[] vertices = new Vertex[] { a, b, c };
		Arrays.sort(vertices, Comparator.comparingDouble(v -> -v.getPoint().getZ()));
		a = vertices[0];
		b = vertices[1];
		c = vertices[2];

		if (aZ < 0) {
			return;
		}

		if (bZ < 0) {
			double t1 = aZ / (aZ - bZ);
			double t2 = aZ / (aZ - cZ);
			Vertex ab = a.mul(1 - t1).add(b.mul(t1));
			Vertex ac = a.mul(1 - t2).add(c.mul(t2));
			rasterizerTriangle.rasterize(a, ab, ac, color);
		} else if (cZ < 0) {
			double t1 = bZ / (bZ - cZ);
			double t2 = aZ / (aZ - cZ);
			Vertex bc = b.mul(1 - t1).add(c.mul(t1));
			Vertex ac = a.mul(1 - t2).add(c.mul(t2));
			rasterizerTriangle.rasterize(a, b, ac, color);
			rasterizerTriangle.rasterize(bc, b, ac, color);
		} else {
			rasterizerTriangle.rasterize(a, b, c, color);
		}
	}

	private void renderLine(Vertex a, Vertex b, Col color) {
		Point3D aPoint = a.getPoint();
		Point3D bPoint = b.getPoint();

		if ((aPoint.getX() > aPoint.getW() && bPoint.getX() > bPoint.getW()) ||
				(aPoint.getX() < -aPoint.getW() && bPoint.getX() < -bPoint.getW()) ||
				(aPoint.getY() > aPoint.getW() && bPoint.getY() > bPoint.getW()) ||
				(aPoint.getY() < -aPoint.getW() && bPoint.getY() < -bPoint.getW()) ||
				(aPoint.getZ() > aPoint.getW() && bPoint.getZ() > bPoint.getW()) ||
				(aPoint.getZ() < 0 && bPoint.getZ() < 0)) {
			return;
		}

		if (aPoint.getZ() < bPoint.getZ()) {
			Vertex temp = a;
			a = b;
			b = temp;
			aPoint = a.getPoint();
			bPoint = b.getPoint();
		}

		if (aPoint.getZ() < 0) {
			return;
		}

		if (bPoint.getZ() < 0) {
			double t = aPoint.getZ() / (aPoint.getZ() - bPoint.getZ());
			Vertex ab = a.mul(1 - t).add(b.mul(t));
			rasterizerLine.rasterize(a, ab, color);
		} else {
			rasterizerLine.rasterize(a, b, color);
		}
	}

	private void renderPoint(Vertex a) {
		Point3D aPoint = a.getPoint();
		double x = aPoint.getX();
		double y = aPoint.getY();
		double z = aPoint.getZ();
		double w = aPoint.getW();

		if (x <= w && x >= -w && y <= w && y >= -w && z <= w && z >= 0) {
			rasterizerPoint.rasterize(a);
		}
	}

	public Mat4 getModel() {
		return model;
	}

	public void setModel(Mat4 model) {
		this.model = model;
	}

	public Mat4 getView() {
		return view;
	}

	public void setView(Mat4 view) {
		this.view = view;
	}

	public Mat4 getProjection() {
		return projection;
	}

	public void setProjection(Mat4 projection) {
		this.projection = projection;
	}

	public boolean isWireframe() {
		return wireframe;
	}

	public void setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
	}

}
