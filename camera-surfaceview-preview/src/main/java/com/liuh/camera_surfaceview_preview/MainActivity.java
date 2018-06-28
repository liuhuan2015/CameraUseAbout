package com.liuh.camera_surfaceview_preview;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements CameraInterface.CamOpenOverCallback {

    @BindView(R.id.camera_surfaceview)
    CameraSurfaceView cameraSurfaceView;
    @BindView(R.id.btn_shutter)
    Button btnShutter;

    float previewRate = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread openThread = new Thread() {
            @Override
            public void run() {
                CameraInterface.getInstance().doOpenCamera(MainActivity.this);
            }
        };
        openThread.start();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViewParams();
    }

    @Override
    public void cameraHasOpened() {
        SurfaceHolder holder = cameraSurfaceView.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }

    private void initViewParams() {
        ViewGroup.LayoutParams params = cameraSurfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        cameraSurfaceView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        ViewGroup.LayoutParams p2 = btnShutter.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);
        btnShutter.setLayoutParams(p2);
    }

    @OnClick({R.id.btn_shutter})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_shutter:
                CameraInterface.getInstance().doTakePicture();
                break;
        }
    }
}
