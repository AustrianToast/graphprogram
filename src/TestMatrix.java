public class TestMatrix {
    public static void main(String[] args) {
        Matrix matrix = new Matrix("/home/old/projects/Java/graphprogram/24n_01.csv");
        Matrix scalarProduct;

        System.out.println(matrix.getRowLength());
        System.out.println(matrix.getColumnLength());

        System.out.println(matrix);
        
        scalarProduct = matrix.multiply(matrix);
        System.out.println(scalarProduct);
    }
}
