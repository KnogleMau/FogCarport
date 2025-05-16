package app.dto;

import java.util.Objects;

public class OrderCustomerDTO {

int orderID;
float totalPrice;
String orderStatus;
String firstName;
String lastName;
String phoneNumber;
String email;

    public OrderCustomerDTO(int orderID, float totalPrice, String orderStatus, String firstName, String lastName, String phoneNumber, String email) {

        this.orderID = orderID;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getOrderID() {
        return orderID;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhonenumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCustomerDTO that)) return false;
        return getOrderID() == that.getOrderID() && Float.compare(getTotalPrice(), that.getTotalPrice()) == 0 && Objects.equals(getOrderStatus(), that.getOrderStatus()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderID(), getTotalPrice(), getOrderStatus(), getFirstName(), getLastName(), phoneNumber, getEmail());
    }
}
