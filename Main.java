import java.util.*;

public class Main {
    private static final Scanner s = new Scanner(System.in);

    private static final Inventory inventory = new Inventory();
    private static final String ID_FORMAT = "^[A-Za-z]{3}-\\d{4}$";

    private static int readMenuChoice(int min, int max){
        boolean a = true;
        while(a){
            String input = s.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max){
                    return value;
                } else {
                    System.out.print("ERROR: Invalid input. please input a number (1-9) ");
                } 
            } catch (NumberFormatException e){
                System.out.println("ERROR: Invalid input. please input a number (1-9) ");
            }
        } 
        throw new IllegalStateException("Menu input loop ended unexpectedly.");
    }

    //Validate if string is not empty
    private static String readNonEmptyString(String prompt) {
        boolean a = true;
        while (a) {
            System.out.print(prompt);
            String input = s.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
        throw new IllegalStateException("Text input loop ended unexpectedly.");
    }

    private static int readValidInt(String prompt){
        final boolean a = true;
        while(a){
            System.out.print(prompt);
            String input = s.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if(value < 1 || value > 1000){
                    System.out.println("ERROR: Quantity must be 1-1000. Please try again.");
                    continue;
                }
                return value;

            } catch (NumberFormatException e){
                System.out.println("ERROR: Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double readValidatedDouble(String prompt) {
        boolean a = true;
        while (a) {
            System.out.print(prompt);
            String input = s.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (!Double.isFinite(value) || value < 1 || value > 20000) {
                    System.out.println("Price must be a valid number from 1 to 20,000. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        throw new IllegalStateException("Price input loop ended unexpectedly.");
    }

    private static boolean confirmChange(String prompt) {
        boolean a = true;
        while (a) {
            String answer = readNonEmptyString(prompt + " (yes/no): ");
            if (answer.equalsIgnoreCase("yes")) {
                return true;
            }
            if (answer.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("ERROR: Please enter yes or no.");
        }
        throw new IllegalStateException("Confirmation loop ended unexpectedly.");
    }

    private static String readValidCategory(String prompt) {
        boolean a = true;
        while (a) {
            String category = readNonEmptyString(prompt);
            if (inventory.isValidCategory(category)) {
                return category;
            }
            System.out.println("ERROR: Category is not available. Please try again.");
        }
        throw new IllegalStateException("Category input loop ended unexpectedly.");
    }

    private static String readUniqueItemId(String prompt) {
        boolean a = true;
        while (a) {
            String id = readNonEmptyString(prompt).toUpperCase(Locale.ROOT);
            if (!id.matches(ID_FORMAT)) {
                System.out.println("ERROR: Item ID must use the format ABC-1234. Please try again.");
                continue;
            }
            if (!inventory.idExist(id)) {
                return id;
            }
            System.out.println("ERROR: Item ID already exists. Please try again.");
        }
        throw new IllegalStateException("Item ID input loop ended unexpectedly.");
    }

    private static String readExistingItemId(String prompt) {
        boolean a = true;
        while (a) {
            String id = readNonEmptyString(prompt).toUpperCase(Locale.ROOT);
            if (!id.matches(ID_FORMAT)) {
                System.out.println("ERROR: Item ID must use the format ABC-1234. Please try again.");
                continue;
            }
            if (inventory.idExist(id)) {
                return id;
            }
            System.out.println("ERROR: Item not found. Please try again.");
        }
        throw new IllegalStateException("Item ID input loop ended unexpectedly.");
    }


    private static void printMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("INVENTORY MANAGEMENT SYSTEM");
        System.out.println("--------------------------------");
        System.out.print("1. Add Item \n2. Update Item \n3.Remove Item \n4.Display Items \n5.Display All Items \n6.Search Item \n7.Sort Item \n8.Display Low Stock \n9.Exit \nEnter input: ");
    }

    private static void addItem(){
        System.out.println("\n--------------------------------");
        System.out.println("            ADD ITEM");
        System.out.println("--------------------------------");

        System.out.println("Categories: \n" + String.join("\n,", inventory.getValidCategories()));
        String category = readValidCategory("Enter category: ");
        String id = readUniqueItemId("Enter item ID: ");

        String name = readNonEmptyString("Enter item name: ");
        int quantity = readValidInt("Enter item quantity: ");
        double price = readValidatedDouble("Enter item price: ");

        boolean success = inventory.addItem(category, id, name, quantity, price);
        if(success){
            System.out.println("Item added successfully!");
        } else {
            System.out.println("ERROR: Category is not available.");
        }
    }

    //Update item information
    private static void updateItem(){
        System.out.println("\n--------------------------------");
        System.out.println("          UPDATE ITEM");
        System.out.println("--------------------------------");

        String id = readExistingItemId("Enter item ID to update: ");
        Item item = inventory.getItemForUpdate(id);

        String field = "";
        boolean a = true;

        while(a){
            field = readNonEmptyString("Update item (quantity/price): ");
            if (field.equalsIgnoreCase("quantity") || field.equalsIgnoreCase("price")){
                break;
            } 
            System.out.println("ERROR: Invalid field. Please enter 'quantity' or 'price'.");
        }

        if (field.equalsIgnoreCase("quantity")){
            int oldValue = item.getQuantity();
            int newValue = readValidInt("Enter new quantity: ");

            if (!confirmChange("Confirm changing quantity from " + oldValue + " to " + newValue + "?")) {
                System.out.println("Quantity update cancelled.");
                return;
            }

            if (newValue == oldValue) {
                System.out.println("Quantity is already set as " + oldValue + "!");
                return;
            }

            inventory.updateQuantity(item, newValue);
            System.out.println("Quantity of Item " + item.getName() + " is updated from "
                    + oldValue + " to " + newValue);
        } else {
            double oldValue = item.getPrice();
            double newValue = readValidatedDouble("Enter new price: ");

            if (!confirmChange("Confirm changing price from " + oldValue + " to " + newValue + "?")) {
                System.out.println("Price update cancelled.");
                return;
            }

            if (Double.compare(newValue, oldValue) == 0) {
                System.out.println("Price is already set as " + oldValue + "!");
                return;
            }

            inventory.updatePrice(item, newValue);
            System.out.println("Price of Item " + item.getName() + " is updated from "
                    + oldValue + " to " + newValue);
        }
    }

    // Delete item
    private static void removeItem(){
        System.out.println("\n--------------------------------");
        System.out.println("          REMOVE ITEM");
        System.out.println("--------------------------------");

        String id = readExistingItemId("Enter item ID: ");
        Item item = inventory.removeItem(id);
        System.out.println("Item " + item.getName() + " has been removed from the inventory");
    }

    //Displaying items by category
    private static void displayByCategory() {
        System.out.println("\n-- DISPLAY ITEMS BY CATEGORY --");
        String category = readValidCategory("Enter Category: ");

        List<Item> list = inventory.getItemsCategory(category);
        if (list.isEmpty()) {
            System.out.println("Category " + category + " does not exist!");
            return;
        }

        printTableHeaderNoCategory();
        for (Item item : list) {
            System.out.println(item.noCategoryTable());
        }
        printTableFooterNoCategory();
    }

    //Display all items
    private static void displayAllItems() {
        System.out.println("\n-- ALL ITEMS --");
        List<Item> list = inventory.getALLItems();

        if (list.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        printTableHeaderWithCategory();
        for (Item item : list) {
            System.out.println(item.withCategoryTable());
        }
        printTableFooterWithCategory();
    }

    //Searching for an Item
    private static void searchItem() {
        System.out.println("\n-- SEARCH ITEM --");
        String id = readExistingItemId("Enter ID: ");
        Item item = inventory.searchItem(id);

        printItemReceipt(item);
    }

    private static void printItemReceipt(Item item) {
        double totalValue = item.getQuantity() * item.getPrice();

        System.out.println("\n================================");
        System.out.println("          ITEM RECEIPT");
        System.out.println("================================");
        System.out.println("Item ID:     " + item.getId());
        System.out.println("Item Name:   " + item.getName());
        System.out.println("Category:    " + item.getCategory());
        System.out.println("Quantity:    " + item.getQuantity());
        System.out.printf("Unit Price:  %.2f%n", item.getPrice());
        System.out.println("--------------------------------");
        System.out.printf("Total Value: %.2f%n", totalValue);
        System.out.println("================================");
    }

    //Item sorter
    private static void sortItem() {
        System.out.println("\n-- SORT ITEMS --");

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty. Nothing to sort.");
            return;
        }

        System.out.println("Sort all items by:");
        System.out.println("1. Quantity");
        System.out.println("2. Price");
        System.out.print("Enter choice: ");
        int sortChoice = readMenuChoice(1, 2);
        String sortBy = sortChoice == 1 ? "quantity" : "price";

        System.out.println("Sort order:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Enter choice: ");
        int orderChoice = readMenuChoice(1, 2);
        String order = orderChoice == 1 ? "ascending" : "descending";

        List<Item> sorted = inventory.sortItems(sortBy, order);

        printTableHeaderWithCategory();
        for (Item item : sorted) {
            System.out.println(item.withCategoryTable());
        }
        printTableFooterWithCategory();
    }

    //Displaying low stock items
    private static void displayLowStock() {
        System.out.println("\n-- LOW STOCK ITEMS (Items less than 10) --");
        List<Item> lowStock = inventory.getLowStockItems();

        if (lowStock.isEmpty()) {
            System.out.println("No low stock items.");
            return;
        }

        printTableHeaderWithCategory();
        for (Item item : lowStock) {
            System.out.println(item.withCategoryTable());       
        }
        printTableFooterWithCategory();
    }

    //Table format
    private static void printTableHeaderNoCategory() {
        System.out.println("+----------+--------------------+----------+------------+");
        System.out.printf("| %-8s | %-18s | %-8s | %-10s |%n", "ID", "Name", "Quantity", "Price");
        System.out.println("+----------+--------------------+----------+------------+");
    }

    private static void printTableFooterNoCategory() {
        System.out.println("+----------+--------------------+----------+------------+");
    }

    private static void printTableHeaderWithCategory() {
        System.out.println("+----------+--------------------+----------+------------+---------------+");
        System.out.printf("| %-8s | %-18s | %-8s | %-10s | %-13s |%n", "ID", "Name", "Quantity", "Price", "Category");
        System.out.println("+----------+--------------------+----------+------------+---------------+");
    }

    private static void printTableFooterWithCategory() {
        System.out.println("+----------+--------------------+----------+------------+---------------+");
    }

    private static void exampleData() {
        inventory.addItem("Clothing", "ABC-1234", "T-Shirt", 20, 199.99);
        inventory.addItem("Clothing", "ABC-1235", "Jacket", 4, 899.50);
        inventory.addItem("Electronics", "ABC-1236", "Wireless Mouse", 15, 349.00);
        inventory.addItem("Electronics", "ABC-1237", "USB-C Cable", 3, 99.75);
        inventory.addItem("Entertainment", "ABC-1238", "Board Game", 8, 599.00);
        inventory.addItem("Entertainment", "ABC-1239", "Puzzle Set", 2, 249.00);
    }

    public static void main(String[] args){
        exampleData();

        boolean a = true;

        while(a){
            printMenu();
            int choice = readMenuChoice(1, 9);
            switch(choice){
                case 1:
                    addItem();
                    break;
                case 2:
                    updateItem();
                    break;
                case 3:
                    removeItem();
                    break;
                case 4:
                    displayByCategory();
                    break;
                case 5:
                    displayAllItems();
                    break;
                case 6:
                    searchItem();
                    break;
                case 7:
                    sortItem();
                    break;
                case 8:
                    displayLowStock();
                    break;
                case 9:
                    System.out.println("Thank you for using the Inventory Management System!");
                    a = false;
                    break;
                default:
                    System.out.print("ERROR: Please try again.");
                    break;
            }
         }
    }


}
