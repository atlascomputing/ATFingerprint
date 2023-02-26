package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;

import com.fpreader.fpdevice.UsbReader;

public class ChineseFPScannerWrapper {
    private UsbReader fpModule;

    private final Context context;

    public ChineseFPScannerWrapper(Context applicationContext) {
        context = applicationContext;

        // init fpModule
        fpModule = new UsbReader();

    }


    public int init() {

        fpModule.InitMatch();
//        fpModule.SetContextHandler(this,mHandler);


//        Log.d("ATFingerprint", "Fingerprint device init failed!");
        return -1;
    }


    public int closeDevice() {
        return -1;
    }

    public int close() {
        return -1;
    }

    public static boolean isSupportedScanner(int vendorId, int productId) {

        // vendorId = 1107, 8201, 8457, 1155. 30264 by two 8201 and 8457
        if (productId == 36869 || productId == 30264 || productId == 22304) {
            return true;
        }
        return false;
    }

}
