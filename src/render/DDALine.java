package render;

import model.Vertex;
import raster.CropView;
import transforms.Col;

public class DDALine {
	private int width, height;
	private CropView testVisibility;

	public DDALine(CropView vis) {
		this.height = vis.getHeight();
		this.width = vis.getWidth();
		this.testVisibility = vis;
	}

	public void rasterize(Vertex a, Vertex b, Col color) {
		a = a.dehomog();
		b = b.dehomog();

		double xA = (width - 1) * (a.getPoint().getX() + 1) / 2;
		double yA = (height - 1) * (-a.getPoint().getY() + 1) / 2;
		double zA = a.getPoint().getZ();

		double xB = (width - 1) * (b.getPoint().getX() + 1) / 2;
		double yB = (height - 1) * (-b.getPoint().getY() + 1) / 2;
		double zB = b.getPoint().getZ();

		double dx = xB - xA;
		double dy = yB - yA;
		double x, y, k, h, g;

		k = dy / dx;

		// 1. kvadrant
		if (yA >= yB && xA <= xB) {
			g = Math.abs(k) <= 1 ? 1 : -1 / k;
			h = Math.abs(k) <= 1 ? k : -1;
		}

		// 2. kvadrant
		else if (yA > yB && xA > xB) {
			g = Math.abs(k) <= 1 ? -1 : -1 / k;
			h = Math.abs(k) <= 1 ? -k : -1;
		}

		// 3. kvadrant
		else if (yA <= yB && xA >= xB) {
			g = Math.abs(k) <= 1 ? -1 : 1 / k;
			h = Math.abs(k) <= 1 ? -k : 1;
		}

		// 4. kvadrant
		else {
			g = Math.abs(k) <= 1 ? 1 : 1 / k;
			h = Math.abs(k) <= 1 ? k : 1;
		}

		x = xA;
		y = yA;

		for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) {
			if ((x > 0 && x < testVisibility.getWidth() - 1) && y > 0 && y < testVisibility.getHeight() - 1) {
				double t, z;
				if (xA == xB) {
					t = ((double) y - yA) / (yB - yA);
				} else {
					t = ((double) x - xA) / (xB - xA);
				}
				z = zA * (1 - t) + zB * t;

				testVisibility.render((int) Math.round(x), (int) Math.round(y), z, color);
			}
			x += g;
			y += h;
		}
	}
}
