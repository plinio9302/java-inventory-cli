import java.util.Set;
import java.util.HashSet;

/**
 * Represents a product in the inventory system.
 * Each product has an ID, name, price, category, stock quantity, and a set of tags.
 */
public class Product {
    private String iD;
    private String name;
    private double price;
    private String category;
    private int stock;
    private Set<String> tags;

    /**
     * Constructs a new Product with all fields initialized.
     *
     * @param iD        The product's unique identifier.
     * @param name      The name of the product.
     * @param price     The price of the product.
     * @param category  The category the product belongs to.
     * @param stock     The quantity available in inventory.
     * @param tags      A set of tags associated with the product.
     */
    public Product(String iD, String name, double price,
                    String category, int stock, Set<String> tags) {

        this.iD = iD;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.tags = tags;
    }

    /**
     * Sets the product ID.
     *
     * @param newID The new ID to assign.
     */
    public void setID(String newID) {
        this.iD = newID;
    }

    /**
     * Sets the product name.
     *
     * @param newName The new name to assign.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets the product price.
     *
     * @param newPrice The new price value.
     */
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    /**
     * Sets the product category.
     *
     * @param newCategory The new category name.
     */
    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    /**
     * Sets the product's stock quantity.
     *
     * @param newStock The new stock amount.
     */
    public void setStock(int newStock) {
        this.stock = newStock;
    }

    /**
     * Sets the tags associated with the product.
     *
     * @param newTags A new set of tags.
     */
    public void setTags(Set<String> newTags) {
        this.tags = newTags;
    }

    /**
     * @return The product's ID.
     */
    public String getID() {
        return this.iD;
    }

    /**
     * @return The product's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The product's price.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * @return The product's category.
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * @return The product's stock quantity.
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * @return The set of tags associated with the product.
     */
    public Set<String> getTags() {
        return this.tags;
    }

    /**
     * Determines whether two Product objects are equal by comparing their IDs.
     *
     * @param obj The object to compare to.
     * @return true if the IDs match, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return this.iD.equals(other.iD);
    }

    /**
     * Generates a hash code based on the product ID.
     *
     * @return The hash code for this product.
     */
    @Override
    public int hashCode() {
        return iD.hashCode();
    }

    /**
     * @return A formatted string representation of the product.
     */
    @Override
    public String toString() {
        return String.format(
            "ID: %s%nName: %s%nPrice: %.2f%nCategory: %s%nStock: %d%nTags: %s",
            this.iD, this.name, this.price, this.category,
            this.stock, this.tags
        );
    }

}
