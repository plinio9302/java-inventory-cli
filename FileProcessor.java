import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Utility class for loading product data from a CSV file.
 *
 * <p>Expected file format (comma-delimited, with a header row):</p>
 * <pre>
 * product_id,name,price,category,stock,tags
 * ELEC001,Wireless Mouse,29.99,Electronics,45,wireless;bestseller;in-stock
 * </pre>
 *
 * <p>Tags within a row are semicolon-separated. All tag strings are
 * normalised to lower case at load time so that tag comparisons are
 * consistently case-insensitive throughout the application.</p>
 *
 * @author Plinio Durango
 * @version 2.0
 */
public class FileProcessor {

    /** Expected number of columns in each data row (excluding the header). */
    private static final int EXPECTED_COLUMNS = 6;

    /**
     * Reads a CSV file and converts each valid row into a {@link Product}.
     *
     * <p>The first line is treated as a header and skipped. Lines with fewer
     * than {@value #EXPECTED_COLUMNS} columns are logged to {@code stderr} and
     * skipped. Any {@link NumberFormatException} on the price or stock field
     * is similarly reported and the row is skipped.</p>
     *
     * @param fileName path to the CSV file
     * @return a {@link Set} of {@link Product} objects parsed from the file;
     *         never {@code null} (returns an empty set on error)
     */
    public static Set<Product> readUsersFile(String fileName) {
        Set<Product> products = new HashSet<>();

        try (Scanner sc = new Scanner(new FileReader(fileName))) {

            if (!sc.hasNextLine()) {
                System.err.println("Warning: file is empty: " + fileName);
                return products;
            }

            sc.nextLine(); // skip header

            int lineNumber = 1;
            while (sc.hasNextLine()) {
                lineNumber++;
                String line = sc.nextLine().trim();

                if (line.isEmpty()) continue;

                String[] cols = line.split(",");

                if (cols.length < EXPECTED_COLUMNS) {
                    System.err.println("Warning (line " + lineNumber + "): expected "
                            + EXPECTED_COLUMNS + " columns but found " + cols.length
                            + " — skipping: " + line);
                    continue;
                }

                try {
                    String id       = cols[0].trim();
                    String name     = cols[1].trim();
                    double price    = Double.parseDouble(cols[2].trim());
                    String category = cols[3].trim();
                    int    stock    = Integer.parseInt(cols[4].trim());

                    // Normalise tags to lower case for consistent comparisons.
                    Set<String> tags = new HashSet<>();
                    for (String tag : cols[5].trim().split(";")) {
                        String t = tag.trim().toLowerCase();
                        if (!t.isEmpty()) tags.add(t);
                    }

                    products.add(new Product(id, name, price, category, stock, tags));

                } catch (NumberFormatException e) {
                    System.err.println("Warning (line " + lineNumber
                            + "): invalid number format — skipping: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: file not found: " + fileName);
        }

        return products;
    }
}
