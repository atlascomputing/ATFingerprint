package co.atlascomputing.atfingerprint;

import android.content.Context;
import android.hardware.usb.UsbManager;

import co.atlascomputing.atfingerprint.dto.DeviceInfo;
import co.atlascomputing.atfingerprint.dto.DeviceModel;
import co.atlascomputing.atfingerprint.interfaces.GenericUsbDevice;
import co.atlascomputing.atfingerprint.wrappers.ChineseFPDeviceWrapper;
import co.atlascomputing.atfingerprint.wrappers.MantraMFS100DeviceWrapper;
import co.atlascomputing.atfingerprint.wrappers.MantraMIDAuthDeviceWrapper;
import co.atlascomputing.atfingerprint.wrappers.MantraMorfinAuthDeviceWrapper;
import co.atlascomputing.atfingerprint.wrappers.MorphoSmartDeviceWrapper;
import co.atlascomputing.atfingerprint.wrappers.SecuGenDeviceWrapper;

public class FingerprintLib implements GenericUsbDevice {
    private static final int INFINITY_TIMEOUT = Integer.MAX_VALUE;
    private final Context context;

    // device wrappers
    private final MantraMFS100DeviceWrapper mantraMFS100DeviceWrapper;
    private final MantraMIDAuthDeviceWrapper mantraMIDAuthDeviceWrapper;
    private final MantraMorfinAuthDeviceWrapper mantraMorfinAuthDeviceWrapper;
    private final SecuGenDeviceWrapper secuGenDeviceWrapper;
    private final MorphoSmartDeviceWrapper morphoSmartDeviceWrapper;
    private final ChineseFPDeviceWrapper chineseFPDeviceWrapper;

    public FingerprintLib(Context applicationContext) {
        context = applicationContext;

        mantraMFS100DeviceWrapper = new MantraMFS100DeviceWrapper();
        mantraMIDAuthDeviceWrapper = new MantraMIDAuthDeviceWrapper();
        mantraMorfinAuthDeviceWrapper = new MantraMorfinAuthDeviceWrapper();
        secuGenDeviceWrapper = new SecuGenDeviceWrapper();
        morphoSmartDeviceWrapper = new MorphoSmartDeviceWrapper();
        chineseFPDeviceWrapper = new ChineseFPDeviceWrapper();
    }

    @Override
    public boolean isSupportedDevice(int vendorId, int productId, String productName, String manufacturerName) {
        return MantraMFS100DeviceWrapper.isSupportedDevice(vendorId, productId) ||
                MantraMIDAuthDeviceWrapper.isSupportedDevice(vendorId, productId) ||
                MantraMorfinAuthDeviceWrapper.isSupportedDevice(vendorId, productId) ||
                SecuGenDeviceWrapper.isSupportedDevice(vendorId, productId) ||
                MorphoSmartDeviceWrapper.isSupportedDevice(vendorId, productId, productName, manufacturerName) ||   // morpho isSupported not implemented
                ChineseFPDeviceWrapper.isSupportedDevice(vendorId, productId);
    }

    @Override
    public int init(DeviceModel inDeviceModel, byte[] inClientKey, DeviceInfo outDeviceInfo) {

        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100DeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isSecuGen = SecuGenDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartDeviceWrapper.isSupportedDevice(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPDeviceWrapper.isSupportedDevice(vendorId, productId);

        if (isMantraMFS100) {
            return mantraMFS100DeviceWrapper.init(context, null);
        }

        if (isMantraMIDAuth) {
            return mantraMIDAuthDeviceWrapper.init(context);
        }

        if (isMantraMorfinAuth) {
            return mantraMorfinAuthDeviceWrapper.init(context, null);
        }

        if (isSecuGen) {
            return secuGenDeviceWrapper.init(context, null);
        }

        if (isMorphoSmart) {
            return morphoSmartDeviceWrapper.init(context, null);
        }

        if (isChineseFP) {
            return chineseFPDeviceWrapper.init(context, null);
        }

        return -1;
    }

    @Override
    public int startCapture(DeviceModel inDeviceModel, int timeout, int minQuality) {

        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100DeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isSecuGen = SecuGenDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartDeviceWrapper.isSupportedDevice(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPDeviceWrapper.isSupportedDevice(vendorId, productId);

        // cap minQuatilty to between 0 and 100
        int tmpMinQuality = minQuality >= 0 ? minQuality : 50; //negative
        tmpMinQuality = tmpMinQuality > 100 ? 50 : tmpMinQuality; // above 100

        if (isMantraMFS100) {
            byte[] captureImage = mantraMFS100DeviceWrapper.captureImage(timeout, false);
            return 0;
        }

        if (isMantraMIDAuth) {
            byte[] captureImage = mantraMIDAuthDeviceWrapper.captureImage(timeout, tmpMinQuality);
            return 0;
        }

        if (isMantraMorfinAuth) {
            byte[] captureImage = mantraMorfinAuthDeviceWrapper.captureImage(timeout, tmpMinQuality);
            return 0;
        }

        if (isSecuGen) {
            int tmpTimeout = timeout == 0 ? INFINITY_TIMEOUT : timeout;
            byte[] captureImage = secuGenDeviceWrapper.captureImage(tmpTimeout, tmpMinQuality);
            return 0;
        }

//        if (isChineseFP){
//             chineseFPDeviceWrapper.init(context,null);
//        }


        return -1;
    }

    @Override
    public int closeDevice(DeviceModel inDeviceModel) {
        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100DeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isSecuGen = SecuGenDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartDeviceWrapper.isSupportedDevice(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPDeviceWrapper.isSupportedDevice(vendorId, productId);

        if (isMantraMFS100) {
            return mantraMFS100DeviceWrapper.closeDevice();
        }

        if (isMantraMIDAuth) {
            return mantraMIDAuthDeviceWrapper.closeDevice();
        }

        if (isMantraMorfinAuth) {
            return mantraMorfinAuthDeviceWrapper.closeDevice();
        }

        if (isMorphoSmart) {
            return morphoSmartDeviceWrapper.closeDevice();
        }

        if (isSecuGen) {
            return secuGenDeviceWrapper.closeDevice();
        }

        if (isChineseFP) {
            return chineseFPDeviceWrapper.closeDevice();
        }

        return -1;
    }

    @Override
    public int close(DeviceModel inDeviceModel) {
        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100DeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isSecuGen = SecuGenDeviceWrapper.isSupportedDevice(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartDeviceWrapper.isSupportedDevice(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPDeviceWrapper.isSupportedDevice(vendorId, productId);

        if (isMantraMFS100) {
            return mantraMFS100DeviceWrapper.close();
        }

        if (isMantraMIDAuth) {
            return mantraMIDAuthDeviceWrapper.close();
        }

        if (isMantraMorfinAuth) {
            return mantraMorfinAuthDeviceWrapper.close();
        }

        if (isMorphoSmart) {
            return morphoSmartDeviceWrapper.close();
        }

        if (isSecuGen) {
            return secuGenDeviceWrapper.close();
        }

        if (isChineseFP) {
            return chineseFPDeviceWrapper.close();
        }

        return -1;
    }
}
