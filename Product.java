package checkApp.app;

import org.jetbrains.annotations.NotNull;

public class Product {
    private String id;
    private String name;
    private double price;
    private double quantity;

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
            this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        }
        else {
            System.err.println("Warning: price of product \"" + this.getName() + "\" was set as 0$.");
        }
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
        else {
            System.err.println("Warning: quantity of product \"" + this.getName() + "\" was set as 0$.");
        }
    }
}
