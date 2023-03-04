import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Matrix {
    public static void main(String[] args) {
    
    }

    public String[] importCSV(String file) {
        String[] result = null;
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            line = br.readLine();

            while (line != null) {
                result = line.trim().split(";");
            }
            
            br.close();
		} catch (FileNotFoundException e) {
			e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return result;
    }
}