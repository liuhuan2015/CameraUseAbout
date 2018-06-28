package com.liuh.camera_textureview_preview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Date: 2018/6/12 13:38
 * Description:
 */

public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final String TAG = "liuh";
    Context mContext;
    SurfaceTexture mSurfaceTexture;

    public CameraTextureView(Context context) {
        this(context, null);
    }

    public CameraTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable...");
        mSurfaceTexture = surface;
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG, "onSurfaceTextureDestroyed...");
        CameraInterface.getInstance().doStopCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureSizeChanged...");
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.e(TAG, "onSurfaceTextureUpdated...");
    }

    /**
     * 让Activity可以得到TextureView的SurfaceTexture
     * @return
     */
    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }
}
