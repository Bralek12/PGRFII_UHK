package raster;

public interface Raster<T> {
	T get(int x, int y);

	void set(int x, int y, T value);

	int getW();

	int getH();
}
