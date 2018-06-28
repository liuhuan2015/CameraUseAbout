package com.liuh.camera_glsurfaceview_preview;

import android.hardware.Camera;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 2018/6/12 09:11
 * Description: 用来设置、打印Camera的PreviewSize、PictureSize、FocusMode的，
 * 并能根据Activity传进来的长宽比(主要是16:9 或 4:3两种尺寸)自动寻找适配的PreviewSize和PictureSize，消除变形
 */

public class CamaParamUtil {
    private static final String TAG = "yanzi";
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static CamaParamUtil myCamPara = null;


    public CamaParamUtil() {

    }

    public static CamaParamUtil getInstance() {
        if (myCamPara == null) {
            myCamPara = new CamaParamUtil();
            return myCamPara;
        } else {
            return myCamPara;
        }
    }

    public Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth) {
        Collections.sort(list, sizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width >= minWidth) && equalRate(s, th)) {
                Log.i(TAG, "PreviewSize:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    public boolean equalRate(Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打印支持的previewSize
     *
     * @param params
     */
    public void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (Camera.Size size : previewSizes)
            Log.e(TAG, "previewSizes:width = " + size.width + " height = " + size.height);
    }

    /**
     * 打印支持的pictureSizes
     */
    public void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (Camera.Size size : pictureSizes) {
            Log.e(TAG, "pictureSizes:width = " + size.width
                    + " height = " + size.height);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.e(TAG, "focusModes----" + mode);
        }
    }

    public class CameraSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}
