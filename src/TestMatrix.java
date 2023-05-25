public class TestMatrix {
    public static void main(String[] args) {
        String pathToProgramRoot = "/home/rene/projects/Java/graphprogram";

        Matrix matrix = new Matrix(pathToProgramRoot + "/csv/graph.csv");
        Matrix scalarProduct;

        System.out.println("RowLength: " + matrix.getRowLength());
        System.out.println("ColumnLength: " +matrix.getColumnLength());

        System.out.println("\nMatrix A:  \n" + matrix);
        
        scalarProduct = matrix.multiply(matrix);
        System.out.println("\nScalarProduct A²:  \n" + scalarProduct);

        Matrix bruh = new Matrix(100, 100);
        System.out.println(bruh);
    }
}
