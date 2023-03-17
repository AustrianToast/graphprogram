public class TestMatrix {
    public static void main(String[] args) {
        test1("");
    }

    public static void test1(String file) {
        Matrix matrix = new Matrix(file);
        Matrix scalarProduct;

        matrix.print();
        System.out.println();
        
        scalarProduct = matrix.multiply( matrix);
        scalarProduct.print();
        
    }
}
