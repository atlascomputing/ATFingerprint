package co.atlascomputing.atfingerprint.interfaces;

import co.atlascomputing.atfingerprint.dto.DeviceInfo;
import co.atlascomputing.atfingerprint.dto.DeviceModel;

public interface GenericUsbScanner {


//    public String getManufacturer();
//    public String[] getSupporedDevices();

//    public boolean isSupportedDevice(int vendorId, int productId);
    public boolean isSupportedScanner(int vendorId, int productId, String productName, String manufacturerName);

    // initialize device, and get device information, like imgWidth, imgHeight, dpi etc
    // if usb permission not given, it should be requested on this method
    // clientKey = null, if no LOCKING
    // Context = Application context
    public int init(DeviceModel inDeviceModel, byte[] inClientKey, DeviceInfo outDeviceInfo);


    // start catprue from specific device model
    public byte[] startCapture(DeviceModel inDeviceModel, int timeout, int minQuality);
    public int closeDevice(DeviceModel inDeviceModel);
    public int close(DeviceModel inDeviceModel);
}
