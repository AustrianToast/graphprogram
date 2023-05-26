public class TestGraph {
    public static void main(String[] args) {
        // test1();
        // test2();
        // test3();
        // test4();
        test5();
    }

    static String pathToProgramRoot = "/home/rene/projects/Java/graphprogram";


    /*
     * This is a small not connected graph with articulations and bridges
     */
    public static void test1() {
        Graph g = new Graph(pathToProgramRoot + "/csv/5n.csv");
        System.out.println(g);
    }


    /*
     * Bigger graph with bridges and many articulations 
     */
    public static void test2() {
        Graph g = new Graph(pathToProgramRoot + "/csv/24n.csv");
        System.out.println(g);
    }

    /*
     * Graph where each vetex is not connected to anything
     */
    public static void test3() {
        Graph g = new Graph(pathToProgramRoot + "/csv/empty.csv");
        System.out.println(g);
    }

    /*
     * small connected graph with one articulation and one bridge 
     */
    public static void test4() {
        Graph g = new Graph(pathToProgramRoot + "/csv/art-brck.csv");
        System.out.println(g);
    }

    /*
     * randomly generated graph with x nodes
     */
    public static void test5() {
        Graph g = new Graph(pathToProgramRoot + "/csv/50n.csv");
        System.out.println(g);
    }
}