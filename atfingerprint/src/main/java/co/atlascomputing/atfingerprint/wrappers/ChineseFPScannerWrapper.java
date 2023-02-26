package co.atlascomputing.atfingerprint.wrappers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fpreader.fpdevice.Constants;
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
        fpModule.SetContextHandler(context, mHandler);

//        int result = fpModule.OpenDevice();
//        if (result == 0) {
//            Log.d("ATFingerprint", "Fingerprint device init success!");
//            return 0;
//        }

        Log.d("ATFingerprint", "Fingerprint device init finished!");
        return -1;
    }


    public int openDevice() {

        try {
            int result = fpModule.OpenDevice();
            if (result == 0) {
                Log.d("ATFingerprint", "Fingerprint device open success!");
                return 0;
            }

            Log.d("ATFingerprint", "Fingerprint device open failed! " + fpModule.FPErr2Str(result));
            return -1;
        } catch (Exception e) {
            Log.d("ATFingerprint", "Fingerprint device open exception occured");


            try {
                int result = fpModule.OpenDevice();
                if (result == 0) {
                    Log.d("ATFingerprint", "Fingerprint device open success!");
                    return 0;
                }

            } catch (Exception e2) {
                Log.d("ATFingerprint", "Fingerprint device open exception occured2");

                return -1;
            }


        }

        return 0;

    }


    public byte[] captureImage(int timeout, int minQuality) {

        int result = fpModule.CaptureImage();

        if (result == 0) {
            Log.d("ATFingerprint", "Fingerprint device captureImage success!");

//            return 0;
        }
        Log.d("ATFingerprint", "Fingerprint device captureImage failed!" + fpModule.FPErr2Str(result));

        return null;
    }

    public int closeDevice() {

        int result = fpModule.CloseDevice();

        if (result == 0) {
            return 0;
        }

        return -1;
    }

    public int close() {
        int result = fpModule.CloseDevice();

        if (result == 0) {
            return 0;
        }

        return -1;
    }

    public static boolean isSupportedScanner(int vendorId, int productId) {

        // vendorId = 1107, 8201, 8457, 1155. 30264 by two 8201 and 8457
        if (productId == 36869 || productId == 30264 || productId == 22304) {
            return true;
        }
        return false;
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.FPM_DEVICE: {
                    switch (msg.arg1) {
                        case Constants.DEV_ATTACHED:
                            break;
                        case Constants.DEV_DETACHED:
//                            isopening=false;
//                            isworking=false;
                            fpModule.CloseDevice();
                            Log.d("AT", "Close");
                            break;
                        case Constants.DEV_OK:
//                            isopening=true;
//                            isworking=false;
                            Log.d("AT", "Open Device OK");
                            break;
                        default:
                            Log.d("AT", "Open Device Fail");
                            break;
                    }
                }
                break;
                case Constants.FPM_PLACE:
                    Log.d("AT", "Place Finger");
                    break;
                case Constants.FPM_LIFT:
                    Log.d("AT", "Lift Finger");
                    break;
                case Constants.FPM_CAPTURE: {
                    if (msg.arg1 == 1) {
                        Log.d("AT", "Capture Image OK");
                    } else {
                        Log.d("AT", "Capture Image Fail");
                    }
//                    isworking=false;
                }
                break;
                case Constants.FPM_GENCHAR: {
                    if (msg.arg1 == 1) {
                        Log.d("AT", "Generate Template OK");
//                        fpModule.GetTemplateByGen(matdata, matsize);
//
//                        int mret=fpModule.MatchTemplate(refdata, matdata);
//                        Log.d("AT",String.format("Match Return:%d",mret));
                    } else {
                        Log.d("AT", "Generate Template Fail");
                    }
//                    isworking=false;
                }
                break;
                case Constants.FPM_ENRFPT: {
                    if (msg.arg1 == 1) {
                        Log.d("AT", "Enrol Template OK");
//                        fpModule.GetTemplateByEnl(refdata,refsize);
                    } else {
                        Log.d("AT", "Enrol Template Fail");
                    }
//                    isworking=false;
                }
                break;
                case Constants.FPM_NEWIMAGE: {
                    Log.d("AT", "FPM_NEWIMAGE");
//                    fpModule.GetBmpImage(bmpdata,bmpsize);
//                    Bitmap bm1= BitmapFactory.decodeByteArray(bmpdata, 0, bmpsize[0]);
//                    ivImage.setImageBitmap(bm1);
                }
                break;
                case Constants.FPM_TIMEOUT:
                    Log.d("AT", "Time Out");
//                    isworking=false;
                    break;
            }
        }
    };
}
