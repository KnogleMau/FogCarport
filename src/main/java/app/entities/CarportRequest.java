package app.entities;

public class CarportRequest {
    int requestID;
    int requestLength;
    int requestWidth;
    int customerID;
    int requestHeight;

    public CarportRequest(int requestID, int requestLength, int requestWidth, int requestHeight, int customerID) {
        this.requestID = requestID;
        this.requestLength = requestLength;
        this.requestWidth = requestWidth;
        this.requestHeight = requestHeight;
        this.customerID = customerID;
    }

    public int getRequestID() {
        return requestID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getRequestHeight() {
        return requestHeight;
    }

    public int getRequestLength() {
        return requestLength;
    }

    public int getRequestWidth() {
        return requestWidth;
    }
}
