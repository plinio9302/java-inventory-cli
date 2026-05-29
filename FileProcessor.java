import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Utility class for reading product data from a CSV file.
 * The file is expected to contain product attributes in each line:
 * ID, name, price, category, stock, and a semicolon-separated list of tags.
 */
public class FileProcessor {

    /**
     * Reads a CSV file and converts each line into a Product object.
     * The file must contain a header line, which will be skipped.
     *
     * Expected format per line:
     * ID,Name,Price,Category,Stock,tag1;tag2;tag3
     *
     * @param fileName The path to the CSV file to read.
     * @return A Set of Product objects parsed from the file.
     */
    public static Set<Product> readUsersFile(String fileName) {
        Set<Product> products = new HashSet<>();

        try (Scanner sc = new Scanner(new FileReader(fileName))) {

            // Handle empty file
            if (!sc.hasNextLine()) {
                System.out.println("Warning: File is empty: " + fileName);
                return products;
            }

            // Skip header
            String header = sc.nextLine();

            // Read lines
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] splitLine = line.split(",");

                // SAFETY CHECK — prevents ArrayIndexOutOfBoundsException
                if (splitLine.length < 5) {
                    System.out.println("Invalid line (too few columns): " + line);
                    continue;
                }

                String iD = splitLine[0];
                String name = splitLine[1];
                double price = Double.parseDouble(splitLine[2]);
                String category = splitLine[3];
                int stock = Integer.parseInt(splitLine[4]);

                // Process tags
                String[] tagTokens = splitLine[5].split(";");
                Set<String> tags = new HashSet<>();
                for (String t : tagTokens) {
                    tags.add(t);
                }

                Product pro = new Product(iD, name, price, category, stock, tags);
                products.add(pro);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found → " + fileName);
        }

        return products;
    }
}
