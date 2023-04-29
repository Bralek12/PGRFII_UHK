package raster;

import java.util.Arrays;

public class ZBuffer implements Raster<Double> {
    private final int width;
    private final int height;
    private final double[] zBuff;

    public ZBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.zBuff = new double[width * height];
        clear();
    }

    public void clear() {
        Arrays.fill(zBuff, 1.0);
    }

    @Override
    public Double get(int x, int y) {
        int index = y * width + x;
        return zBuff[index];
    }

    @Override
    public void set(int x, int y, Double value) {
        int index = y * width + x;
        zBuff[index] = value;
    }

    @Override
    public int getW() {
        return width;
    }

    @Override
    public int getH() {
        return height;
    }
}
