package co.atlascomputing.atfingerprint;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;

public class DummyDevice {
    private JSGFPLib sgfplib;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private void requestUsbPermission(Context context, UsbManager usbManager) {
        UsbDevice usbDevice = sgfplib.GetUsbDevice();
        boolean hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
        if (!hasPermission) {
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
            sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
        }
    }

    public int init(Context context, UsbManager usbManager) {


        sgfplib = new JSGFPLib(context, usbManager);

        long error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE){
            Log.d("ATFingerprint", "Fingerprint device init successful!");
            // request usb permission
            this.requestUsbPermission(context,usbManager);
            return 0;
        }
        Log.d("ATFingerprint", "Fingerprint device init failed!");
        return -1;
    }

    public int openDevice(int deviceId) {
        long error = sgfplib.OpenDevice(deviceId);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE){
            Log.d("ATFingerprint", "Fingerprint device opened successfully!");
            return 0;
        }
        return -1;
    }

    public byte[] captureRaw() {

        SGDeviceInfoParam device_info = new SGDeviceInfoParam();
        long error = sgfplib.GetDeviceInfo(device_info);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE)
        {
            // dimensions for the buffer
            int imgWidth = device_info.imageWidth;
            int imgHeight = device_info.imageHeight;

            byte[] buffer = new byte[imgWidth*imgHeight];
            long timeout = 10000;
            long quality = 50;

            if(sgfplib.GetImageEx(buffer, timeout,quality) == SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                // process image
                Log.d("ATFingerprint", "Fingerprint captured successfully!");
                return buffer;
            }
        }


        Log.d("ATFingerprint", "Fingerprint capture failed with code: " + error);

        // error comes here
        return null;
    }

    public byte[] captureRaw(long timeout , long quality) {

        SGDeviceInfoParam device_info = new SGDeviceInfoParam();
        long error = sgfplib.GetDeviceInfo(device_info);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE)
        {
            // dimensions for the buffer
            int imgWidth = device_info.imageWidth;
            int imgHeight = device_info.imageHeight;

            byte[] buffer = new byte[imgWidth*imgHeight];
            if(sgfplib.GetImageEx(buffer, timeout,quality) == SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                // process image
                Log.d("ATFingerprint", "Fingerprint captured successfully!");
                return buffer;
            }
        }


        Log.d("ATFingerprint", "Fingerprint capture failed with code: " + error);

        // error comes here
        return null;
    }

    public byte[] captureSimple() {

        SGDeviceInfoParam device_info = new SGDeviceInfoParam();
        long error = sgfplib.GetDeviceInfo(device_info);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE)
        {
            // dimensions for the buffer
            int imgWidth = device_info.imageWidth;
            int imgHeight = device_info.imageHeight;

            byte[] buffer = new byte[imgWidth*imgHeight];
            if(sgfplib.GetImage(buffer) == SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                // process image
                Log.d("ATFingerprint", "Fingerprint captured successfully!");
                return buffer;
            }
        }

        Log.d("ATFingerprint", "Fingerprint capture failed with code: " + error);

        // error comes here
        return null;
    }

    // close the device. to reuse the device, openDevice is required, no need for init
    public int closeDevice() {
        long error = sgfplib.CloseDevice();
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE){
            Log.d("ATFingerprint", "Fingerprint closeDevice successfully!");
            return 0;
        }
        Log.d("ATFingerprint", "Fingerprint closeDevice failed with code: " + error);
        return -1;
    }

    // close and release all library objects, to reuse the device. init will be required
    public int close() {
        long error = sgfplib.Close();
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE){
            Log.d("ATFingerprint", "Fingerprint closed successfully!");
            return 0;
        }

        Log.d("ATFingerprint", "Fingerprint close failed with code: " + error);
        return -1;
    }
}
