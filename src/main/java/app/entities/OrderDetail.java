package app.entities;

public class OrderDetail {
private int orderId;
private int materialId;
private int quantity;
private int lengthId;
private double price;

    public OrderDetail(int orderId, int materialId, int quantity, int lengthId, double price) {
        this.orderId = orderId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.lengthId = lengthId;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLengthId() {
        return lengthId;
    }

    public void setLengthId(int lengthId) {
        this.lengthId = lengthId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId=" + orderId +
                ", materialId=" + materialId +
                ", quantity=" + quantity +
                ", lengthId=" + lengthId +
                ", price=" + price +
                '}';
    }
}
