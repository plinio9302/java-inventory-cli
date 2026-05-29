import java.util.*;

/**
 * Main driver class for the product management system.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Load products from an input file.</li>
 *     <li>Store products in a hash table, BST, and a set.</li>
 *     <li>Provide a menu-driven interface to:
 *         <ul>
 *             <li>Add, search, and remove products.</li>
 *             <li>Display products sorted by price.</li>
 *             <li>Filter products by price range, category, and tags.</li>
 *             <li>Use set operations like intersection, difference, and union on products.</li>
 *             <li>Manage a simple shopping cart backed by a set.</li>
 *         </ul>
 *     </li>
 * </ul>
 * </p>
 */
public class ProductManager {

    /**
     * Shared {@link Scanner} instance used to read user input from standard input.
     */
    private static Scanner sc = new Scanner(System.in);
    
    /**
     * Master set of all products currently known to the system.
     * <p>
     * This set is used for tag/category-based filtering and as the
     * authoritative collection of products.
     * </p>
     */
    private static Set<Product> products;

    /**
     * Application entry point.
     * <p>
     * Expects a single command-line argument: the path to the input file
     * containing product data. The method:
     * <ol>
     *     <li>Validates the command-line argument.</li>
     *     <li>Loads products via {@code FileProcessor.readUsersFile(...)}.</li>
     *     <li>Inserts products into a {@code HashTable} and {@code BST}.</li>
     *     <li>Enters an interactive menu loop to perform various operations
     *         on the products and the shopping cart.</li>
     * </ol>
     * </p>
     *
     * @param args command-line arguments; expects exactly one argument: the file path.
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
        while (true) {

            printMenu();
            int choice = intValidation();

            if (choice < 1 || choice > 13) {
                System.out.println("Please type a number from 1 to 13.");
                continue;
            }

            if (choice == 13) {
                System.out.println("Quitting...");
                break;
            }

            // Add new product
            if (choice == 1) {
                System.out.print("Please type ID: ");
                String iD = sc.nextLine();

                System.out.print("Please type Name: ");
                String name = sc.nextLine();

                System.out.print("Please type Price: ");
                float price = sc.nextFloat();
                sc.nextLine();

                System.out.print("Please type Category: ");
                String category = sc.nextLine();

                System.out.print("Please type Quantity: ");
                int quantity = sc.nextInt();
                sc.nextLine();

                System.out.print("Please type tags (comma separated): ");
                String tagLine = sc.nextLine();
                Set<String> userTags = new HashSet<>();
                for (String t : tagLine.split(",")) userTags.add(t.trim().toLowerCase());

                Product newProduct = new Product(iD, name, price, category, quantity, userTags);

                hTable.insert(newProduct);
                bst.insert(newProduct);
                products.add(newProduct);

                System.out.println("Product added successfully!");
            }

            // Search by ID
            if (choice == 2) {
                System.out.print("Enter product ID: ");
                String id = sc.nextLine();

                Product found = hTable.getProduct(id);
                if (found != null) System.out.println(found);
                else System.out.println("Product not found.");
            }

            // Display products sorted by price
            if (choice == 3) {
                System.out.println("Products sorted by price:");
                bst.inorderTraversal();
            }

            // Remove by ID
            if (choice == 4) {
                System.out.print("Enter product ID: ");
                String id = sc.nextLine();

                Product p = hTable.getProduct(id);   // or getProduct(id)

                if (p == null) {
                    System.out.println("Product not found");
                }
                else {
                    System.out.println("---------Removing product from a BST-------------");
                    bst.remove(p);
                    System.out.println("Product: " + p.getName() + " successfully removed from BST");

                    System.out.println("---------Removing product from a hash table-----------");
                    Product removedProductHT =  hTable.remove(id);
                    System.out.println("Product: " + removedProductHT.getName() + " removed successfully from Hash Table.");

                    System.out.println("---------Removing product from a the Set -------------");
                    products.remove(p);
                    System.out.println("Product: " + p.getName() + " successfully removed from Set");



                }  
            }

            // Find products within range
            if (choice == 5) {

                double minValue = 0;
                double maxValue = 0;

                while(true) {
                    System.out.print("Please enter the minimum value: ");
                    minValue = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Please enter the maximum value: ");
                    maxValue = sc.nextDouble();
                    sc.nextLine();

                    if(minValue < maxValue) break;
                    else System.out.println("Maximum value must be greater than minimum value");
                }

                Set<Product> result = bst.getProductsInRange(minValue, maxValue);

                if (result.isEmpty()) {
                    System.out.println("No products found in that price range.");
                } else {
                    for (Product p : result) {
                        System.out.println(p);
                        System.out.println();
                    }
                }
            }


            // Find least and most expensive
            if (choice == 6) {
                Product leastExpensive = bst.findMin();
                Product mostExpensive = bst.findMax();

                System.out.println("Least expensive product: " + leastExpensive);
                System.out.println();
                System.out.println("Most expensive product: " + mostExpensive);
            }

            // Find by tag
            if (choice == 7) {

                System.out.print("Enter a tag: ");
                String targetTag = sc.nextLine().trim().toLowerCase();

                Set<Product> result = new HashSet<>();

                for (Product p : products) {
                    Set<String> productTags = p.getTags();

                    for (String t : productTags) {
                        if (t.toLowerCase().equals(targetTag)) {
                            result.add(p);
                            break;
                        }
                    }
                }

                if (result.isEmpty()) {
                    System.out.println("No products found with tag: " + targetTag);
                } else {
                    System.out.println("Products with tag '" + targetTag + "':");
                    for (Product p : result) {
                        System.out.println(p);
                        System.out.println();
                    }
                }
            }
            // Find products matching multiple tags"
            if (choice == 8) {

                System.out.print("Please type tags (comma separated): ");
                String tagLine = sc.nextLine();

                Set<String> userTags = new HashSet<>();
                for (String t : tagLine.split(",")) {
                    userTags.add(t.trim().toLowerCase());
                }

                Set<Product> result = new HashSet<>();

                for (Product p : products) {

                    
                    Set<String> productTags = new HashSet<>();
                    for (String t : p.getTags()) {
                        productTags.add(t.toLowerCase());
                    }

                    // Check if productTags contains ALL userTags
                    if (productTags.containsAll(userTags)) {
                        result.add(p);
                    }
                }

                // Print results
                if (result.isEmpty()) {
                    System.out.println("No matching products.");
                } else {
                    System.out.println("Products matching ALL tags:");
                    for (Product p : result){
                        System.out.println(p);
                        System.out.println();
                    }
                }
            }
            // Find products in one category but not another
            if (choice == 9) { 

                System.out.print("Category to include: ");
                String includeCategory = sc.nextLine().trim().toLowerCase();

                System.out.print("Tag to exclude: ");
                String excludeTag = sc.nextLine().trim().toLowerCase();

                Set<Product> categorySet = new HashSet<>();
                Set<Product> excludedSet = new HashSet<>();

                // Building CATEGORY set
                for (Product p : products) {
                    if (p.getCategory().equalsIgnoreCase(includeCategory)) {
                        categorySet.add(p);
                    }
                }

                // Building EXCLUDED set (products that *do* contain the tag)
                for (Product p : products) {
                    for (String tag : p.getTags()) {
                        if (tag.toLowerCase().equals(excludeTag)) {
                            excludedSet.add(p);
                            break;
                        }
                    }
                }

                // Set difference: categorySet - excludedSet
                categorySet.removeAll(excludedSet);

                // Output
                if (categorySet.isEmpty()) {
                    System.out.println("No products found.");
                } else {
                    System.out.println("Products in category '" + includeCategory + "' but NOT tagged '" + excludeTag + "':");
                    for (Product p : categorySet) {
                        System.out.println(p);
                    }
                }
            }
            // Shoping Cart
            if (choice == 10) {
                ShoppingCart shc = new ShoppingCart(hTable,bst);
                while(true) {
                    shopingCart();
                    int choice2 = intValidation();

                    if (choice2 < 1 || choice2 > 7) {
                        System.out.println("Please type a number from 1 to 7.");
                        continue;
                    }

                    if (choice2 == 7) {
                        System.out.println("Quitting...");
                        break;
                    }
                    //  Add items to cart by product ID
                    if (choice2 == 1) {
                        System.out.println("Please type the ID of the item to add:");
                
                        String id = sc.nextLine();
                   
                        boolean addItem = shc.add(id);
                        if(addItem) {
                            System.out.println("Product: " + id + " successfully added");
                        }
                    }
                    // Display all items currently in cart
                    if (choice2 == 2) {
                        shc.display();
                    }
                    // Calculate total cart price
                    if (choice2 == 3) {
                        double totalPrice = shc.totalPrice();
                        System.out.printf("Total price: %.2f", totalPrice);
                        System.out.println();
                    }
                    // Remove items from cart
                    if (choice2 == 4) {
                        if (shc.getCart().isEmpty()) {
                            System.out.println("Your cart is currently empty");
                        }
                        else {
                            System.out.println("Insert ID of the the item to remove");
                            String id = sc.nextLine();
                            shc.remove(id);
                        }
                    }             
                    // Check if a specific item is in the cart
                    if (choice2 == 5) {
                        if (shc.getCart().isEmpty()) {
                            System.out.println("Your cart is currently empty");
                        }
                        else {
                            System.out.println("Insert ID of the the item");
                            String id = sc.nextLine();
                            boolean isInCart = shc.search(id);
                            if(isInCart) {
                                System.out.println("The item '" + id + "' is in the cart");
                            }
                            else System.out.println("The item '" + id + "' is not in the cart");
                        }
                    }
                    // Clear entire cart
                    if (choice2 == 6) {
                        shc.getCart().clear();
                        System.out.println("Your cart is now empty");
                    }
                }
            }
            // Find products matching any of several tags using set union
            if (choice == 11) {
                System.out.print("Please type tags (comma separated): ");
                String tagLine = sc.nextLine();

                Set<String> userTags = new HashSet<>();
                for (String t : tagLine.split(",")) {
                    userTags.add(t.trim().toLowerCase());
                }

                Set<Product> result = new HashSet<>();
                for (String s : userTags) {
                    Set<Product> matchesForThisTag = new HashSet<>();

                    for (Product p : products) {
                        for (String t : p.getTags()) {
                            if (s.equalsIgnoreCase(t)) {
                                matchesForThisTag.add(p);
                            }
                        }
                    }
                    result.addAll(matchesForThisTag);
                }
                if (result.isEmpty()) {
                    System.out.println("No products found for those tags.");
                } else {
                    System.out.println("Products matching ANY of the tags:");
                    for (Product p : result) {
                        System.out.println(p);
                        System.out.println();
                    }
                }  
            }
            // Combine price range filtering with tag filtering
            if (choice == 12) {
                System.out.print("Enter a tag: ");
                String targetTag = sc.nextLine().trim().toLowerCase();

                Set<Product> filteredSet = new HashSet<>();

                for (Product p : products) {
                    Set<String> productTags = p.getTags();

                    for (String t : productTags) {
                        if (t.toLowerCase().equals(targetTag)) {
                            filteredSet.add(p);
                            break;
                        }
                    }
                }

                if (filteredSet.isEmpty()) {
                    System.out.println("No products found with tag: " + targetTag);
                } 
                else {
                    BST newBST = new BST();
                    for (Product p : filteredSet) {
                        newBST.insert(p);
                    }
                    double minValue = 0;
                    double maxValue = 0;

                    while(true) {
                        System.out.print("Please enter the minimum value: ");
                        minValue = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Please enter the maximum value: ");
                        maxValue = sc.nextDouble();
                        sc.nextLine();

                        if(minValue < maxValue) break;
                        else System.out.println("Maximum value must be greater than minimum value");
                    }
                    Set<Product> result = newBST.getProductsInRange(minValue, maxValue);

                    if (result.isEmpty()) {
                        System.out.println("No products found in that price range.");
                    } else {
                        for (Product p : result) {
                            System.out.println(p);
                            System.out.println();
                        }
                    }
                }           
            }
        }
    }

    /**
     * Prints the main menu of options for managing products and the shopping cart.
     * This method does not read any input; it only displays the choices.
     */
    private static void printMenu() {
        System.out.println("Options:");
        System.out.println("[1] Add new product");
        System.out.println("[2] Search product by ID");
        System.out.println("[3] Display products sorted by price");
        System.out.println("[4] Remove a product by ID");
        System.out.println("[5] Find products within a price range");
        System.out.println("[6] Find the most expensive and least expensive products");
        System.out.println("[7] Find and display all products with a user-specified tag");
        System.out.println("[8] Find products matching multiple tags");
        System.out.println("[9] Find products in one category but not another using set difference");
        System.out.println("[10] Shopping Cart");
        System.out.println("[11] Find products matching any of several tags using set union");
        System.out.println("[12] Combine price range filtering with tag filtering");
        System.out.println("[13] Quit");
        System.out.print(">> ");
    }

    /**
     * Prints the submenu for shopping cart operations.
     * <p>
     * This method only displays the available options; it does not
     * perform any actions or read input.
     * </p>
     */
    private static void shopingCart() {
    System.out.println("Shopping Cart Options:");
    System.out.println("[1] Add items to cart by product ID");
    System.out.println("[2] Display all items currently in cart");
    System.out.println("[3] Calculate total cart price");
    System.out.println("[4] Remove items from cart");
    System.out.println("[5] Check if a specific item is in the cart");
    System.out.println("[6] Clear entire cart");
    System.out.println("[7] Quit");
    System.out.print(">> ");
}


    /**
     * Reads and validates an integer from user input.
     * <p>
     * This method repeatedly prompts the user until a valid integer is entered.
     * If a non-integer is provided, the invalid token is discarded and an error
     * message is printed.
     * </p>
     *
     * @return a valid integer read from {@link #sc}.
     */
    public static int intValidation() {
        while (true) {
            try {
                int n = sc.nextInt();
                sc.nextLine();
                return n;
            } catch (InputMismatchException e) {
                System.out.println("Must enter a number.");
                sc.next();
                System.out.print(">> ");
            }
        }
    }
}
