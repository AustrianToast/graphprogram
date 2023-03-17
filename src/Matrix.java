import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class Matrix {
    private int[][] matrix;

    public static void main(String[] args) {}

    public Matrix(String file) {
        matrix = this.readCSV(file);
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

    public void print() {
        for(int columnIndex=0; columnIndex < matrix.length; columnIndex++) {
            for(int rowIndex=0; rowIndex < matrix[columnIndex].length; rowIndex++) {
                System.out.print(matrix[columnIndex][rowIndex]);
            }
            System.out.println();
        }
    }

}
