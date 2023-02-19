package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.morpho.morphosmart.sdk.CallbackMask;
import com.morpho.morphosmart.sdk.CompressionAlgorithm;
import com.morpho.morphosmart.sdk.CustomInteger;
import com.morpho.morphosmart.sdk.DetectionMode;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.morpho.morphosmart.sdk.LatentDetection;
import com.morpho.morphosmart.sdk.MorphoDevice;
import com.morpho.morphosmart.sdk.MorphoImage;
import com.morpho.morphosmart.sdk.MorphoWakeUpMode;
import com.morpho.morphosmart.sdk.TemplateList;

import java.util.Observer;

public class MorphoSmartDeviceWrapper {
    private MorphoDevice morphoDevice;
    private String sensorName;

    public int init(Context context, String clientKey) {
        Log.d("AT", "creating MorphoDevice");

        morphoDevice = new MorphoDevice();

        int ret = enumerate();
        Log.d("AT", "MorphoDevice enumerate result: " + ret);

        if (ret != ErrorCodes.MORPHO_OK) {
            ret = morphoDevice.rebootSoft(30, null);
            Log.d("AT", "MorphoDevice rebootSoft result: " + ret);

            ret = morphoDevice.resumeConnection(30, null);
            Log.d("AT", "MorphoDevice resumeConnection result: " + ret);
            return -1;
        }

        ret = connection(morphoDevice);
        Log.d("AT", "MorphoDevice connection result: " + ret);


        //success
        if (ret == ErrorCodes.MORPHO_OK) {
            return 0;
        }

        Log.d("AT", "init response: " + ret);

        return -1;
    }

    public byte[] captureImage(int timeout, int minQuality, final Observer observer) {

        int timeOut = 20;
//        int timeOut = timeout / 1000; // change milli seconds to seconds

        int acquisitionThreshold = 0;
//        final CompressionAlgorithm compressAlgo = CompressionAlgorithm.MORPHO_COMPRESS_WSQ;
        final CompressionAlgorithm compressAlgo = CompressionAlgorithm.MORPHO_NO_COMPRESS;

        int compressRate = 0;
        int detectModeChoice = DetectionMode.MORPHO_ENROLL_DETECT_MODE.getValue();
        LatentDetection latentDetection = LatentDetection.LATENT_DETECT_DISABLE;
        final MorphoImage[] morphoImage = new MorphoImage[]{new MorphoImage()};
        int callbackCmd = CallbackMask.MORPHO_CALLBACK_IMAGE_CMD.getValue(); //Com.Morpho.Morphosmart.Sdk.CallbackMask.MorphoCallbackImageCmd.Value

//        callbackCmd &= ~CallbackMask.MORPHO_CALLBACK_ENROLLMENT_CMD.getValue();

//        TemplateList templateList = new TemplateList();
//        templateList.setActivateFullImageRetrieving(true);


        final int ret = morphoDevice.getImage(timeOut, acquisitionThreshold, compressAlgo, compressRate, detectModeChoice, latentDetection, morphoImage[0], callbackCmd, observer);
//        ProcessInfo.getInstance().setCommandBioStart(false);

//        getAndWriteFFDLogs();

        if (ret == ErrorCodes.MORPHO_OK) {

            return morphoImage[0].getImage();
        }


        return null;
    }

    public int close() {
        if (morphoDevice == null) {
            Log.d("ATFingerprint", "Attempt to close un-initialized device");
            return -1;
        }


        int error = morphoDevice.cancelLiveAcquisition();
        if (error != 0) {
            Log.d("ATFingerprint", "cancelLiveAcquisition failed: " + error);

        }

        error = morphoDevice.closeDevice();

        //success
        if (error == 0) {
            return 0;
        }

        return -1;
    }

    public static boolean isSupportedDevice(int vendorId, int productId) {

        return true;
    }


    private int enumerate() {

        CustomInteger cUsbDevice = new CustomInteger();

        int ret = morphoDevice.initUsbDevicesNameEnum(cUsbDevice);
        Integer nbUsbDevice = new Integer(cUsbDevice.getValueOf());

        if (ret == ErrorCodes.MORPHO_OK) {
            if (nbUsbDevice > 0) {
                sensorName = morphoDevice.getUsbDeviceName(0);
                return 0;
            } else {
                return -1;
            }
        }
//        else {
//            return -1;
//        }
        return -1;

    }

    private int connection(MorphoDevice morphoDevice) {
        int ret = morphoDevice.openUsbDevice(sensorName, 0);

        if (ret == ErrorCodes.MORPHO_OK) {
            return 0;
        }

        this.close();
        return -1;
    }


}
