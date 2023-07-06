package s3.ai;

import java.util.Random;

public class Edge {
    private int U, V, weight, limit = 4;

    public Edge(int u, int  v){
        this.U = u;
        this.V = v;
        this.weight = new Random().nextInt(limit);
    }

    public int getU() {
        return this.U;
    }

    public int getV() {
        return this.V;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setU(int u) {
        this.U = u;
    }
}

