package render;

import model.Vertex;
import raster.CropView;
import transforms.Col;

public class RasterizerPoint {

	private int width;
	private int height;
	private CropView visibility;

	public RasterizerPoint(CropView visibility) {
		this.height = visibility.getHeight();
		this.width = visibility.getWidth();
		this.visibility = visibility;
	}

	public void rasterize(Vertex a) {
		a = a.dehomog();
		// Transformace bodu do obrazovky
		double xA = (width - 1) * (a.getPoint().getX() + 1) / 2;
		double yA = (height - 1) * (-a.getPoint().getY() + 1) / 2;
		double zA = a.getPoint().getZ();

		// Vykreslení bodu
		visibility.render((int) xA + 1, (int) yA + 1, zA, new Col(0x00ff00));
	}
}
