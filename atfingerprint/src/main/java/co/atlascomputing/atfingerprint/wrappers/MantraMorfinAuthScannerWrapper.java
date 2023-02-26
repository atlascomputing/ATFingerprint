package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.mantra.morfinauth.DeviceInfo;
import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.enums.DeviceModel;
import com.mantra.morfinauth.enums.ImageFormat;

public class MantraMorfinAuthScannerWrapper {
    private MorfinAuth morfinAuth;
    DeviceInfo deviceInfo;

    private final Context context;

    public MantraMorfinAuthScannerWrapper(Context applicationContext) {
        context = applicationContext;

    }

    public int init(byte[] clientKey) {

        // init MorfinAuth
        morfinAuth = new MorfinAuth(context, null);

        deviceInfo = new com.mantra.morfinauth.DeviceInfo();
        int error = -1;
        if (clientKey == null || clientKey.length == 0) {
            Log.d("AT", "init no key");
            error = morfinAuth.Init(DeviceModel.MFS500, deviceInfo);
        } else {
            Log.d("AT", "init with key");
            // clientKey is a base64 encoded string
            error = morfinAuth.Init(DeviceModel.MFS500, Base64.encodeToString(clientKey, 0), deviceInfo);
        }

        Log.d("AT", "init response: " + error);

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public byte[] captureImage(int timeout, int minQuality) {
        if (morfinAuth == null) {
            Log.d("ATFingerprint", "Fingerprint device should be initialized first");
            return null;
        }

        int qty[] = new int[1];
        int nfiq[] = new int[1];

        int error = morfinAuth.AutoCapture(minQuality, timeout, qty, nfiq);

        Log.d("AT", "AutoCapture response: " + error);
        Log.d("AT", "AutoCapture response: QUALITY: " + qty[0] + " NFIQ: " + nfiq[0]);

        //success
        if (error == 0) {
            morfinAuth.StopCapture();
//            byte[] image = null;
//            int[] imageLen = null;
////            morfinAuth.GetImage(image, imageLen, 0, ImageFormat.WSQ);
//            morfinAuth.GetImage(image, imageLen, 0, ImageFormat.RAW);


            // get captured image, w296,h354
            int Size = deviceInfo.Width * deviceInfo.Height + 1111;
            int[] iSize = new int[Size];
            byte[] bImage1 = new byte[Size];
            int ret = morfinAuth.GetImage(bImage1, iSize, 1, ImageFormat.RAW);
            byte[] bImage = new byte[iSize[0]];
            System.arraycopy(bImage1, 0, bImage, 0, iSize[0]);
            return bImage;
        }


        return null;
    }

    public int closeDevice() {
        if (morfinAuth == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }

        int error = morfinAuth.Uninit();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public int close() {
        if (morfinAuth == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }

        int error = morfinAuth.Uninit();
        morfinAuth.Dispose();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public static boolean isSupportedScanner(int vendorId, int productId) {
        if (vendorId != 11279 || productId != 4352 && productId != 4619 && productId != 4621) {
            return false;
        } else {

//            switch (productId) {
//                case 4352:
//                    PRODUCT_NAME = "MFS500";
//                    break;
//                case 4619:
//                    PRODUCT_NAME = "MELO31";
//                    break;
//                case 4621:
//                    PRODUCT_NAME = "MARC10";
//            }
            return true;
        }
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
