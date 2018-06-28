package com.liuh.camera_textureview_preview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Date: 2018/6/12 09:55
 * Description:
 */

public class ImageUtil {

    /**
     * 旋转Bitmap
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotateBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                matrix, false);
        return rotateBitmap;
    }

}
