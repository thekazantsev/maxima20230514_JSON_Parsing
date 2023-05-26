import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SimpleToConsoleFileReader {
    public static void main(String[] args) throws FileNotFoundException {

        String fileName = "src/main/resources/Cinema.json";
        try (FileInputStream fis = new FileInputStream(fileName);
             Scanner scanner = new Scanner(fis, "UTF-8")) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
