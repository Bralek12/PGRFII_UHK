package model;

public class Part {
	private TypeTopology type;
	private int start;
	private int count;

	public Part(TypeTopology type, int count, int start) {
        this.type = type;
        this.count = count;
        this.start = start;
    }
	
	public TypeTopology getType() {
		return type;
	}

	public int getStart() {
		return start;
	}

	public int getCount() {
		return count;
	}
	
}
