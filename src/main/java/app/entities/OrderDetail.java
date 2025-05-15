package app.entities;

public class OrderDetail {
private int orderId;
private Material material;
private int quantity;
private MaterialVariant materialVariant;
private double price;

    public OrderDetail(int orderId, Material material, int quantity, MaterialVariant materialVariant, double price) {
        this.orderId = orderId;
        this.material = material;
        this.quantity = quantity;
        this.materialVariant = materialVariant;
        this.price = price;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MaterialVariant getMaterialVariant() {
        return materialVariant;
    }

    public void setMaterialVariant(MaterialVariant materialVariant) {
        this.materialVariant = materialVariant;
    }

    public double getPrice() {
        return price * quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
