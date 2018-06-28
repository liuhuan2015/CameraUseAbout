package com.liuh.camera_surfaceview_preview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Date: 2018/6/28 13:48
 * Description:
 */

public class ImageUtil {

    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }

}
