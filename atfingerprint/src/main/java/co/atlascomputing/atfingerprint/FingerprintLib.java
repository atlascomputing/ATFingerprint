package co.atlascomputing.atfingerprint;

import android.content.Context;

import co.atlascomputing.atfingerprint.dto.DeviceInfo;
import co.atlascomputing.atfingerprint.dto.DeviceModel;
import co.atlascomputing.atfingerprint.interfaces.GenericUsbScanner;
import co.atlascomputing.atfingerprint.wrappers.ChineseFPScannerWrapper;
import co.atlascomputing.atfingerprint.wrappers.MantraMFS100ScannerWrapper;
import co.atlascomputing.atfingerprint.wrappers.MantraMIDAuthScannerWrapper;
import co.atlascomputing.atfingerprint.wrappers.MantraMorfinAuthScannerWrapper;
import co.atlascomputing.atfingerprint.wrappers.MorphoSmartScannerWrapper;
import co.atlascomputing.atfingerprint.wrappers.SecuGenScannerWrapper;

public class FingerprintLib implements GenericUsbScanner {
    private static final int INFINITY_TIMEOUT = Integer.MAX_VALUE;
    private final Context context;

    // scanner wrappers
    private final MantraMFS100ScannerWrapper mantraMFS100ScannerWrapper;
    private final MantraMIDAuthScannerWrapper mantraMIDAuthScannerWrapper;
    private final MantraMorfinAuthScannerWrapper mantraMorfinAuthScannerWrapper;
    private final SecuGenScannerWrapper secuGenScannerWrapper;
    private final MorphoSmartScannerWrapper morphoSmartScannerWrapper;
    private final ChineseFPScannerWrapper chineseFPScannerWrapper;

    public FingerprintLib(Context applicationContext) {
        context = applicationContext;

        mantraMFS100ScannerWrapper = new MantraMFS100ScannerWrapper(context);
        mantraMIDAuthScannerWrapper = new MantraMIDAuthScannerWrapper(context);
        mantraMorfinAuthScannerWrapper = new MantraMorfinAuthScannerWrapper(context);
        secuGenScannerWrapper = new SecuGenScannerWrapper(context);
        morphoSmartScannerWrapper = new MorphoSmartScannerWrapper(context);
        chineseFPScannerWrapper = new ChineseFPScannerWrapper(context);
    }

    @Override
    public boolean isSupportedScanner(int vendorId, int productId, String productName, String manufacturerName) {
        return MantraMFS100ScannerWrapper.isSupportedScanner(vendorId, productId) ||
                MantraMIDAuthScannerWrapper.isSupportedScanner(vendorId, productId) ||
                MantraMorfinAuthScannerWrapper.isSupportedScanner(vendorId, productId) ||
                SecuGenScannerWrapper.isSupportedScanner(vendorId, productId) ||
                MorphoSmartScannerWrapper.isSupportedScanner(vendorId, productId, productName, manufacturerName) ||   // morpho isSupported not implemented
                ChineseFPScannerWrapper.isSupportedScanner(vendorId, productId);
    }

    @Override
    public int init(DeviceModel inDeviceModel, byte[] inClientKey, DeviceInfo outDeviceInfo) {

        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100ScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isSecuGen = SecuGenScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartScannerWrapper.isSupportedScanner(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPScannerWrapper.isSupportedScanner(vendorId, productId);

        if (isMantraMFS100) {
            return mantraMFS100ScannerWrapper.init(inClientKey);
        }

        if (isMantraMIDAuth) {
            return mantraMIDAuthScannerWrapper.init();
        }

        if (isMantraMorfinAuth) {
            return mantraMorfinAuthScannerWrapper.init(inClientKey);
        }

        if (isSecuGen) {
            secuGenScannerWrapper.init();
            return secuGenScannerWrapper.openDevice(0);
        }

        if (isMorphoSmart) {
            return morphoSmartScannerWrapper.init(inClientKey);
        }

        if (isChineseFP) {
            chineseFPScannerWrapper.init();
            return chineseFPScannerWrapper.openDevice();
        }

        return -1;
    }

    @Override
    public byte[] captureImage(DeviceModel inDeviceModel, int timeout, int minQuality) {

        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100ScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isSecuGen = SecuGenScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartScannerWrapper.isSupportedScanner(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPScannerWrapper.isSupportedScanner(vendorId, productId);

        // cap minQuatilty to between 0 and 100
        int tmpMinQuality = minQuality >= 0 ? minQuality : 50; //negative
        tmpMinQuality = tmpMinQuality > 100 ? 50 : tmpMinQuality; // above 100

        if (isMantraMFS100) {
            byte[] captureImage = mantraMFS100ScannerWrapper.captureImage(timeout, false);
            return captureImage;
        }

        if (isMantraMIDAuth) {
            byte[] captureImage = mantraMIDAuthScannerWrapper.captureImage(timeout, tmpMinQuality);
            return captureImage;
        }

        if (isMantraMorfinAuth) {
            byte[] captureImage = mantraMorfinAuthScannerWrapper.captureImage(timeout, tmpMinQuality);
            return captureImage;
        }

        if (isSecuGen) {
            int tmpTimeout = timeout == 0 ? INFINITY_TIMEOUT : timeout;
            byte[] captureImage = secuGenScannerWrapper.captureImage(tmpTimeout, tmpMinQuality);
            return captureImage;
        }

//        if (isChineseFP){
//             chineseFPDeviceWrapper.init(context,null);
//        }


        return null;
    }

    @Override
    public int closeDevice(DeviceModel inDeviceModel) {
        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100ScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isSecuGen = SecuGenScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartScannerWrapper.isSupportedScanner(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPScannerWrapper.isSupportedScanner(vendorId, productId);

        if (isMantraMFS100) {
            return mantraMFS100ScannerWrapper.closeDevice();
        }

        if (isMantraMIDAuth) {
            return mantraMIDAuthScannerWrapper.closeDevice();
        }

        if (isMantraMorfinAuth) {
            return mantraMorfinAuthScannerWrapper.closeDevice();
        }

        if (isMorphoSmart) {
            return morphoSmartScannerWrapper.closeDevice();
        }

        if (isSecuGen) {
            return secuGenScannerWrapper.closeDevice();
        }

        if (isChineseFP) {
            return chineseFPScannerWrapper.closeDevice();
        }

        return -1;
    }

    @Override
    public int close(DeviceModel inDeviceModel) {
        int vendorId = inDeviceModel.getVendorId();
        int productId = inDeviceModel.getProductId();
        String productName = inDeviceModel.getProductName();
        String manufacturerName = inDeviceModel.getManufacturerName();

        boolean isMantraMFS100 = MantraMFS100ScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMIDAuth = MantraMIDAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMantraMorfinAuth = MantraMorfinAuthScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isSecuGen = SecuGenScannerWrapper.isSupportedScanner(vendorId, productId);
        boolean isMorphoSmart = MorphoSmartScannerWrapper.isSupportedScanner(vendorId, productId, productName, manufacturerName);
        boolean isChineseFP = ChineseFPScannerWrapper.isSupportedScanner(vendorId, productId);

        if (isMantraMFS100) {
            return mantraMFS100ScannerWrapper.close();
        }

        if (isMantraMIDAuth) {
            return mantraMIDAuthScannerWrapper.close();
        }

        if (isMantraMorfinAuth) {
            return mantraMorfinAuthScannerWrapper.close();
        }

        if (isMorphoSmart) {
            return morphoSmartScannerWrapper.close();
        }

        if (isSecuGen) {
            return secuGenScannerWrapper.close();
        }

        if (isChineseFP) {
            return chineseFPScannerWrapper.close();
        }

        return -1;
    }
}
