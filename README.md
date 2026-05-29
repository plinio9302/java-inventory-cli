# Java Inventory Management System

A command-line inventory management application written in Java. It uses a custom hash table for O(1) product lookups, a binary search tree (BST) for price-ordered traversal, and Java Sets for flexible tag-based filtering. Products are loaded from a CSV file, and a built-in shopping cart lets users assemble orders, apply discounts, and export receipts.

---

## Features

- **Product Management** — Add, search, and remove products by ID across all internal data structures simultaneously (hash table, BST, and master set).
- **Price-Sorted Listing** — Display all products in ascending price order via BST in-order traversal.
- **Price Range Filtering** — Retrieve all products whose price falls within a user-defined minimum/maximum range.
- **Tag-Based Filtering** — Find products by a single tag, match products containing ALL of a set of tags (intersection), or match products containing ANY of a set of tags (union).
- **Category & Tag Difference** — Find products in a given category that do NOT carry a specified tag (set difference).
- **Combined Filtering** — Filter by tag first, then apply a price range on the resulting subset.
- **Shopping Cart** — Add/remove products by ID, view cart contents with item count, compute total price, apply a percentage discount, search for a specific item, clear the cart, and export the cart to a text file.
- **Inventory Statistics** — View the most and least expensive products in the catalog.
- **CSV Data Loading** — Products are loaded from a structured CSV file at startup, with validation and error handling for malformed lines.

---

## Data Structures

| Structure | Purpose |
|---|---|
| `HashTable` (custom) | O(1) average-case insert, lookup, and delete by product ID using open addressing with linear probing and dynamic resizing at load factor 0.6 |
| `BST` (custom) | Price-ordered storage; supports in-order traversal, range queries, and min/max retrieval |
| `HashSet<Product>` | Master product collection used for tag/category set operations |
| `HashSet<Product>` (cart) | Shopping cart storage; prevents duplicate items |

---

## Project Structure

```
.
├── Product.java          # Product model (ID, name, price, category, stock, tags)
├── HashTable.java        # Custom hash table with linear probing
├── BST.java              # Custom binary search tree ordered by price
├── FileProcessor.java    # CSV parser — loads products at startup
├── ShoppingCart.java     # Shopping cart with discount and export features
├── ProductManager.java   # Main entry point and interactive CLI menu
└── product.csv           # Sample dataset (25 products, 5 categories)
```

---

## Getting Started

### Prerequisites

- Java 11 or higher
- A terminal / command prompt

### Compile

Place all `.java` files in the same directory, then run:

```bash
javac *.java
```

### Run

```bash
java ProductManager product.csv
```

You can substitute `product.csv` with any CSV file that follows the expected format.

---

## CSV Format

The data file must have a header row followed by product rows:

```
product_id,name,price,category,stock,tags
ELEC001,Wireless Mouse,29.99,Electronics,45,wireless;bestseller;in-stock
```

- **Columns**: `product_id`, `name`, `price`, `category`, `stock`, `tags`
- **Tags**: semicolon-separated (`;`) within the last column

---

## Menu Overview

```
[1]  Add new product
[2]  Search product by ID
[3]  Display products sorted by price
[4]  Remove a product by ID
[5]  Find products within a price range
[6]  Find the most and least expensive products
[7]  Find products by a single tag
[8]  Find products matching ALL specified tags (intersection)
[9]  Find products in a category excluding a tag (set difference)
[10] Shopping Cart
[11] Find products matching ANY specified tag (union)
[12] Filter by tag and price range combined
[13] Quit
```

---

## License

This project is open source and available under the [MIT License](LICENSE).
