import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a shopping cart that stores {@link Product} objects selected by the user.
 *
 * <p>The cart:
 * <ul>
 *   <li>Uses a {@link HashTable} to look up products by ID when adding items.</li>
 *   <li>Stores products in a {@link Set} to prevent duplicate entries.</li>
 *   <li>Provides operations to add, display, remove, search, compute the total price,
 *       apply a percentage discount, count items, and export the cart to a text file.</li>
 * </ul></p>
 *
 * @author Plinio Durango
 * @version 2.0
 */
public class ShoppingCart {

    /** Hash table used to look up products by their ID. */
    private HashTable ht;

    /** Binary search tree reference (available for future extensions). */
    private BST bst;

    /** Set of products currently in the shopping cart (no duplicate products allowed). */
    private Set<Product> cart;

    /**
     * Constructs a {@code ShoppingCart} using the given hash table and BST.
     *
     * @param ht  the hash table used to look up products by ID
     * @param bst the BST containing products (available for future extensions)
     */
    public ShoppingCart(HashTable ht, BST bst) {
        this.ht  = ht;
        this.bst = bst;
        this.cart = new HashSet<>();
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    /**
     * Returns the set of products currently in the shopping cart.
     *
     * @return a {@link Set} of {@link Product} objects in the cart
     */
    public Set<Product> getCart() {
        return this.cart;
    }

    /**
     * Returns the number of items currently in the cart.
     *
     * @return the item count (0 if the cart is empty)
     */
    public int getItemCount() {
        return cart.size();
    }

    // =========================================================================
    // CORE CART OPERATIONS
    // =========================================================================

    /**
     * Adds an item to the cart by product ID.
     *
     * <p>Looks up the product in the hash table using the given ID.
     * If found, adds the product to the cart set (duplicates are automatically ignored
     * because the cart uses a {@link HashSet}).  If not found, prints an error message.</p>
     *
     * @param iD the ID of the product to add
     * @return {@code true} if the product was found (and added or was already in the cart),
     *         {@code false} if no product with the given ID exists
     */
    public boolean add(String iD) {
        Product p = ht.getProduct(iD);
        if (p != null) {
            cart.add(p);
            return true;
        } else {
            System.out.println("Product not found.");
            return false;
        }
    }

    /**
     * Displays all items currently in the cart.
     *
     * <p>If the cart is empty, prints a message indicating that the cart is empty.
     * Otherwise, each product is printed on its own line followed by a blank line.</p>
     */
    public void display() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("=== Shopping Cart (" + cart.size() + " item(s)) ===");
        for (Product p : cart) {
            System.out.println(p);
            System.out.println();
        }
    }

    /**
     * Calculates and returns the total price of all items in the cart.
     *
     * @return the sum of the prices of all products in the cart;
     *         returns {@code 0.0} if the cart is empty (and prints a message)
     */
    public double totalPrice() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is currently empty.");
            return 0.0;
        }
        double total = 0.0;
        for (Product p : cart) {
            total += p.getPrice();
        }
        return total;
    }

    /**
     * Checks whether a product with the given ID is present in the cart.
     *
     * @param iD the ID of the product to search for
     * @return {@code true} if a product with the given ID is in the cart;
     *         {@code false} otherwise
     */
    public boolean search(String iD) {
        for (Product p : cart) {
            if (p.getID().equals(iD)) return true;
        }
        return false;
    }

    /**
     * Removes the product with the given ID from the cart.
     *
     * <p>Prints a message if the cart is empty, if the item is not found,
     * or on successful removal.</p>
     *
     * @param iD the ID of the product to remove from the cart
     */
    public void remove(String iD) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty — nothing to remove.");
            return;
        }
        if (!search(iD)) {
            System.out.println("Item not found in cart.");
            return;
        }
        Product toRemove = null;
        for (Product p : cart) {
            if (p.getID().equalsIgnoreCase(iD)) {
                toRemove = p;
                break;
            }
        }
        cart.remove(toRemove);
        System.out.println("\"" + toRemove.getName() + "\" successfully removed from cart.");
    }

    /**
     * Clears all items from the shopping cart.
     */
    public void clear() {
        cart.clear();
        System.out.println("Cart cleared.");
    }

    // =========================================================================
    // A-GRADE FEATURES
    // =========================================================================

    /**
     * Calculates the cart total after applying a percentage discount.
     *
     * <p>For example, {@code discountPercent = 10.0} means a 10% discount,
     * so the method returns 90% of the total price.</p>
     *
     * @param discountPercent the discount to apply (0.0 to 100.0)
     * @return the discounted total; returns {@code 0.0} if the cart is empty
     * @throws IllegalArgumentException if {@code discountPercent} is outside [0, 100]
     */
    public double applyDiscount(double discountPercent) {
        if (discountPercent < 0.0 || discountPercent > 100.0) {
            throw new IllegalArgumentException(
                "Discount must be between 0 and 100. Got: " + discountPercent);
        }
        double full    = totalPrice();
        double savings = full * (discountPercent / 100.0);
        double discounted = full - savings;
        System.out.printf("Original total : $%.2f%n", full);
        System.out.printf("Discount (%.1f%%): -$%.2f%n", discountPercent, savings);
        System.out.printf("Final total    : $%.2f%n", discounted);
        return discounted;
    }

    /**
     * Saves the current cart contents to a text file.
     *
     * <p>Writes each product (one per line) followed by a summary total.
     * If the cart is empty, a message is printed and no file is created.</p>
     *
     * @param filePath the path of the output file (e.g. {@code "cart_export.txt"})
     */
    public void exportToFile(String filePath) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty — nothing to export.");
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("=== Cart Export ===");
            double total = 0.0;
            for (Product p : cart) {
                pw.println(p);
                total += p.getPrice();
            }
            pw.printf("%nTotal: $%.2f%n", total);
            System.out.println("Cart exported to: " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting cart: " + e.getMessage());
        }
    }
}
