package solids;

import java.util.ArrayList;
import java.util.List;

import model.Part;
import model.Vertex;
import transforms.Mat4;
import transforms.Mat4Identity;

public abstract class Solid {
	List<Vertex> vertexBuffer = new ArrayList<>();
	List<Integer> indexBuffer = new ArrayList<>();
	List<Part> parts = new ArrayList<>();

	public List<Vertex> getVertexBuffer() {
		return vertexBuffer;
	}

	public List<Integer> getIndexBuffer() {
		return indexBuffer;
	}

	public List<Part> getParts() {
		return parts;
	}
	
}
