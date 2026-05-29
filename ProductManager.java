import java.util.*;

/**
 * Main driver class for the Inventory Management System.
 *
 * <p>Loads products from a CSV file into a hash table, a BST, and a master
 * set, then presents an interactive CLI menu for product management and
 * shopping-cart operations.</p>
 *
 * <p><b>Menu options:</b></p>
 * <ol>
 *   <li>Add a new product</li>
 *   <li>Search a product by ID</li>
 *   <li>Display all products sorted by price (BST in-order)</li>
 *   <li>Remove a product by ID</li>
 *   <li>Find products within a price range</li>
 *   <li>Find the most and least expensive products</li>
 *   <li>Find products by a single tag</li>
 *   <li>Find products matching ALL specified tags (intersection)</li>
 *   <li>Find products in a category excluding a tag (set difference)</li>
 *   <li>Shopping cart</li>
 *   <li>Find products matching ANY specified tag (union)</li>
 *   <li>Filter products by tag and price range (combined)</li>
 *   <li>Quit</li>
 * </ol>
 *
 * @author Plinio Durango
 * @version 3.0
 */
public class ProductManager {

    /** Scanner shared across all input operations. */
    private static final Scanner sc = new Scanner(System.in);

    /** Master set of all products — used for tag/category set operations. */
    private static Set<Product> products;

    // =========================================================================
    // ENTRY POINT
    // =========================================================================

    /**
     * Application entry point.
     *
     * @param args expects exactly one argument: the path to the CSV product file.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ProductManager <filename>");
            System.exit(1);
        }

        String filePath = args[0];
        HashTable hTable = new HashTable();
        BST bst = new BST();
        products = FileProcessor.readUsersFile(filePath);

        for (Product p : products) {
            hTable.insert(p);
            bst.insert(p);
        }

        // Shopping cart lives outside the menu loop so it persists across sessions.
        ShoppingCart shc = new ShoppingCart(hTable, bst);

        while (true) {
            printMenu();
            int choice = intValidation();

            if (choice < 1 || choice > 13) {
                System.out.println("Please enter a number from 1 to 13.");
                continue;
            }

            if (choice == 13) {
                System.out.println("Goodbye!");
                break;
            } else if (choice == 1) {
                addProduct(hTable, bst);
            } else if (choice == 2) {
                searchById(hTable);
            } else if (choice == 3) {
                System.out.println("\nProducts sorted by price:");
                bst.inorderTraversal();
            } else if (choice == 4) {
                removeProduct(hTable, bst);
            } else if (choice == 5) {
                filterByPriceRange(bst);
            } else if (choice == 6) {
                showMinMax(bst);
            } else if (choice == 7) {
                filterBySingleTag();
            } else if (choice == 8) {
                filterByAllTags();
            } else if (choice == 9) {
                filterByCategoryNotTag();
            } else if (choice == 10) {
                shoppingCartMenu(shc);
            } else if (choice == 11) {
                filterByAnyTag();
            } else if (choice == 12) {
                filterByTagAndPriceRange();
            }
        }
        sc.close();
    }

    // =========================================================================
    // MENU HANDLERS
    // =========================================================================

    /**
     * Prompts the user for product details and inserts the new product into
     * all three data structures.
     *
     * @param hTable the hash table to insert into
     * @param bst    the BST to insert into
     */
    private static void addProduct(HashTable hTable, BST bst) {
        System.out.print("ID: ");
        String id = sc.nextLine().trim();

        if (hTable.getProduct(id) != null) {
            System.out.println("A product with ID '" + id + "' already exists.");
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        System.out.print("Category: ");
        String category = sc.nextLine().trim();

        System.out.print("Stock quantity: ");
        int stock = sc.nextInt();
        sc.nextLine();

        System.out.print("Tags (comma-separated): ");
        String tagLine = sc.nextLine();
        Set<String> tags = parseTags(tagLine, ",");

        Product newProduct = new Product(id, name, price, category, stock, tags);
        hTable.insert(newProduct);
        bst.insert(newProduct);
        products.add(newProduct);
        System.out.println("Product '" + name + "' added successfully.");
    }

    /**
     * Searches for a product by ID and prints it if found.
     *
     * @param hTable the hash table to search
     */
    private static void searchById(HashTable hTable) {
        System.out.print("Enter product ID: ");
        String id = sc.nextLine().trim();
        Product found = hTable.getProduct(id);
        if (found != null) {
            System.out.println(found);
        } else {
            System.out.println("No product found with ID '" + id + "'.");
        }
    }

    /**
     * Removes a product from all three data structures.
     *
     * @param hTable the hash table
     * @param bst    the BST
     */
    private static void removeProduct(HashTable hTable, BST bst) {
        System.out.print("Enter product ID to remove: ");
        String id = sc.nextLine().trim();
        Product p = hTable.getProduct(id);

        if (p == null) {
            System.out.println("No product found with ID '" + id + "'.");
            return;
        }

        bst.remove(p);
        hTable.remove(id);
        products.remove(p);
        System.out.println("'" + p.getName() + "' removed successfully.");
    }

    /**
     * Prompts for a price range and displays all matching products.
     *
     * @param bst the BST to query
     */
    private static void filterByPriceRange(BST bst) {
        double[] range = promptPriceRange();
        Set<Product> result = bst.getProductsInRange(range[0], range[1]);
        if (result.isEmpty()) {
            System.out.println("No products found in that price range.");
        } else {
            result.forEach(p -> System.out.println(p + "\n"));
        }
    }

    /**
     * Displays the least and most expensive products.
     *
     * @param bst the BST to query
     */
    private static void showMinMax(BST bst) {
        Product min = bst.findMin();
        Product max = bst.findMax();
        if (min == null) {
            System.out.println("No products in inventory.");
            return;
        }
        System.out.println("Least expensive:\n" + min + "\n");
        System.out.println("Most expensive:\n" + max);
    }

    /**
     * Finds products that contain a single specified tag.
     */
    private static void filterBySingleTag() {
        System.out.print("Enter tag: ");
        String tag = sc.nextLine().trim().toLowerCase();
        Set<Product> result = new HashSet<>();
        for (Product p : products) {
            if (p.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag))) {
                result.add(p);
            }
        }
        printResults(result, "Products tagged '" + tag + "':", "No products found with tag '" + tag + "'.");
    }

    /**
     * Finds products that match ALL of the specified tags (set intersection).
     */
    private static void filterByAllTags() {
        System.out.print("Enter tags (comma-separated): ");
        Set<String> userTags = parseTags(sc.nextLine(), ",");
        Set<Product> result = new HashSet<>();
        for (Product p : products) {
            Set<String> lower = lowerCaseTags(p.getTags());
            if (lower.containsAll(userTags)) {
                result.add(p);
            }
        }
        printResults(result, "Products matching ALL tags:", "No products match all specified tags.");
    }

    /**
     * Finds products in a given category that do NOT carry a specified tag
     * (set difference: category − tag).
     */
    private static void filterByCategoryNotTag() {
        System.out.print("Category to include: ");
        String includeCategory = sc.nextLine().trim().toLowerCase();
        System.out.print("Tag to exclude: ");
        String excludeTag = sc.nextLine().trim().toLowerCase();

        Set<Product> categorySet = new HashSet<>();
        Set<Product> excludedSet = new HashSet<>();

        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(includeCategory)) categorySet.add(p);
            if (p.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(excludeTag))) excludedSet.add(p);
        }
        categorySet.removeAll(excludedSet);

        String header = "Products in '" + includeCategory + "' without tag '" + excludeTag + "':";
        printResults(categorySet, header, "No matching products found.");
    }

    /**
     * Finds products that match ANY of the specified tags (set union).
     */
    private static void filterByAnyTag() {
        System.out.print("Enter tags (comma-separated): ");
        Set<String> userTags = parseTags(sc.nextLine(), ",");
        Set<Product> result = new HashSet<>();
        for (Product p : products) {
            for (String t : p.getTags()) {
                if (userTags.contains(t.toLowerCase())) {
                    result.add(p);
                    break;
                }
            }
        }
        printResults(result, "Products matching ANY of the tags:", "No products found for those tags.");
    }

    /**
     * Filters by tag first, then applies a price range on the matching subset.
     */
    private static void filterByTagAndPriceRange() {
        System.out.print("Enter tag: ");
        String tag = sc.nextLine().trim().toLowerCase();

        Set<Product> tagMatches = new HashSet<>();
        for (Product p : products) {
            if (p.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag))) {
                tagMatches.add(p);
            }
        }

        if (tagMatches.isEmpty()) {
            System.out.println("No products found with tag '" + tag + "'.");
            return;
        }

        BST subBST = new BST();
        tagMatches.forEach(subBST::insert);

        double[] range = promptPriceRange();
        Set<Product> result = subBST.getProductsInRange(range[0], range[1]);
        printResults(result, "Products tagged '" + tag + "' in price range:", "No products found in that range.");
    }

    // =========================================================================
    // SHOPPING CART SUB-MENU
    // =========================================================================

    /**
     * Presents the shopping-cart sub-menu and delegates to the appropriate
     * {@link ShoppingCart} methods.
     *
     * @param shc the shopping cart instance (persists across menu visits)
     */
    private static void shoppingCartMenu(ShoppingCart shc) {
        while (true) {
            printCartMenu();
            int choice = intValidation();

            if (choice < 1 || choice > 8) {
                System.out.println("Please enter a number from 1 to 8.");
                continue;
            }

            if (choice == 8) {
                System.out.println("Returning to main menu.");
                break;
            } else if (choice == 1) {
                System.out.print("Enter product ID: ");
                String id = sc.nextLine().trim();
                if (shc.add(id)) System.out.println("Product '" + id + "' added to cart.");
            } else if (choice == 2) {
                shc.display();
            } else if (choice == 3) {
                System.out.printf("Cart total: $%.2f%n", shc.totalPrice());
            } else if (choice == 4) {
                if (shc.getCart().isEmpty()) {
                    System.out.println("Your cart is empty.");
                } else {
                    System.out.print("Enter product ID to remove: ");
                    shc.remove(sc.nextLine().trim());
                }
            } else if (choice == 5) {
                if (shc.getCart().isEmpty()) {
                    System.out.println("Your cart is empty.");
                } else {
                    System.out.print("Enter product ID: ");
                    String id = sc.nextLine().trim();
                    System.out.println(shc.search(id)
                        ? "'" + id + "' is in the cart."
                        : "'" + id + "' is not in the cart.");
                }
            } else if (choice == 6) {
                shc.clear();
            } else if (choice == 7) {
                System.out.print("Discount percentage (e.g. 10 for 10%): ");
                double pct = sc.nextDouble();
                sc.nextLine();
                shc.applyDiscount(pct);
            }
        }
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    /**
     * Prompts the user for a valid minimum/maximum price range.
     *
     * @return a double array where [0] is the minimum and [1] is the maximum
     */
    private static double[] promptPriceRange() {
        double min, max;
        while (true) {
            System.out.print("Minimum price: $");
            min = sc.nextDouble();
            sc.nextLine();
            System.out.print("Maximum price: $");
            max = sc.nextDouble();
            sc.nextLine();
            if (min >= 0 && max >= min) break;
            System.out.println("Invalid range. Maximum must be ≥ minimum and both must be non-negative.");
        }
        return new double[]{min, max};
    }

    /**
     * Prints a result set with a header, or an empty message if the set is empty.
     *
     * @param result       the set of products to display
     * @param header       message to print before the results
     * @param emptyMessage message to print when the set is empty
     */
    private static void printResults(Set<Product> result, String header, String emptyMessage) {
        if (result.isEmpty()) {
            System.out.println(emptyMessage);
        } else {
            System.out.println("\n" + header);
            result.forEach(p -> System.out.println(p + "\n"));
        }
    }

    /**
     * Splits a tag string by the given delimiter, trims whitespace, and
     * lower-cases each token.
     *
     * @param tagLine   the raw tag string entered by the user
     * @param delimiter the separator character(s) to split on
     * @return a set of normalised tag strings
     */
    private static Set<String> parseTags(String tagLine, String delimiter) {
        Set<String> tags = new HashSet<>();
        for (String t : tagLine.split(delimiter)) {
            String trimmed = t.trim().toLowerCase();
            if (!trimmed.isEmpty()) tags.add(trimmed);
        }
        return tags;
    }

    /**
     * Returns a new set containing all tags from the given set in lower case.
     *
     * @param tags the original tag set
     * @return lower-cased copy
     */
    private static Set<String> lowerCaseTags(Set<String> tags) {
        Set<String> lower = new HashSet<>();
        for (String t : tags) lower.add(t.toLowerCase());
        return lower;
    }

    // =========================================================================
    // MENU PRINTERS
    // =========================================================================

    /** Prints the main menu. */
    private static void printMenu() {
        System.out.println("\n==============================");
        System.out.println(" Inventory Management System");
        System.out.println("==============================");
        System.out.println("[1]  Add new product");
        System.out.println("[2]  Search product by ID");
        System.out.println("[3]  Display products sorted by price");
        System.out.println("[4]  Remove a product by ID");
        System.out.println("[5]  Find products within a price range");
        System.out.println("[6]  Find most and least expensive products");
        System.out.println("[7]  Find products by a single tag");
        System.out.println("[8]  Find products matching ALL specified tags (intersection)");
        System.out.println("[9]  Find products in a category excluding a tag (difference)");
        System.out.println("[10] Shopping Cart");
        System.out.println("[11] Find products matching ANY specified tag (union)");
        System.out.println("[12] Filter by tag and price range");
        System.out.println("[13] Quit");
        System.out.print(">> ");
    }

    /** Prints the shopping cart sub-menu. */
    private static void printCartMenu() {
        System.out.println("\n--- Shopping Cart ---");
        System.out.println("[1] Add item by ID");
        System.out.println("[2] View cart");
        System.out.println("[3] View total price");
        System.out.println("[4] Remove item by ID");
        System.out.println("[5] Check if item is in cart");
        System.out.println("[6] Clear cart");
        System.out.println("[7] Apply discount");
        System.out.println("[8] Back to main menu");
        System.out.print(">> ");
    }

    // =========================================================================
    // INPUT VALIDATION
    // =========================================================================

    /**
     * Reads an integer from standard input, re-prompting on invalid input.
     *
     * @return a valid integer entered by the user
     */
    public static int intValidation() {
        while (true) {
            try {
                int n = sc.nextInt();
                sc.nextLine();
                return n;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                sc.next();
                System.out.print(">> ");
            }
        }
    }
}
