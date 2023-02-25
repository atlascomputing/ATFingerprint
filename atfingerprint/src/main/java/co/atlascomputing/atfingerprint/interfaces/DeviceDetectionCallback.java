package co.atlascomputing.atfingerprint.interfaces;

import android.content.Context;
import android.content.Intent;

public interface DeviceDetectionCallback {

//    void OnDeviceDetection(String var1, DeviceDetection var2);
//    void onDeviceAttached;
//    void onDeviceDetached;

    // Context context, Intent intent
//    public void onUsbDeviceAttached(int vendorId, int productId, boolean hasPermission);
//    public void onUsbPermissionGranted(UsbDevice device, boolean hasPermission);
//    public void onUsbDeviceDetached(UsbDevice device);

    public void onUsbDeviceAttached(Context context, Intent intent);
    public void onUsbPermissionGranted(Context context, Intent intent);
    public void onUsbDeviceDetached(Context context, Intent intent);
}

