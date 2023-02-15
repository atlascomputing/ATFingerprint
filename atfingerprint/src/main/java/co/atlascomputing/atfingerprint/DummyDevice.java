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

        if(sgfplib == null){
            Log.d("ATFingerprint", "Fingerprint device should be initialized first");
            return -1;
        }

        long error = sgfplib.OpenDevice(deviceId);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE){
            Log.d("ATFingerprint", "Fingerprint device opened successfully!");
            return 0;
        }
        return -1;
    }

    public byte[] captureImage() {
        long timeout = 100000000; // in milliseconds
        long quality = 50; // 50 good for registration, 40 good for verification
        return captureImage(timeout,quality);
    }

    public byte[] captureImage(long timeout , long quality) {

        if(sgfplib == null){
            Log.d("ATFingerprint", "Fingerprint device should be initialized and opened first");
            return null;
        }

        SGDeviceInfoParam device_info = new SGDeviceInfoParam();
        long error = sgfplib.GetDeviceInfo(device_info);
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE)
        {
            // dimensions for the buffer
            int imgWidth = device_info.imageWidth;
            int imgHeight = device_info.imageHeight;

            byte[] buffer = new byte[imgWidth*imgHeight];

            sgfplib.SetLedOn(true);
            error = sgfplib.GetImageEx(buffer, timeout, quality);
            if(error == SGFDxErrorCode.SGFDX_ERROR_NONE)
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

    // check for quality is not done
    public byte[] captureImageSimple() {

        if(sgfplib == null){
            Log.d("ATFingerprint", "Fingerprint device should be initialized and opened first");
            return null;
        }

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

        if (sgfplib == null){
            Log.d("ATFingerprint", "Attempt  to close un initialized device");
            return -1;
        }

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

        if (sgfplib == null){
            Log.d("ATFingerprint", "Attempt  to close un initialized lib");
            return -1;
        }

        long error = sgfplib.Close();
        if (error == SGFDxErrorCode.SGFDX_ERROR_NONE){
            Log.d("ATFingerprint", "Fingerprint closed successfully!");
            return 0;
        }

        Log.d("ATFingerprint", "Fingerprint close failed with code: " + error);
        return -1;
    }
}
