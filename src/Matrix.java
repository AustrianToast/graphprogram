import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Matrix {
    private String[] result;
    
    public static void main(String[] args) {
    
    }

    public String[] importCSV(String file) {
        result = new String[100];
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                result = line.trim().split(";");
            }
            
            br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void printCSV() {
        
    }
}