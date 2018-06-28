package com.liuh.camera_textureview_preview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Date: 2018/6/12 13:48
 * Description:
 */

public class CameraInterface {

    private static final String TAG = "liuh";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;

    public interface CamOpenOverCallback {
        void cameraHasOpened();
    }

    public CameraInterface() {

    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * 打开相机
     *
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback) {
        Log.e(TAG, "Camera open....");
        mCamera = Camera.open();
        Log.e(TAG, "Camera open over....");
        callback.cameraHasOpened();
    }

    /**
     * 使用SurfaceView开启预览
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initCamera(previewRate);
        }
    }

    public void doStartPreview(SurfaceTexture surfaceTexture, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(previewRate);
        }
    }

    private void initCamera(float previewRate) {
        mParams = mCamera.getParameters();
        mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
        CamaParamUtil.getInstance().printSupportPictureSize(mParams);
        CamaParamUtil.getInstance().printSupportPreviewSize(mParams);

        //设置PreviewSize和PictureSize
        Camera.Size pictureSize = CamaParamUtil.getInstance().getPropPreviewSize(
                mParams.getSupportedPictureSizes(), previewRate, 800
        );
        mParams.setPictureSize(pictureSize.width, pictureSize.height);

        Camera.Size previewSize = CamaParamUtil.getInstance().getPropPreviewSize(
                mParams.getSupportedPreviewSizes(), previewRate, 800);
        mParams.setPreviewSize(previewSize.width, previewSize.height);

        mCamera.setDisplayOrientation(90);

        CamaParamUtil.getInstance().printSupportFocusMode(mParams);
        List<String> focusModes = mParams.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        mCamera.setParameters(mParams);

        mCamera.startPreview();//开启预览

        isPreviewing = true;
        mPreviwRate = previewRate;

        mParams = mCamera.getParameters();//重新get一次

        Log.e(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
                + "Height = " + mParams.getPreviewSize().height);
        Log.e(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
                + "Height = " + mParams.getPictureSize().height);

    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            //三个回调变量是关于拍照时的声音和拍照图片保存的
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }


    //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.e(TAG, "myShutterCallback:onShutter...");
        }
    };

    // 拍摄的未压缩原数据的回调,可以为null
    Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e(TAG, "myRawCallback:onPictureTaken...");
        }
    };

    //对jpeg图像数据的回调,最重要的一个回调
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if (null != data) {
                //data是字节数据，将其解析为位图
                b = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                isPreviewing = false;
            }

            //保存到sdcard
            if (null != b) {
                //因为设置FOCUS_MODE_CONTINUOUS_VIDEO之后，myParam.set("rotation", 90)失效。
                //图片不能旋转了，故这里要旋转下
                Bitmap rotabitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                FileUtil.saveBitmap(rotabitmap);
            }
            mCamera.startPreview();
            isPreviewing = true;
        }
    };


}
