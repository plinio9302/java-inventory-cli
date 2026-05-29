import java.util.Set;
import java.util.HashSet;

/**
 * A Binary Search Tree (BST) storing Product objects ordered by price.
 * Supports insertion, removal, searching, and retrieving products in a price range.
 */
public class BST {
    public BSTNode root;

    /**
     * Creates an empty BST.
     */
    public BST() {
        root = null;
    }

    /*=============== SEARCH ===============*/

    /**
     * Searches for a product with the given price.
     *
     * @param price The price to search for.
     * @return true if a product with that price exists, false otherwise.
     */
    public boolean search(double price) {
        return searchHelper(root, price);
    }

    /**
     * Recursive search helper.
     *
     * @param node  Current node in traversal.
     * @param price Price to search for.
     * @return true if found, false otherwise.
     */
    private boolean searchHelper(BSTNode node, double price) {
        if (node == null) return false;

        double nodePrice = node.product.getPrice();

        if (price == nodePrice) return true;
        else if (price < nodePrice) return searchHelper(node.left, price);
        else return searchHelper(node.right, price);
    }

    /*=============== FIND MIN/MAX ===============*/

    /**
     * Finds the node with the minimum price in a subtree.
     *
     * @param node Root of subtree.
     * @return Node with the smallest price.
     */
    public BSTNode findMin(BSTNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Finds the node with the maximum price in a subtree.
     *
     * @param node Root of subtree.
     * @return Node with the highest price.
     */
    public BSTNode findMax(BSTNode node) {
        while (node.right != null) node = node.right;
        return node;
    }

    /**
     * Returns the product with the minimum price in the entire BST.
     *
     * @return Product with lowest price or null if tree is empty.
     */
    public Product findMin() {
        if (root == null) return null;

        BSTNode curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.product;
    }

    /**
     * Returns the product with the maximum price in the entire BST.
     *
     * @return Product with highest price or null if tree is empty.
     */
    public Product findMax() {
        if (root == null) return null;

        BSTNode curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.product;
    }

    /*=============== REMOVE USING ID ===============*/

    /**
     * Removes a specific product from the BST (matches by price and ID).
     *
     * @param target The target product to remove.
     */
    public void remove(Product target) {
        root = removeHelper(root, target);
    }

    /**
     * Recursive remove helper.
     *
     * @param node   Current node in traversal.
     * @param target Product to remove.
     * @return Updated subtree root.
     */
    private BSTNode removeHelper(BSTNode node, Product target) {
        if (node == null) return null;

        double nodePrice = node.product.getPrice();
        double targetPrice = target.getPrice();

        if (targetPrice < nodePrice) {
            node.left = removeHelper(node.left, target);
        }
        else if (targetPrice > nodePrice) {
            node.right = removeHelper(node.right, target);
        }
        else {
            // SAME PRICE, ensure ID matches
            if (!node.product.getID().equals(target.getID())) {
                node.left = removeHelper(node.left, target);
                return node;
            }

            // Case 1: no children
            if (node.left == null && node.right == null) return null;

            // Case 2: only right child
            if (node.left == null) return node.right;

            // Case 3: only left child
            if (node.right == null) return node.left;

            // Case 4: two children → replace with inorder successor
            BSTNode succ = findMin(node.right);
            node.product = succ.product;
            node.right = removeHelper(node.right, succ.product);
        }
        return node;
    }

    /*=============== GET PRODUCTS IN PRICE RANGE ===============*/

    /**
     * Returns all products whose prices fall within a given range.
     *
     * @param min Minimum price (inclusive).
     * @param max Maximum price (inclusive).
     * @return A Set of all products in the range.
     */
    public Set<Product> getProductsInRange(double min, double max) {
        Set<Product> result = new HashSet<>();
        rangeHelper(root, min, max, result);
        return result;
    }

    /**
     * Recursive helper to add products in a price range.
     *
     * @param node   Current node.
     * @param min    Minimum price.
     * @param max    Maximum price.
     * @param result Result set to collect products.
     */
    private void rangeHelper(BSTNode node, double min, double max, Set<Product> result) {
        if (node == null) return;

        double nodePrice = node.product.getPrice();

        if (nodePrice >= min && nodePrice <= max) {
            result.add(node.product);
            rangeHelper(node.left, min, max, result);
            rangeHelper(node.right, min, max, result);
        }
        else if (nodePrice < min) {
            rangeHelper(node.right, min, max, result);
        }
        else {
            rangeHelper(node.left, min, max, result);
        }
    }

    /*=============== INSERT ===============*/

    /**
     * Inserts a product into the BST based on its price.
     *
     * @param p Product to insert.
     */
    public void insert(Product p) {
        root = insertHelper(root, p);
    }

    /**
     * Recursive insert helper.
     *
     * @param node Current subtree root.
     * @param p    Product to insert.
     * @return Updated subtree root.
     */
    private BSTNode insertHelper(BSTNode node, Product p) {
        if (node == null) {
            return new BSTNode(p);
        }

        double nodePrice = node.product.getPrice();
        double price = p.getPrice();

        if (price <= nodePrice) {
            node.left = insertHelper(node.left, p);
        } 
        else {
            node.right = insertHelper(node.right, p);
        }

        return node;
    }

    /*=============== INORDER TRAVERSAL ===============*/

    /**
     * Prints the BST contents in ascending price order.
     */
    public void inorderTraversal() {
        inorderHelper(root);
    }

    /**
     * Recursive inorder traversal helper.
     *
     * @param node Current node.
     */
    private void inorderHelper(BSTNode node) {
        if (node == null) return;

        inorderHelper(node.left);
        System.out.println(node.product);
        System.out.println();
        inorderHelper(node.right);
    }

    /*=============== NODE CLASS ===============*/

    /**
     * Represents a single node in the BST.
     * Holds a Product and references to left/right children.
     */
    private static class BSTNode {
        public Product product;
        public BSTNode left;
        public BSTNode right;

        /**
         * Creates a node storing a product.
         *
         * @param product The product to store.
         */
        public BSTNode(Product product) {
            this.product = product;
            this.left = null;
            this.right = null;
        }
    }
}
