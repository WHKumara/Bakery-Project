package lk.ijse.finalproject.to;

public class RawMaterial {
    private String materialId;
    private String type;
    private int qty;
    private String supId;

    public RawMaterial() {
    }

    public RawMaterial(String materialId, String type, int qty, String supId) {
        this.materialId = materialId;
        this.type = type;
        this.qty = qty;
        this.supId = supId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    @Override
    public String toString() {
        return "RawMaterial{" +
                "materialId='" + materialId + '\'' +
                ", type='" + type + '\'' +
                ", qty=" + qty +
                ", supId='" + supId + '\'' +
                '}';
    }
}
