public abstract class Item {
    final private String id;
    private String name;
    private int quantity;
    private double price;

    //Constructor for Item
    public Item(String id, String name, int quantity, double price) {
        if (id == null || !id.matches("^[A-Za-z]{3}-\\d{4}$")) {
            throw new IllegalArgumentException("ERROR: Item ID must use the format ABC-1234.");
        }
        this.id = id.toUpperCase(java.util.Locale.ROOT);
        setName(name);
        setQuantity(quantity);
        setPrice(price);
    }

    //getters for id, name, quantity, and price
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPrice() {
        return price;
    }

    //Setters and validations for name, quantity, and price
    public void setName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR: Name cannot be empty.");
        }
        this.name = name.trim();
    }
    public void setQuantity(int quantity){
        if (quantity < 1 || quantity > 1000) {
            throw new IllegalArgumentException("ERROR: Quantity must be from 1 - 1000.");
        }
        this.quantity = quantity;
    }
    public void setPrice(double price){
        if (!Double.isFinite(price) || price < 1 || price > 20000) {
            throw new IllegalArgumentException("ERROR: Price must be from 1 - 20,000.");
        }
        this.price = price;
    }

    public abstract String getCategory();

    public String noCategoryTable(){
        return String.format("| %-8s | %-18s | %-8d | %-10.2f |",
                getId(), getName(), getQuantity(), getPrice());
    }

    public String withCategoryTable(){
        return String.format("| %-8s | %-18s | %-8d | %-10.2f | %-13s |",
                getId(), getName(), getQuantity(), getPrice(), getCategory());
    }

    //Subclass for electronnics, entertainment, and clothing.
    static class Electronics extends Item{
        public Electronics(String id, String name, int quantity, double price) {
            super(id, name, quantity, price);
        }

        @Override
        public String getCategory() {
            return "Electronics";
        }
    }

    public static class Entertainment extends Item{
        public Entertainment(String id, String name, int quantity, double price){
            super(id, name, quantity, price);
        }

        @Override
        public String getCategory(){
            return "Entertainment";
        }
    }

    static class Clothing extends Item{
        public Clothing(String id, String name, int quantity, double price) {
            super(id, name, quantity, price);
        }

        @Override
        public String getCategory() {
            return "Clothing";
        }
    }



    

}
