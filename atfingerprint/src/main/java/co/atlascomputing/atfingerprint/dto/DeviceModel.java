package co.atlascomputing.atfingerprint.dto;

import java.util.HashMap;
import java.util.Map;

//public enum DeviceModel {
//    //######### Mantra Morfin
//    MFS500("MFS500"),
////    MELO31("MELO31"),
//    MARC10("MARC10"),
//
//    //#########  Mantra MID Finger
////    MFS500("MFS500"),
//    MFS100V2("MFS100 V2"),
//    MAPRO_CX("MAPRO CX"),
//    MAPRO_OX("MAPRO OX"),
//    MELO31("MELO31"),
//
//    //#########  Mantra MFS100
//    MFS100("MFS100");
//
//
//    private String deviceModel;
//    private static final Map<String, DeviceModel> map = new HashMap();
//
//    private DeviceModel(String deviceModel) {
//        this.deviceModel = deviceModel;
//    }
//
//    public String getDeviceName() {
//        return this.deviceModel;
//    }
//
//    public static DeviceModel valueFor(String name) {
//        return (DeviceModel)map.get(name);
//    }
//
//    static {
//        DeviceModel[] var0 = values();
//        int var1 = var0.length;
//
//        for(int var2 = 0; var2 < var1; ++var2) {
//            DeviceModel en = var0[var2];
//            map.put(en.deviceModel, en);
//        }
//
//    }
//}

public class DeviceModel {
    private int vendorId = 0;
    private int productId = 0;
    private String productName = "";
    private String manufacturerName = "";

    public DeviceModel(int vendorId, int productId, String productName, String manufacturerName) {

        this.vendorId = vendorId;
        this.productId = productId;
        this.productName = productName;
        this.manufacturerName = manufacturerName;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
}