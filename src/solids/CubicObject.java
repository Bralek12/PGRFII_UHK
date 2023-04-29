package solids;

import model.Part;
import model.TypeTopology;
import model.Vertex;
import transforms.Bicubic;
import transforms.Cubic;
import transforms.Point3D;

public class CubicObject extends Solid {
    Bicubic bc;

    public CubicObject() {
        Point3D[] rb = new Point3D[20];
        rb[0] = new Point3D(4, 4, 0);
        rb[1] = new Point3D(2, 2, -2);
        rb[2] = new Point3D(8, 2, -2);
        rb[3] = new Point3D(6, 4, 0);
        
        rb[4] = new Point3D(4, 4, -2);
        rb[5] = new Point3D(2, 2, -4);
        rb[6] = new Point3D(8, 2, -4);
        rb[7] = new Point3D(6, 4, -2);
        
        rb[8] = new Point3D(4, 6, -2);
        rb[9] = new Point3D(2, 8, -4);
        rb[10] = new Point3D(8, 8, -4);
        rb[11] = new Point3D(6, 6, -2);
        
        rb[12] = new Point3D(4, 6, 0);
        rb[13] = new Point3D(2, 8, -2);
        rb[14] = new Point3D(8, 8, -2);
        rb[15] = new Point3D(6, 6, 0);

        bc = new Bicubic(Cubic.BEZIER, rb);

        // create vertices
        int index = 0;
        for (float u = 0; u < 1; u += 0.05f) {
            for (float v = 0; v < 1; v += 0.05f) {
                Vertex vertex1 = new Vertex(bc.compute(u, v));
                Vertex vertex2 = new Vertex(bc.compute(u, v + 0.05f));
                Vertex vertex3 = new Vertex(bc.compute(u + 0.05f, v));
                Vertex vertex4 = new Vertex(bc.compute(u + 0.05f, v + 0.05f));

                // add the vertices to the vertex buffer
                getVertexBuffer().add(vertex1);
                getVertexBuffer().add(vertex2);
                getVertexBuffer().add(vertex3);
                getVertexBuffer().add(vertex4);

                // add the indices of the first triangle vertices to the index buffer
                getIndexBuffer().add(index); // triangle 1
                getIndexBuffer().add(index + 1);
                getIndexBuffer().add(index + 2);

                // add the indices of the second triangle vertices to the index buffer
                getIndexBuffer().add(index + 2); // triangle 2
                getIndexBuffer().add(index + 1);
                getIndexBuffer().add(index + 3);

                index += 4; // move to the next set of four vertices
            }
        }

        // add the parts to the solid
        getParts().add(new Part(TypeTopology.TRIANGLES, 800, 0));
    }
}