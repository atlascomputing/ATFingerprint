package co.atlascomputing.atfingerprint.interfaces;

import android.content.Context;
import android.hardware.usb.UsbManager;

import co.atlascomputing.atfingerprint.dto.DeviceInfo;

public interface GenericUsbDevice {


    public String getManufacturer();
    public String[] getSupporedDevices();

    // initialize device, and get device information, like imgWidth, imgHeight, dpi etc
    // if usb permission not given, it should be requested on this method
    // clientKey = null, if no LOCKING
    // Context = Application context
    public int init(Context inContext, UsbManager inUsbManager, byte[] clientKey, DeviceInfo outDeviceInfo);


    public int startCapture(int deviceId);
    public int closeDevice();
    public int close();
}
