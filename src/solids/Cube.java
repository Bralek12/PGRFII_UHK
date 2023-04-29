package solids;

import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class Cube extends Solid {
    public Cube() {
        // Define colors for each face
        Col frontColor = new Col(0, 0, 255);
        Col backColor = new Col(0, 0, 200);
        Col leftColor = new Col(0, 0, 230);
        Col rightColor = new Col(0, 0, 180);
        Col topColor = new Col(0, 0, 210);
        Col bottomColor = new Col(0, 0, 150);

        // Define vertices
        getVertexBuffer().add(new Vertex(new Point3D(1, 1, 1), frontColor));
        getVertexBuffer().add(new Vertex(new Point3D(3, 1, 1), frontColor));
        getVertexBuffer().add(new Vertex(new Point3D(3, 3, 1), frontColor));
        getVertexBuffer().add(new Vertex(new Point3D(1, 3, 1), frontColor));

        getVertexBuffer().add(new Vertex(new Point3D(1, 1, 3), backColor));
        getVertexBuffer().add(new Vertex(new Point3D(3, 1, 3), backColor));
        getVertexBuffer().add(new Vertex(new Point3D(3, 3, 3), backColor));
        getVertexBuffer().add(new Vertex(new Point3D(1, 3, 3), backColor));

        // Define faces
        int[][] faces = {
            { 0, 1, 2, 3 }, // Front
            { 1, 5, 6, 2 }, // Right
            { 5, 4, 7, 6 }, // Back
            { 4, 0, 3, 7 }, // Left
            { 3, 2, 6, 7 }, // Top
            { 0, 1, 5, 4 }  // Bottom
    };

    Col[] faceColors = {frontColor, rightColor, backColor, leftColor, topColor, bottomColor};

    // Add the indices for the faces
    for (int i = 0; i < faces.length; i++) {
        int[] face = faces[i];
        Col faceColor = faceColors[i];

        getVertexBuffer().get(face[0]).setColor(faceColor);
        getVertexBuffer().get(face[1]).setColor(faceColor);
        getVertexBuffer().get(face[2]).setColor(faceColor);
        getVertexBuffer().get(face[3]).setColor(faceColor);

        getIndexBuffer().add(face[0]);
        getIndexBuffer().add(face[1]);
        getIndexBuffer().add(face[2]);

        getIndexBuffer().add(face[0]);
        getIndexBuffer().add(face[2]);
        getIndexBuffer().add(face[3]);
    }

    getParts().add(new model.Part(model.TypeTopology.TRIANGLES, 12, 0));
}
}
