package solids;

import model.Part;
import model.TypeTopology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Axis extends Solid {

    public Axis(Point3D a, Point3D b, Point3D c, Col color) {
        getVertexBuffer().add(new Vertex(new Point3D(0, 0, 0), color));
        getVertexBuffer().add(new Vertex(a, color));
        getVertexBuffer().add(new Vertex(b, color));
        getVertexBuffer().add(new Vertex(c, color)); // spicka

        getIndexBuffer().add(3);
        getIndexBuffer().add(1);
        getIndexBuffer().add(2);

        getIndexBuffer().add(3);
        getIndexBuffer().add(0);

        getParts().add(new Part(TypeTopology.LINES, 1, 3));
    }
}
