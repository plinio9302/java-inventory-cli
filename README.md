[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/W6QCYcn8)
Plinio Durango and I'm aiming for an 'A'  ID: 208000410

## How to Run the Program

1. Make sure all the `.java` files (`Product.java`, `FileProcessor.java`, `HashTable.java`, `BST.java`, `ProductManager.java`, and `ShoppingCart.java`) are in the same folder (e.g., your `src/` directory).
2. Compile the program: `javac *.java`
3. Run the main program: `java ProductManager product.csv`

## Program Behavior

The application starts in CLI mode and displays a numbered menu. You can:

- Load products from a CSV file
- Insert, search, and delete products
- List products sorted by price (BST in-order traversal)
- Filter products by price range, category, and tags
- Use set operations: intersection (AND), difference (NOT), and union (OR) on tags
- Manage a shopping cart (add, remove, search, total, clear)
- **Apply a percentage discount to the cart total** *(A-grade feature)*
- **Export the cart to a text file** *(A-grade feature)*
- **View inventory statistics** (avg price, most expensive/cheapest per category) *(A-grade feature)*

Simply type the menu number and press Enter.

---

## AI Use

Link: https://chatgpt.com/share/6937afb6-5768-8011-b2e4-bca7be3e766c

Used for:
1. Help structuring the BST insertion and in-order traversal logic
2. Assistance with Java Set operations (intersection, union, difference using `retainAll`, `removeAll`, `addAll`)
3. Code review and Javadoc formatting
4. Designing the `applyDiscount()` and `exportToFile()` A-grade features

---


## Specifications for a C (70)

**Filtering:**

```
Find products matching multiple tags using set intersection ("electronics" AND "on-sale")  ✅
Find products in one category but not another using set difference ("clothing" NOT "clearance") ✅
```

**Shopping Cart:**

```
Implement a shopping cart using a set to prevent duplicate items   ✅
Add items to cart by product ID                                    ✅
Display all items currently in cart                                ✅
Calculate total cart price                                         ✅
```

**Code Quality:**

```
Consistent Javadoc documentation of new features                   ✅
At least 20 sample products in your test dataset                   ✅
```

---

## Specifications for a B (80)

**Enhanced Filtering:**

```
Find products matching any of several tags using set union ("wireless" OR "bluetooth")     ✅
Combine price range filtering with tag filtering ("electronics" between $100-$500)         ✅
```

**Shopping Cart Enhancement:**

```
Remove items from cart                                             ✅
Check if a specific item is in the cart                            ✅
Clear entire cart                                                  ✅
```

**Code Quality:**

```
Comprehensive testing of all feature combinations                  ✅
Well-structured code with minimal redundancy                       ✅
Professional-quality comments and documentation                    ✅
```

---

## Specifications for an A (90+)

**Cart Discount & Export:**

```
applyDiscount(double pct): applies % discount, prints breakdown    ✅
exportToFile(String path): exports cart contents to a .txt file    ✅
getItemCount(): returns number of items currently in cart          ✅
Cart display now shows item count header                           ✅
```

**Code Quality Improvements:**

```
Fixed typos: "Obeject" -> object, "succesfully" -> successfully     ✅
Fixed typos in README: "Duarango" -> Durango, "aming" -> aiming     ✅
All methods have full @param / @return Javadoc                     ✅
Author and version tags added to class-level Javadoc               ✅
remove() uses break after finding match (avoids ConcurrentModificationException) ✅
Consistent null-safe checks and clear error messages throughout     ✅
```
