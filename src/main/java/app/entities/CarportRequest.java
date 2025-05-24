package app.entities;

public class CarportRequest {

    int requestID;
    int requestLength;
    int requestWidth;
    int requestHeight;
    int customerID;

    public CarportRequest(int requestID, int requestLength, int requestHeight, int requestWidth, int customerID) {
        this.requestID = requestID;
        this.requestLength = requestLength;
        this.requestHeight = requestHeight;
        this.requestWidth = requestWidth;
        this.customerID = customerID;
    }

    public int getRequestID() {
        return requestID;
    }

    public int getRequestLength() {
        return requestLength;
    }

    public int getRequestHeight() {
        return requestHeight;
    }

    public int getRequestWidth() {
        return requestWidth;
    }

    public int getCustomerID() {
        return customerID;
    }

}