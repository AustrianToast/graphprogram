public class TestMatrix {
    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        String pathToProgramRoot = "";

        Matrix matrix = new Matrix(pathToProgramRoot + "/csv/graph.csv");
        Matrix scalarProduct;

        System.out.println("RowLength: " + matrix.getRowLength());
        System.out.println("ColumnLength: " +matrix.getColumnLength());

        System.out.println("\nMatrix A:  \n" + matrix);
        
        scalarProduct = matrix.multiply(matrix);
        System.out.println("\nScalarProduct A²:  \n" + scalarProduct);
    }

    public static void test2() {
        Matrix bruh = new Matrix(10, 10, true);
        System.out.println(bruh);
    } 
}
