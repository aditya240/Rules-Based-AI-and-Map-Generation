package s3.ai;

import java.util.List;

public class Node {

    private Node parent;
    private double x,y;
    private double g,h;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Node getParent() {
        return this.parent;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getG() {
        return this.g;
    }
    public double getH() {
        return this.h;
    }
    public double getF() {
        double h = this.getH();
        double g = this.getG();
        double f = h + g;
        return  f;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setG(double g) {
        this.g = g;
    }
    public void setH(double h) {
        this.h = h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Node))
            return false;
        Node that = (Node) obj;
        return (this.getX() == that.getX()) && (this.getY() == that.getY());
    }

    public static Node removeLowestF(List<Node> open) {
        Node removed = open.get(0);
        double lowestF = removed.getF();
        for (Node node : open) {
            double f = node.getF();
            if (f < lowestF) {
                removed = node;
                lowestF = f;
            }
        }
        open.remove(removed);
        return removed;
    }

}
