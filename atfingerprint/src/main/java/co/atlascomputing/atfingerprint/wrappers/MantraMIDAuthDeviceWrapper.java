package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;
import android.util.Log;

import com.mantra.midfingerauth.DeviceInfo;
import com.mantra.midfingerauth.MIDFingerAuth;
import com.mantra.midfingerauth.MIDFingerAuth_Callback;
import com.mantra.midfingerauth.enums.DeviceDetection;
import com.mantra.midfingerauth.enums.DeviceModel;
import com.mantra.midfingerauth.enums.ImageFormat;

public class MantraMIDAuthDeviceWrapper {
    private MIDFingerAuth midFingerAuth;
    DeviceInfo deviceInfo;

    public int init(Context context) {
        Log.d("AT", "creating MorfinAuth");
        midFingerAuth = new MIDFingerAuth(context, null);
        Log.d("AT", "created MorfinAuth");

        deviceInfo = new com.mantra.midfingerauth.DeviceInfo();
        int error = -1;
        Log.d("AT", "init no key");
        error = midFingerAuth.Init(DeviceModel.MFS500, deviceInfo); // NO option to init with clientKey

        Log.d("AT", "init response: " + error);

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public byte[] captureImage(int timeout, int minQuality) {
        if (midFingerAuth == null) {
            Log.d("ATFingerprint", "Fingerprint device should be initialized first");
            return null;
        }

        int qty[] = new int[1];
        int nfiq[] = new int[1];

        int error = midFingerAuth.AutoCapture(minQuality, timeout, qty, nfiq);

        Log.d("AT", "AutoCapture response: " + error);
        Log.d("AT", "AutoCapture response: QUALITY: " + qty + " NFIQ: " + nfiq);

        //success
        if (error == 0) {
            midFingerAuth.StopCapture();
//            byte[] image = null;
//            int[] imageLen = null;
////            morfinAuth.GetImage(image, imageLen, 0, ImageFormat.WSQ);
//            morfinAuth.GetImage(image, imageLen, 0, ImageFormat.RAW);


            // get captured image, w296,h354
            int Size = deviceInfo.Width * deviceInfo.Height + 1111;
            int[] iSize = new int[Size];
            byte[] bImage1 = new byte[Size];
            int ret = midFingerAuth.GetImage(bImage1, iSize, 1, ImageFormat.RAW);
            byte[] bImage = new byte[iSize[0]];
            System.arraycopy(bImage1, 0, bImage, 0, iSize[0]);
            return bImage;
        }


        return null;
    }

    public int closeDevice() {
        if (midFingerAuth == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }

        int error = midFingerAuth.Uninit();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public int close() {
        if (midFingerAuth == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }

        int error = midFingerAuth.Uninit();
        midFingerAuth.Dispose();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public static boolean isSupportedDevice(int vendorId, int productId) {

        if (vendorId == 11279 && (productId == 4352 || productId == 4355 || productId == 4610 || productId == 4611)) {

//            switch (productId) {
//                case 4352:
//                    return DeviceModel.MFS500;
//                case 4355:
//                    return DeviceModel.MFS100V2;
//                case 4610:
//                    return DeviceModel.MAPRO_CX;
//                case 4611:
//                    return DeviceModel.MAPRO_OX;
//                default:
//                    return null;
//            }
            return true;
        }
        return false;

    }


//    @Override
//    public void OnDeviceDetection(String s, DeviceDetection deviceDetection) {
//
//        Log.d("AT", "OnDeviceDetection: " + deviceDetection.toString());
//    }
//
//    @Override
//    public void OnPreview(int i, int i1, byte[] bytes) {
//        Log.d("AT", "OnPreview");
//
//    }
//
//    @Override
//    public void OnComplete(int i, int i1, int i2) {
//        Log.d("AT", "OnComplete");
//    }
}
