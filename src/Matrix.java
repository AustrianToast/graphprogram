import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class Matrix {
    private int[][] matrix;

    public static void main(String[] args) {}

    public Matrix(int rowLength, int columnLength) {
        matrix = new int[rowLength][columnLength];
    }

    public Matrix(String file) {
        matrix = this.readCSV(file);
    }

    public Matrix multiply( Matrix m) {
        Matrix scalarProduct = null;
        
        if(this.columnLength() != m.rowLength()) {
            return scalarProduct;
        }

        scalarProduct = new Matrix(this.rowLength(), m.columnLength());

        for(int columnIndex = 0; columnIndex < this.columnLength(); columnIndex++) {
            for(int rowIndex = 0; rowIndex < m.rowLength(); rowIndex++) {
                int sum = 0;
                for(int k=0; k < this.rowLength(); k++) {
                    sum += this.getValue(columnIndex, k) * m.getValue(k, rowIndex);
                }
                scalarProduct.insert(columnIndex, rowIndex, sum);
            }
        }

        return scalarProduct;
    }
    
    public int rowLength() {
        return matrix.length;
    }

    public int columnLength() {
        return matrix[0].length;
    }

    public int getValue(int columnIndex, int rowIndex) {
        return matrix[columnIndex][rowIndex];
    }

    public void insert(int columnIndex, int rowIndex, int value) {
        matrix[rowIndex][columnIndex] = value;
    }

    public void print() {
        for(int columnIndex=0; columnIndex < matrix.length; columnIndex++) {
            for(int rowIndex=0; rowIndex < matrix[columnIndex].length; rowIndex++) {
                System.out.print(matrix[columnIndex][rowIndex]);
            }
            System.out.println();
        }
    }

    public int[][] readCSV(String file){
        int[][] intMatrix = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int rowCount = line.trim().split(";").length;
            int columnCount = rowCount;
            String[] lineArray = null;
            
            intMatrix = new int[rowCount][columnCount];

            for(int columnIndex = 0; line != null && columnIndex < intMatrix.length; columnIndex++, line = br.readLine()) {
                lineArray = line.trim().split(";");
                for(int rowIndex=0; rowIndex < intMatrix[columnIndex].length; rowIndex++) {
                    intMatrix[columnIndex][rowIndex] = Integer.parseInt(lineArray[rowIndex]);
                }
            }
            br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intMatrix;
    }

}