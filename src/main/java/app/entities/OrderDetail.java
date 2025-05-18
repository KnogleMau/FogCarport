package app.entities;

public class OrderDetail {
private int orderId;
private int materialId;
private int quantity;
private int materialLength;
private double price;

    public OrderDetail(int orderId, int materialId, int quantity, int materialLength, double price) {
        this.orderId = orderId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.materialLength = materialLength;
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

    public int getMaterialLength() {
        return materialLength;
    }

    public void setMaterialLength(int materialLength) {
        this.materialLength = materialLength;
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
                ", materialLength=" + materialLength +
                ", price=" + price +
                '}';
    }
}
