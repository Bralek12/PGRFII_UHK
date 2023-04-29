package raster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageBuffer implements Raster<Integer>{

	private BufferedImage imageBuffer;
	
	public ImageBuffer(int width, int height) {
		imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	@Override
	public Integer get(int x, int y) {
		return imageBuffer.getRGB(x, y);
	}

	@Override
	public void set(int x, int y, Integer value) {
		imageBuffer.setRGB(x, y, value.intValue());
	}

	@Override
	public int getW() {
		return imageBuffer.getWidth();
	}

	@Override
	public int getH() {
		return imageBuffer.getHeight();
	}
	
	public void clear(int color) {
		Graphics gr = imageBuffer.getGraphics();
		gr.setColor(new Color(color));
		gr.fillRect(0, 0, imageBuffer.getWidth(), imageBuffer.getHeight());
	}
	
	public BufferedImage getBuff() {
		return imageBuffer;
	}
}
