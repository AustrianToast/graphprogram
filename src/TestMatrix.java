public class TestMatrix {
    public static void main(String[] args) {
        test1("/home/old/projects/Java/graphprogram/24n_01.csv");
    }

    public static void test1(String file) {
        Matrix matrix = new Matrix(file);
        Matrix scalarProduct;

        System.out.println(matrix.getRowLength());
        System.out.println(matrix.getColumnLength());

        System.out.println(matrix);
        
        scalarProduct = matrix.multiply(matrix);
        System.out.println(scalarProduct);
    }
}
