package app.dto;

import app.entities.OrderDetail;

import java.util.Objects;


public class OrderDetailsMaterialLengthDTO {

    String materialName;
    int materialLength;
    int quantity;
    String materialUnit;
    String materialDescription;

    public OrderDetailsMaterialLengthDTO(String materialName, int materialLength, int quantity, String materialUnit, String materialDescription) {
        this.materialName = materialName;
        this.materialLength = materialLength;
        this.quantity = quantity;
        this.materialUnit = materialUnit;
        this.materialDescription = materialDescription;
    }

    public String getMaterialName() {
        return materialName;
    }

    public int getMaterialLength() {
        return materialLength;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailsMaterialLengthDTO that)) return false;
        return getMaterialLength() == that.getMaterialLength() && getQuantity() == that.getQuantity() && Objects.equals(getMaterialName(), that.getMaterialName()) && Objects.equals(getMaterialUnit(), that.getMaterialUnit()) && Objects.equals(getMaterialDescription(), that.getMaterialDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialName(), getMaterialLength(), getQuantity(), getMaterialUnit(), getMaterialDescription());
    }
}
