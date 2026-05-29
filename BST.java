import java.util.HashSet;
import java.util.Set;

/**
 * A Binary Search Tree (BST) that stores {@link Product} objects ordered by price.
 *
 * <p>Products with equal prices are placed in the left subtree. The tree supports
 * insertion, removal (by product ID), in-order traversal, price-range queries,
 * and min/max lookups.</p>
 *
 * <p><b>Complexity:</b> O(log n) average for all operations; O(n) worst-case
 * on a degenerate (sorted-insert) tree.</p>
 *
 * @author Plinio Durango
 * @version 2.0
 */
public class BST {

    // Package-private so ProductManager can access it if needed in tests.
    BSTNode root;

    /** Creates an empty BST. */
    public BST() {
        root = null;
    }

    // =========================================================================
    // INSERT
    // =========================================================================

    /**
     * Inserts a product into the BST ordered by price.
     * Products with the same price are placed in the left subtree.
     *
     * @param p the product to insert
     */
    public void insert(Product p) {
        root = insertHelper(root, p);
    }

    private BSTNode insertHelper(BSTNode node, Product p) {
        if (node == null) return new BSTNode(p);
        if (p.getPrice() <= node.product.getPrice()) {
            node.left  = insertHelper(node.left,  p);
        } else {
            node.right = insertHelper(node.right, p);
        }
        return node;
    }

    // =========================================================================
    // REMOVE
    // =========================================================================

    /**
     * Removes the product that matches {@code target} by both price and ID.
     *
     * <p>When multiple products share the same price the tree is searched
     * in both subtrees until the correct ID is found.</p>
     *
     * @param target the product to remove
     */
    public void remove(Product target) {
        root = removeHelper(root, target);
    }

    /**
     * Recursive remove helper.
     *
     * @param node   current subtree root
     * @param target product to remove (matched by price then ID)
     * @return updated subtree root
     */
    private BSTNode removeHelper(BSTNode node, Product target) {
        if (node == null) return null;

        double nodePrice   = node.product.getPrice();
        double targetPrice = target.getPrice();

        if (targetPrice < nodePrice) {
            // Target is cheaper — go left
            node.left = removeHelper(node.left, target);
        } else if (targetPrice > nodePrice) {
            // Target is more expensive — go right
            node.right = removeHelper(node.right, target);
        } else {
            // Prices match — verify by ID
            if (node.product.getID().equals(target.getID())) {
                // Found the exact node to remove
                if (node.left == null)  return node.right;
                if (node.right == null) return node.left;

                // Two children: replace with in-order successor
                BSTNode succ = findMinNode(node.right);
                node.product = succ.product;
                node.right   = removeHelper(node.right, succ.product);
            } else {
                // Same price but different ID — search BOTH subtrees because
                // equal-price products can be on either side.
                node.left  = removeHelper(node.left,  target);
                node.right = removeHelper(node.right, target);
            }
        }
        return node;
    }

    // =========================================================================
    // SEARCH
    // =========================================================================

    /**
     * Returns {@code true} if any product in the tree has the given price.
     *
     * @param price the price to search for
     * @return {@code true} if found, {@code false} otherwise
     */
    public boolean search(double price) {
        return searchHelper(root, price);
    }

    private boolean searchHelper(BSTNode node, double price) {
        if (node == null) return false;
        double nodePrice = node.product.getPrice();
        if (price == nodePrice)   return true;
        if (price  < nodePrice)   return searchHelper(node.left,  price);
        return searchHelper(node.right, price);
    }

    // =========================================================================
    // MIN / MAX
    // =========================================================================

    /**
     * Returns the product with the lowest price in the tree.
     *
     * @return the cheapest product, or {@code null} if the tree is empty
     */
    public Product findMin() {
        if (root == null) return null;
        return findMinNode(root).product;
    }

    /**
     * Returns the product with the highest price in the tree.
     *
     * @return the most expensive product, or {@code null} if the tree is empty
     */
    public Product findMax() {
        if (root == null) return null;
        return findMaxNode(root).product;
    }

    /**
     * Returns the node with the minimum price in a subtree.
     *
     * @param node root of the subtree
     * @return the leftmost (minimum-price) node
     */
    public BSTNode findMinNode(BSTNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Returns the node with the maximum price in a subtree.
     *
     * @param node root of the subtree
     * @return the rightmost (maximum-price) node
     */
    public BSTNode findMaxNode(BSTNode node) {
        while (node.right != null) node = node.right;
        return node;
    }

    // =========================================================================
    // PRICE-RANGE QUERY
    // =========================================================================

    /**
     * Returns all products whose prices fall within [{@code min}, {@code max}].
     *
     * @param min minimum price (inclusive)
     * @param max maximum price (inclusive)
     * @return set of matching products; never {@code null}
     */
    public Set<Product> getProductsInRange(double min, double max) {
        Set<Product> result = new HashSet<>();
        rangeHelper(root, min, max, result);
        return result;
    }

    private void rangeHelper(BSTNode node, double min, double max, Set<Product> result) {
        if (node == null) return;
        double p = node.product.getPrice();
        if (p >= min && p <= max) {
            result.add(node.product);
            rangeHelper(node.left,  min, max, result);
            rangeHelper(node.right, min, max, result);
        } else if (p < min) {
            rangeHelper(node.right, min, max, result);
        } else {
            rangeHelper(node.left,  min, max, result);
        }
    }

    // =========================================================================
    // IN-ORDER TRAVERSAL
    // =========================================================================

    /**
     * Prints all products to {@code stdout} in ascending price order.
     */
    public void inorderTraversal() {
        inorderHelper(root);
    }

    private void inorderHelper(BSTNode node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.println(node.product);
        System.out.println();
        inorderHelper(node.right);
    }

    // =========================================================================
    // NODE
    // =========================================================================

    /**
     * Internal tree node holding a {@link Product} and left/right child references.
     */
    static class BSTNode {
        Product product;
        BSTNode left;
        BSTNode right;

        BSTNode(Product product) {
            this.product = product;
        }
    }
}
