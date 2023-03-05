public class TestMatrix {
    public static void main(String[] args) {
        testImportCSV();
    }

    public static void testImportCSV() {
        Matrix m = new Matrix();

        m.importCSV("/home/satan/bin/graphprogram/graph.csv");
        m.printCSV();;
    }
}
