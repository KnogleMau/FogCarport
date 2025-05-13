package app.entities;

public class Product {
    private int id;
    private String name;
    private double price;
    private String variant;


    public Product(int id, String name,String variant, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.variant = variant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }
}
