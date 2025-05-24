package app.entities;

public class MaterialVariant {
private int length;
private Material material;
private int lengthId;


    public MaterialVariant(int lengthId, int length, Material material) {
        this.lengthId = lengthId;
        this.length = length;
        this.material = material;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getLengthId() {
        return lengthId;
    }

    public void setLengthId(int lengthId) {
        this.lengthId = lengthId;
    }

    @Override
    public String toString() {
        return "MaterialVariant{" +
                "length=" + length +
                ", material=" + material +
                ", lengthId=" + lengthId +
                '}';
    }
}
