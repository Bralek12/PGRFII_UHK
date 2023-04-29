package render;

import model.Vertex;
import raster.CropView;
import transforms.Col;

public class RasterizerTriangle {
	private int width;
	private int height;
	private CropView testVisibility;

	public RasterizerTriangle(CropView testVisibility) {
		this.height = testVisibility.getHeight();
		this.width = testVisibility.getWidth();
		this.testVisibility = testVisibility;
	}

	public void rasterize(Vertex a, Vertex b, Vertex c, Col color) {

		a = a.dehomog();
		b = b.dehomog();
		c = c.dehomog();

		double xA = (width - 1) * (a.getPoint().getX() + 1) / 2;
		double yA = (height - 1) * (-a.getPoint().getY() + 1) / 2;
		double zA = a.getPoint().getZ();

		double xB = (width - 1) * (b.getPoint().getX() + 1) / 2;
		double yB = (height - 1) * (-b.getPoint().getY() + 1) / 2;
		double zB = b.getPoint().getZ();

		double xC = (width - 1) * (c.getPoint().getX() + 1) / 2;
		double yC = (height - 1) * (-c.getPoint().getY() + 1) / 2;
		double zC = c.getPoint().getZ();

		if (yA > yB) {
			double temp = yA;
			yA = yB;
			yB = temp;
			temp = xA;
			xA = xB;
			xB = temp;
			temp = zA;
			zA = zB;
			zB = temp;
		}
		if (yA > yC) {
			double temp = yA;
			yA = yC;
			yC = temp;
			temp = xA;
			xA = xC;
			xC = temp;
			temp = zA;
			zA = zC;
			zC = temp;
		}
		if (yB > yC) {
			double temp = yB;
			yB = yC;
			yC = temp;
			temp = xB;
			xB = xC;
			xC = temp;
			temp = zB;
			zB = zC;
			zC = temp;
		}

	
		for (int y = Math.max((int) yA + 1, 0); y <= Math.min(yB, height - 1); y++) {
			double s1 = (y - yA) / (yB - yA);
			double x1 = (1 - s1) * xA + xB * s1;
			double z1 = (1 - s1) * zA + zB * s1;

			double s2 = (y - yA) / (yC - yA);
			double x2 = (1 - s2) * xA + xC * s2;
			double z2 = (1 - s2) * zA + zC * s2;

			if (x1 > x2) {
				double help = x1;
				x1 = x2;
				x2 = help;
				help = z1;
				z1 = z2;
				z2 = help;
			}
			for (int x = Math.max((int) x1 + 1, 0); x <= Math.min(x2, width - 1); x++) {
				double t = (x - x1) / (x2 - x1);
				double z = z1 * (1 - t) + (z2 * t);
				testVisibility.render(x, y, z, color);

			}

		}

		for (int y = Math.max((int) yB + 1, 0); y < Math.min(yC, height - 1); y++) {
			double s1 = (y - yB) / (yC - yB);
			double x1 = (1 - s1) * xB + xC * s1;
			double z1 = (1 - s1) * zB + zC * s1;

			double s2 = (y - yA) / (yC - yA);
			double x2 = (1 - s2) * xA + xC * s2;
			double z2 = (1 - s2) * zA + zC * s2;

			if (x1 > x2) {
				double help = x1;
				x1 = x2;
				x2 = help;
				help = z1;
				z1 = z2;
				z2 = help;
			}
			for (int x = Math.max((int) x1 + 1, 0); x < Math.min(x2, width - 1); x++) {
				double t = (x - x1) / (x2 - x1);
				double z = z1 * (1 - t) + (z2 * t);
				testVisibility.render(x, y, z, color);

			}
		}
	}
}
