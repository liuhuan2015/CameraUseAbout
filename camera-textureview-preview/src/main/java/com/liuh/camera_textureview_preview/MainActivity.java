package com.liuh.camera_textureview_preview;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc : 使用TextureView实时预览Camera，TextureView是在android 4.0时提出的。
 * 作者认为：推出TextureView这个类，是为了弥补SurfaceView的不足，另一方便也是为了平衡GLSurfaceView。
 * 使用SurfaceView预览的话，需要传一个SurfaceHolder进去，使用TextureView预览的话，需要传进去一个SurfaceTexture。
 * 两者其它的与Camare相关的流程不变
 */
public class MainActivity extends AppCompatActivity implements CameraInterface.CamOpenOverCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.camera_textureview)
    CameraTextureView mTextureView;

    float previewRate = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Thread openThread = new Thread() {

            @Override
            public void run() {
                CameraInterface.getInstance().doOpenCamera(MainActivity.this);
            }
        };
        openThread.start();

        initViewParams();
    }

    private void initViewParams() {
        ViewGroup.LayoutParams params = mTextureView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this);//默认全屏的比例预览
        mTextureView.setLayoutParams(params);
    }

    @Override
    public void cameraHasOpened() {
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        Log.e(TAG, "----previewRate : " + previewRate);
        CameraInterface.getInstance().doStartPreview(surfaceTexture, previewRate);
    }

    @OnClick({R.id.btn_take_pic})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_pic:
                CameraInterface.getInstance().doTakePicture();
                break;
        }
    }

}
