package model;

import transforms.Col;
import transforms.Point3D;

public class Vertex {

    private final Point3D point; // pozice vrcholu
    private Col color; // barva vrcholu

    // Konstruktor pro vytvoření nového vrcholu s danou pozicí a barvou
    public Vertex(Point3D position, Col color) {
        this.point = position;
        this.color = color;
    }

    // Konstruktor pro vytvoření nového vrcholu s danou pozicí a náhodnou barvou
    public Vertex(Point3D position) {
        this(position, new Col(Math.random(), Math.random(), Math.random()));
    }

    // Metoda pro vrácení nového vrcholu s danou pozicí
    public Vertex withPoint(Point3D point) {
        return new Vertex(point, color);
    }

    // Metoda pro vynásobení vrcholu skalárem
    public Vertex mul(double d) {
        return new Vertex(point.mul(d), color);
    }

    // Metoda pro sčítání dvou vrcholů
    public Vertex add(Vertex d) {
        return new Vertex(point.add(d.getPoint()), color);
    }

    // Metoda pro dehomogenizaci vrcholu
    public Vertex dehomog() {
        return this.mul(1 / this.getPoint().getW());
    }

    // Metoda pro vrácení barvy vrcholu
    public Col getColor() {
        return color;
    }

    // Metoda pro nastavení barvy vrcholu
    public void setColor(Col color) {
        this.color = color;
    }

    // Metoda pro vrácení pozice vrcholu
    public Point3D getPoint() {
        return point;
    }

}
