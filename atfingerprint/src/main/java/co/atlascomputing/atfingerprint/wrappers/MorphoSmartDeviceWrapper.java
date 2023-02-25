package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.morpho.android.usb.USBManager;
import com.morpho.morphosmart.sdk.CallbackMask;
import com.morpho.morphosmart.sdk.CompressionAlgorithm;
import com.morpho.morphosmart.sdk.CustomInteger;
import com.morpho.morphosmart.sdk.DetectionMode;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.morpho.morphosmart.sdk.LatentDetection;
import com.morpho.morphosmart.sdk.MorphoDevice;
import com.morpho.morphosmart.sdk.MorphoImage;
import com.morpho.morphosmart.sdk.MorphoImageHeader;
import com.morpho.morphosmart.sdk.MorphoWakeUpMode;
import com.morpho.morphosmart.sdk.StrategyAcquisitionMode;
import com.morpho.morphosmart.sdk.TemplateList;

import java.util.Objects;
import java.util.Observer;

import co.atlascomputing.atfingerprint.dto.DeviceModel;

public class MorphoSmartDeviceWrapper {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = "AT";
    private MorphoDevice morphoDevice;
    private String sensorName;

    public int init(Context context, String clientKey) {

        int ret = USBManager.getInstance().initialize(context, ACTION_USB_PERMISSION);
        Log.d("AT", "MorphoDevice initialize result: " + ret);

        Log.d(TAG, "creating MorphoDevice");

        morphoDevice = new MorphoDevice();

        ret = enumerate();
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
//        int acquisitionThreshold = minQuality;
//        final CompressionAlgorithm compressAlgo = CompressionAlgorithm.MORPHO_COMPRESS_WSQ;
        final CompressionAlgorithm compressAlgo = CompressionAlgorithm.MORPHO_NO_COMPRESS;

        int compressRate = 0;
        int detectModeChoice = DetectionMode.MORPHO_ENROLL_DETECT_MODE.getValue();
        LatentDetection latentDetection = LatentDetection.LATENT_DETECT_DISABLE;
//        final MorphoImage[] morphoImage = new MorphoImage[]{new MorphoImage()};
        final MorphoImage morphoImage = new MorphoImage();

        MorphoImageHeader tmp = new MorphoImageHeader();
        tmp.setResX(500);
        tmp.setResY(500);
        tmp.setNbRow(500);
        tmp.setNbColumn(500);

        morphoImage.setMorphoImageHeader(tmp);


        int callbackCmd = CallbackMask.MORPHO_CALLBACK_IMAGE_CMD.getValue(); //Com.Morpho.Morphosmart.Sdk.CallbackMask.MorphoCallbackImageCmd.Value

//        callbackCmd &= ~CallbackMask.MORPHO_CALLBACK_ENROLLMENT_CMD.getValue();

//        TemplateList templateList = new TemplateList();
//        templateList.setActivateFullImageRetrieving(true);

        Log.d(TAG, "Starting image capture");

        morphoDevice.setStrategyAcquisitionMode(StrategyAcquisitionMode.MORPHO_ACQ_EXPERT_MODE);
        final int ret = morphoDevice.getImage(timeOut, acquisitionThreshold, compressAlgo, compressRate, detectModeChoice, latentDetection, morphoImage, callbackCmd, observer);
//        ProcessInfo.getInstance().setCommandBioStart(false);

//        getAndWriteFFDLogs();
        Log.d(TAG, "Capture image result: " + ret);

        if (ret == ErrorCodes.MORPHO_OK) {

            return morphoImage.getImage();
        }


        return null;
    }

    public int closeDevice() {
        return -1;
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

    public static boolean isSupportedDevice(int vendorId, int productId, String productName, String manufacturerName) {

        if (Objects.equals(manufacturerName, "Morpho")) {
            return true;
        }

        return false;
    }

    private int enumerate() {

        CustomInteger cUsbDevice = new CustomInteger();

        Log.d(TAG, "Starting initUsbDevicesNameEnum");

        int ret = morphoDevice.initUsbDevicesNameEnum(cUsbDevice);
//        Integer nbUsbDevice = new Integer(cUsbDevice.getValueOf());
        Log.d(TAG, "initUsbDevicesNameEnum result: " + ret + " nbUsbDevice: " + cUsbDevice.getValueOf());

        if (ret == ErrorCodes.MORPHO_OK) {
            if (cUsbDevice.getValueOf() > 0) {
                sensorName = morphoDevice.getUsbDeviceName(0);
                Log.d(TAG, "enumerate success:,  sensorName: " + sensorName);
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
        Log.d(TAG, "starting openUsbDevice");

        int ret = morphoDevice.openUsbDevice(sensorName, 0);
        Log.d(TAG, "openUsbDevice result: " + ret);

        if (ret == ErrorCodes.MORPHO_OK) {
            return 0;
        }

        this.close();
        return -1;
    }


}
