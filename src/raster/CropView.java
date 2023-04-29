package raster;

import java.awt.image.BufferedImage;
import transforms.Col;

public class CropView {
    private final ImageBuffer imgBuffer;
    private final ZBuffer zBuffer;
    private final int width;
    private final int height;

    public CropView(int width, int height) {
        this.width = width;
        this.height = height;
        zBuffer = new ZBuffer(width, height);
        imgBuffer = new ImageBuffer(width, height);
    }

    public void render(int x, int y, double z, Col color) {
        if (z < zBuffer.get(x, y) && z >= 0) {
            imgBuffer.set(x, y, color.getRGB());
            zBuffer.set(x, y, z);
        }
    }

    public void init(int color) {
        zBuffer.clear();
        imgBuffer.clear(color);
    }

    public void clear(int color) {
        imgBuffer.clear(color);
        zBuffer.clear();
    }

    public BufferedImage getBufferedImage() {
        return imgBuffer.getBuff();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
