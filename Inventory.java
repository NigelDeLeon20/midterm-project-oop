import java.util.*;

class Inventory {

    // The list itself is private. Outside classes cannot touch it directly;
    // they must go through the public methods below.
    private final List<Item> items;
    private static final int lowStockIndicator = 5;
    private static final String[] VALID_CATEGORIES = {"Clothing", "Electronics", "Entertainment"};

    public Inventory() {
        items = new ArrayList<>();
    }

    // ---------- Helper: category validation ----------
    public boolean isValidCategory(String category) {
        for (String c : VALID_CATEGORIES) {
            if (c.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    public String[] getValidCategories() {
        return VALID_CATEGORIES;
    }

    // ---------- Helper: check duplicate ID ----------
    public boolean idExist(String id) {
        return findItemById(id) != null;
    }

    private Item findItemById(String id) {
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    // ---------- ADD ITEM ----------
    public boolean addItem(String category, String id, String name, int quantity, double price) {
        if (!isValidCategory(category)) {
            return false; 
        }
        Item newItem;
        switch (category.toLowerCase()) {
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

    // ---------- UPDATE ITEM ----------
    // Returns an Object[] { success(boolean), oldValue, newValue, item }
    public Item getItemForUpdate(String id) {
        return findItemById(id);
    }

    public void updateQuantity(Item item, int newQuantity) {
        item.setQuantity(newQuantity);
    }

    public void updatePrice(Item item, double newPrice) {
        item.setPrice(newPrice);
    }

    // ---------- REMOVE ITEM ----------
    public Item removeItem(String id) {
        Item item = findItemById(id);
        if (item != null) {
            items.remove(item);
        }
        return item;
    }

    // ---------- SEARCH ITEM ----------
    public Item searchItem(String id) {
        return findItemById(id);
    }

    // ---------- DISPLAY BY CATEGORY ----------
    public List<Item> getItemsCategory(String category) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                result.add(item);
            }
        }
        return result;
    }

    // ---------- DISPLAY ALL ----------
    public List<Item> getALLItems() {
        return new ArrayList<>(items); // returns a copy -> protects internal list
    }

    // ---------- SORT ----------
    public List<Item> sortItems(String sortBy, String order) {
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

    // ---------- LOW STOCK ----------
    public List<Item> getLowStockItems() {
        List<Item> lowStock = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() <= lowStockIndicator) {
                lowStock.add(item);
            }
        }
        return lowStock;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

