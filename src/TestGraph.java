public class TestGraph {
    public static void main(String[] args) {
        test1("/home/old/projects/Java/graphprogram/24n_01.csv");
    }

    public static void test1(String file) {
        Graph g = new Graph(file);
        g.calculateExzentrizitäten();
        g.calculateProperties();
        System.out.println(g);
    }
}