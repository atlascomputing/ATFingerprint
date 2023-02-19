package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;
import android.util.Log;

import com.mantra.mfs100.DeviceInfo;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;
//import com.mantra.mfs100.enums.DeviceModel;
//import com.mantra.mfs100.enums.ImageFormat;

public class MantraMFS100DeviceWrapper {
    private MFS100 mfs100;
    DeviceInfo deviceInfo;

    private MFS100EventHandler eventHandler;

    // Init for mfs100 is using callbacks, call init on OnDeviceAttached,
    public int init(Context context, String clientKey) {
        Log.d("AT", "creating mfs100");

        this.eventHandler = new MFS100EventHandler();

        mfs100 = new MFS100(this.eventHandler);
        mfs100.SetApplicationContext(context);

        Log.d("AT", "created mfs100");

        int error = -1;
//        if (clientKey == null || clientKey.isEmpty()) {
//            Log.d("AT", "init no key");
//            error = mfs100.Init();
//        } else {
//            Log.d("AT", "init with key");
//            error = mfs100.Init( clientKey);
//        }
//
//        Log.d("AT", "init response: " + error);
//
        //success
        if (error == 0) {
            deviceInfo = mfs100.GetDeviceInfo();

            return 0;
        }

        return -1;
    }

    public byte[] captureImage(int timeout, boolean fastDetection) {
        if (mfs100 == null) {
            Log.d("ATFingerprint", "Fingerprint device should be initialized first");
            return null;
        }

//        int qty[] = new int[1];
//        int nfiq[] = new int[1];

//        int error = mfs100.AutoCapture(minQuality, timeout, qty, nfiq);

        FingerData fingerData = new FingerData();
        int error = mfs100.AutoCapture(fingerData, timeout, fastDetection);

        Log.d("AT", "AutoCapture response: " + error);
        Log.d("AT", "AutoCapture response: QUALITY: " + fingerData.Quality() + " NFIQ: " + fingerData.Nfiq());

        //success
        if (error == 0) {
            mfs100.StopAutoCapture();

            return fingerData.RawData();


//            // get captured image, w296,h354
//            int Size = deviceInfo.Width * deviceInfo.Height + 1111;
//            int[] iSize = new int[Size];
//            byte[] bImage1 = new byte[Size];
//            int ret = mfs100.GetImage(bImage1, iSize, 1, ImageFormat.RAW);
//            byte[] bImage = new byte[iSize[0]];
//            System.arraycopy(bImage1, 0, bImage, 0, iSize[0]);
//            return bImage;
        }


        return null;
    }

    public int closeDevice() {
        if (mfs100 == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }

        int error = mfs100.UnInit();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public int close() {
        if (mfs100 == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }

        int error = mfs100.UnInit();
        mfs100.Dispose();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public static boolean isSupportedDevice(int vendorId, int productId) {

        if ((vendorId == 1204 || vendorId == 11279) && (productId == 34323 || productId == 4101 || productId == 4102)) {
            return true;
        }

        return false;
    }

    private class MFS100EventHandler implements MFS100Event {

        @Override
        public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
            Log.d("AT", "mfs100 OnDeviceAttached");


            int ret;
            if (!hasPermission) {
                Log.d("AT", "Permission denied");

                return;
            }
            try {
                if (vid == 1204 || vid == 11279) {
                    if (pid == 34323) {
                        ret = mfs100.LoadFirmware();
                        if (ret != 0) {
                            Log.d("AT", "Load firmware failed: " + mfs100.GetErrorMsg(ret));

                        } else {
                            Log.d("AT", "Load firmware success");

                        }
                    } else if (pid == 4101) {
                        String key = "Without Key";
                        ret = mfs100.Init();
                        if (ret == 0) {

                            Log.d("AT", "Init Success without key");
                            deviceInfo = mfs100.GetDeviceInfo();

                        } else {
                            Log.d("AT", "Init Success without key failed: " + mfs100.GetErrorMsg(ret));

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void OnDeviceDetached() {
            Log.d("AT", "mfs100 OnDeviceDetached");

        }

        @Override
        public void OnHostCheckFailed(String s) {
            Log.d("AT", "mfs100 OnHostCheckFailed");

        }
    }
}
