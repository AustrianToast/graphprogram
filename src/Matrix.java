import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class Matrix {
    private int[][] matrix;
    private int rowLength;
    private int columnLength;

    public static void main(String[] args) {}

    public Matrix(String file) {
        readCSV(file);
    }

    public Matrix(int rowLength, int columnLength) {
        matrix = new int[rowLength][columnLength];
        this.rowLength = rowLength;
        this.columnLength = columnLength;
    }

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
        rowLength = matrix.length;
        columnLength = matrix[0].length;
    }

    public Matrix multiply(Matrix m) {
        Matrix scalarProduct = new Matrix(new int[rowLength][m.columnLength]);

        for(int columnIndex = 0; columnIndex < this.getColumnLength(); columnIndex++) {
            for(int rowIndex = 0; rowIndex < m.getRowLength(); rowIndex++) {
                int sum = 0;
                for(int k=0; k < this.getRowLength(); k++) {
                    sum += this.getValueAt(rowIndex, k) * m.getValueAt(k, columnIndex);
                }
                scalarProduct.insert(rowIndex, columnIndex, sum);
            }
        }
        return scalarProduct;
    }
    
    public int getRowLength() {
        return rowLength;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public int getValueAt(int rowIndex, int columnIndex) {
        return matrix[rowIndex][columnIndex];
    }

    public void insert(int rowIndex, int columnIndex, int value) {
        matrix[rowIndex][columnIndex] = value;
    }

    public void readCSV(String file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            rowLength = line.trim().split(";").length;
            columnLength = rowLength;
            String[] lineArray = null;
            
            matrix = new int[rowLength][columnLength];

            for(int columnIndex = 0; line != null; columnIndex++, line = br.readLine()) {
                lineArray = line.split(";");
                for(int rowIndex=0; rowIndex < matrix[columnIndex].length; rowIndex++) {
                    matrix[rowIndex][columnIndex] = Integer.parseInt(lineArray[rowIndex]);
                }
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] clone() {
        int[][] clone = new int[rowLength][columnLength];

        for(int columnIndex=0; columnIndex < columnLength; columnIndex++) {
            for(int rowIndex=0; rowIndex < rowLength; rowIndex++) {
                clone[rowIndex][columnIndex] = matrix[rowIndex][columnIndex];
            }
        }

        return clone;
    }

    public void randomAdjazenzMatrix() {
        Random r = new Random();
        for(int columnIndex=0; columnIndex < columnLength; columnIndex++) {
            for(int rowIndex=0; rowIndex < rowLength; rowIndex++) {
                if(rowIndex == columnIndex) {
                    continue;
                }
                matrix[rowIndex][columnIndex] = r.nextInt(2);
            }
        }
    }

    public String toString() {
        String s = "";
        for(int columnIndex=0; columnIndex < columnLength; columnIndex++) {
            for(int rowIndex=0; rowIndex < rowLength; rowIndex++) {
                s += matrix[rowIndex][columnIndex] + " ";
            }
            s += "\n";
        }
        return s;
    }
}