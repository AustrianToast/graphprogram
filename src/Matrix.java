import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Matrix {
    private String[][] matrix;
    public static void main(String[] args) {
    
    }

    public String[][] importCSV(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            int rowCount = 0;
            int columnCount = line.split(";").length;

            while (line != null) {
                line = br.readLine();
                rowCount++;
            }

            matrix = new String[rowCount][columnCount];

            br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public void printCSV() {
        for(int i=0; i < matrix.length; i++) {
            for(int j=0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
}