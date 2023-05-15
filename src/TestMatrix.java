public class TestMatrix {
    public static void main(String[] args) {
        Matrix matrix = new Matrix("/home/old/projects/Java/graphprogram/24n_01.csv");
        Matrix scalarProduct;

        System.out.println("RowLength: " + matrix.getRowLength());
        System.out.println("ColumnLength: " +matrix.getColumnLength());

        System.out.println("\nMatrix A:  \n" + matrix);
        
        scalarProduct = matrix.multiply(matrix);
        System.out.println("\nScalarProduct A²:  \n" + scalarProduct);
    }
}
