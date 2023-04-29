package solids;

import model.TypeTopology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Pyramid extends Solid {
    public Pyramid() {
        Col[] colors = {
                new Col(1, 0, 0), // red
                new Col(1, 0.5, 0.5), // pink
                new Col(0.7, 0, 0), // dark red
                new Col(0.7, 0.25, 0.25), // dark pink
        };

        Point3D[] points = {
                new Point3D(0, 0, 2),
                new Point3D(-2, 0, 2),
                new Point3D(4, 2, 0),
                new Point3D(1, 1, 4),
        };

        for (int i = 0; i < points.length; i++) {
            getVertexBuffer().add(new Vertex(points[i], colors[i % colors.length]));
        }

        int[][] indices = {
                {0, 1, 2},
                {0, 1, 3},
                {0, 2, 3},
                {1, 2, 3},
        };

        for (int[] face : indices) {
            for (int index : face) {
                getIndexBuffer().add(index);
            }
        }

        getParts().add(new model.Part(TypeTopology.TRIANGLES, 4, 0));
    }
}
