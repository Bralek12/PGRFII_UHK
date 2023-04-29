package solids;

import model.Part;
import model.TypeTopology;
import model.Vertex;
import transforms.Bicubic;
import transforms.Cubic;
import transforms.Point3D;

public class CubicObjectPoints extends Solid {
    Bicubic bc;

    public CubicObjectPoints() {
        Point3D[] rb = new Point3D[20];
        rb[0] = new Point3D(-3.6, 1.2, 0.3);
        rb[1] = new Point3D(-1.5, -2.1, -0.9);
        rb[2] = new Point3D(2.7, -2.4, -1.2);
        rb[3] = new Point3D(0.9, 0.6, 0.6);

        rb[4] = new Point3D(-3.0, 0.3, -1.2);
        rb[5] = new Point3D(-2.1, -1.8, -1.8);
        rb[6] = new Point3D(2.4, -1.8, -2.1);
        rb[7] = new Point3D(0.6, 0.3, -1.5);

        rb[8] = new Point3D(-2.7, 0.9, -1.5);
        rb[9] = new Point3D(-0.9, 1.5, -2.4);
        rb[10] = new Point3D(2.1, 1.8, -2.7);
        rb[11] = new Point3D(0.3, 1.2, -1.8);

        rb[12] = new Point3D(-3.3, 0.6, 0.6);
        rb[13] = new Point3D(-1.2, 1.2, -0.9);
        rb[14] = new Point3D(3.0, 1.5, -0.6);
        rb[15] = new Point3D(1.2, 0.9, 0.9);

        bc = new Bicubic(Cubic.BEZIER, rb);

        // create vertices
        for (float u = 0; u < 1; u += 0.05f) {
            for (float v = 0; v < 1; v += 0.05f) {
                Vertex vertex = new Vertex(bc.compute(u, v));
                getVertexBuffer().add(vertex);
            }
        }

        // create indices
        for (int i = 0; i < getVertexBuffer().size(); i++) {
            getIndexBuffer().add(i);
        }

        // add the parts to the solid
        getParts().add(new Part(TypeTopology.POINTS, getVertexBuffer().size(), 0));

    }
}