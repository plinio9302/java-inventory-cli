import java.util.Set;
import java.util.HashSet;

/**
 * A hash table implementation that stores Product objects using
 * open addressing with linear probing.
 * The table resizes itself when the load factor reaches 0.6.
 */
public class HashTable {
    /** Array of hash table buckets. */
    private Entry[] table;

    /** Number of stored items (non-removed entries). */
    private int size;

    /** Current load factor = size / table.length. */
    private double loadFactor;

    /**
     * Default constructor initializing the table with a capacity of 11.
     */
    public HashTable() {
        this(11);
    }

    /**
     * Creates a hash table with the given initial capacity.
     * All buckets are initialized as EMPTY_SINCE_START.
     *
     * @param initialCapacity the initial table size
     */
    public HashTable(int initialCapacity) {
        table = new Entry[initialCapacity];
        size = 0;
        loadFactor = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = Entry.EMPTY_SINCE_START;
        }
    }

    /**
     * Computes the hash index for a product ID using Java's hashCode()
     * and ensures a non-negative result within table bounds.
     *
     * @param iD the product ID
     * @return the computed bucket index
     */
    private int hash(String iD) {
        return (iD.hashCode() & 0x7fffffff) % table.length;
    }

    /**
     * Inserts a product into the hash table.
     * If the ID already exists, the stored product is updated.
     * Resizes the table if the load factor exceeds 0.6.
     *
     * @param newProduct product to insert
     * @return true if inserted or updated, false if table is full
     */
    public boolean insert(Product newProduct) {
        if (loadFactor >= 0.6) {
            this.resize();
        }

        String key = newProduct.getID();
        int hashIndex = hash(key);

        // PHASE 1: Search for existing key
        for (int i = 0; i < table.length; i++) {
            int bucketIndex = (hashIndex + i) % table.length;
            Entry entry = table[bucketIndex];

            if (entry.isEmptySinceStart()) {
                break;
            }

            if (!entry.isEmptyAfterRemoval()) {
                if (key.equals(entry.iD)) {
                    entry.product = newProduct;
                    return true;
                }
            }
        }

        // PHASE 2: Insert into first empty bucket
        for (int i = 0; i < table.length; i++) {
            int bucketIndex = (hashIndex + i) % table.length;
            Entry entry = table[bucketIndex];

            if (entry.isEmpty()) {
                table[bucketIndex] = new Entry(key, newProduct);
                size++;
                loadFactor = (double) size / table.length;
                return true;
            }
        }

        return false;
    }

    /**
     * Doubles the hash table size (plus 1) and rehashes all elements.
     * Called automatically when loadFactor reaches ≥ 0.6.
     */
    public void resize() {
        HashTable newTable = new HashTable(2 * table.length + 1);

        for (Entry e : table) {
            if (!e.isEmpty()) {
                newTable.insert(e.product);
            }
        }

        this.table = newTable.table;
        this.size = newTable.size;
        this.loadFactor = (double) size / table.length;
    }

    /**
     * Removes a product from the hash table by ID.
     * Marks the bucket as EMPTY_AFTER_REMOVAL.
     *
     * @param iD product ID to remove
     * @return the removed Product, or null if not found
     */
    public Product remove(String iD) {
        int hashIndex = hash(iD);

        for (int i = 0; i < table.length; i++) {
            int bucketIndex = (hashIndex + i) % table.length;
            Entry entry = table[bucketIndex];

            if (entry.isEmptySinceStart()) {
                return null;
            }

            if (!entry.isEmptyAfterRemoval()) {
                if (iD.equals(entry.iD)) {
                    Product removed = entry.product;
                    table[bucketIndex] = Entry.EMPTY_AFTER_REMOVAL;
                    size--;
                    loadFactor = (double) size / table.length;
                    return removed;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a product by ID using linear probing.
     *
     * @param usersID the product ID to search for
     * @return the corresponding Product, or null if not found
     */
    public Product getProduct(String usersID) {
        int hashIndex = hash(usersID);

        for (int i = 0; i < table.length; i++) {
            int bucketIndex = (hashIndex + i) % table.length;
            Entry entry = table[bucketIndex];

            if (entry.isEmptySinceStart()) {
                return null;
            }

            if (entry.isEmptyAfterRemoval()) {
                continue;
            }

            if (usersID.equals(entry.iD)) {
                return entry.product;
            }
        }

        return null;
    }

    /**
     * Internal class representing a bucket in the hash table.
     * Buckets can be:
     * - occupied (contains ID and Product)
     * - EMPTY_SINCE_START
     * - EMPTY_AFTER_REMOVAL
     */
    private static class Entry {
        String iD;
        Product product;

        /** Marker bucket for never-used bucket. */
        public static final Entry EMPTY_SINCE_START = new Entry(null, null);

        /** Marker bucket for a removed bucket. */
        public static final Entry EMPTY_AFTER_REMOVAL = new Entry(null, null);

        /**
         * Creates a new bucket entry storing an ID and product.
         *
         * @param iD product ID
         * @param product product object
         */
        public Entry(String iD, Product product) {
            this.iD = iD;
            this.product = product;
        }

        /**
         * @return true if this bucket is either empty type
         */
        public boolean isEmpty() {
            return this == EMPTY_SINCE_START || this == EMPTY_AFTER_REMOVAL;
        }

        /**
         * @return true if bucket is empty due to removal
         */
        public boolean isEmptyAfterRemoval() {
            return this == EMPTY_AFTER_REMOVAL;
        }

        /**
         * @return true if bucket has never been used
         */
        public boolean isEmptySinceStart() {
            return this == EMPTY_SINCE_START;
        }
    }
}
