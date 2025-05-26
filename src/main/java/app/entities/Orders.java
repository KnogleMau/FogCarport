package app.entities;

public class Orders {
    private int orderId;
    private int customerId;
    private int requestId;
    private double totalPrice;
    private String orderStatus;

    public Orders(int orderId, int customerId, int requestId, double totalPrice, String orderStatus){
        this.orderId = orderId;
        this.customerId = customerId;
        this.requestId = requestId;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
