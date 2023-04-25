public class TestGraph {
    public static void main(String[] args) {
        Graph g = new Graph("/home/old/projects/Java/graphprogram/graph.csv");
        g.calculateExzentrizitäten();
        g.calculateProperties();
        System.out.println(g);
    }
}