import java.util.*;

class Inventory {

    // The list itself is private. Outside classes cannot touch it directly;
    // they must go through the public methods below.
    private final List<Item> items;
    private static final int LOW_STOCK_INDICATOR = 10;
    private static final String[] VALID_CATEGORIES = {"Clothing", "Electronics", "Entertainment"};

    public Inventory() {
        items = new ArrayList<>();
    }

    // validation for category
    public boolean isValidCategory(String category) {
        if (category == null) {
            return false;
        }
        for (String c : VALID_CATEGORIES) {
            if (c.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    public String[] getValidCategories() {
        return Arrays.copyOf(VALID_CATEGORIES, VALID_CATEGORIES.length);
    }

    // Check if id has duplicates
    public boolean idExist(String id) {
        return findItemById(id) != null;
    }

    private Item findItemById(String id) {
        if (id == null) {
            return null;
        }
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    // Adding Item
    public boolean addItem(String category, String id, String name, int quantity, double price) {
        if (!isValidCategory(category) || id == null || !id.matches("^[A-Za-z]{3}-\\d{4}$")
                || idExist(id) || name == null || name.trim().isEmpty()
                || quantity < 1 || quantity > 1000 || !Double.isFinite(price) || price < 1 || price > 20000) {
            return false; 
        }
        Item newItem;
        switch (category.toLowerCase(Locale.ROOT)) {
            case "clothing":
                newItem = new Item.Clothing(id, name, quantity, price);
                break;
            case "electronics":
                newItem = new Item.Electronics(id, name, quantity, price);
                break;
            case "entertainment":
                newItem = new Item.Entertainment(id, name, quantity, price);
                break;
            default:
                return false;
        }
        items.add(newItem);
        return true;
    }

    // Update item
    public Item getItemForUpdate(String id) {
        return findItemById(id);
    }

    public void updateQuantity(Item item, int newQuantity) {
        if (item == null) {
            throw new IllegalArgumentException("ERROR: Item cannot be empty.");
        }
        item.setQuantity(newQuantity);
    }

    public void updatePrice(Item item, double newPrice) {
        if (item == null) {
            throw new IllegalArgumentException("ERROR: Item cannot be empty.");
        }
        item.setPrice(newPrice);
    }

    // deleting item
    public Item removeItem(String id) {
        Item item = findItemById(id);
        if (item != null) {
            items.remove(item);
        }
        return item;
    }

    // Searching for an item using ID
    public Item searchItem(String id) {
        return findItemById(id);
    }

    // Display items by category
    public List<Item> getItemsCategory(String category) {
        List<Item> result = new ArrayList<>();
        if (category == null) {
            return result;
        }
        for (Item item : items) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                result.add(item);
            }
        }
        return result;
    }

    // Display all items
    public List<Item> getALLItems() {
        return new ArrayList<>(items); // returns a copy -> protects internal list
    }

    // Sorting items by quanity or price and in ascending or descending order
    public List<Item> sortItems(String sortBy, String order) {
        if (sortBy == null || (!sortBy.equalsIgnoreCase("quantity") && !sortBy.equalsIgnoreCase("price"))) {
            throw new IllegalArgumentException("ERROR: Sort field must be quantity or price.");
        }
        if (order == null || (!order.equalsIgnoreCase("ascending") && !order.equalsIgnoreCase("descending"))) {
            throw new IllegalArgumentException("ERROR: Sort order must be ascending or descending.");
        }
        List<Item> sorted = new ArrayList<>(items);

        sorted.sort((a, b) -> {
            int cmp;
            if (sortBy.equalsIgnoreCase("quantity")) {
                cmp = Integer.compare(a.getQuantity(), b.getQuantity());
            } else { // price
                cmp = Double.compare(a.getPrice(), b.getPrice());
            }
            return order.equalsIgnoreCase("descending") ? -cmp : cmp;
        });

        return sorted;
    }

    //If item is low in stock
    public List<Item> getLowStockItems() {
        List<Item> lowStock = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() < LOW_STOCK_INDICATOR) {
                lowStock.add(item);
            }
        }
        return lowStock;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

