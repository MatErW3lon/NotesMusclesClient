package com.notesmuscles.RecordLecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity{

    private final int Photo_Delay = 20000;
    private boolean recordingStarted;
    private Button captureButton, returnButton;
    private PhotoSend photoSend;
    private TiltSensor myTiltSensor;
    private TextureView cameraView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private String cameraID;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest captureRequest;
    private CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    private Handler backgroundHandler;
    private HandlerThread backgroundHandlerThread;

    private Handler delay_handler;

    private final Runnable senderRunnable = new Runnable() {
        @Override
        public void run() {
            repeatPhotoCaptureOnDelay();
        }
    };

    private View.OnClickListener captureStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            recordingStarted = false;
            captureButton.setText("RE START");
            delay_handler.removeCallbacks(senderRunnable);
            Thread stopperThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //Perform network tasks on new thread
                    try {
                        LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.Image_Stop);
                        LoginActivity.dataOutputStream.flush();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
            stopperThread.start();
            captureButton.setOnClickListener(captureStartListener);
        }
    };

    private View.OnClickListener captureStartListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            recordingStarted = true;
            Toast.makeText(getApplicationContext(), "STARTED RECORDING", Toast.LENGTH_SHORT).show();
            captureButton.setText("STOP");
            photoSend = new PhotoSend();
            //initialize the photo delay modules
            delay_handler = new Handler();
            try{
                takePicture();
            }catch(CameraAccessException cameraAccessException){}
            //wait for first photo send
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    repeatPhotoCaptureOnDelay();
                }
            }, Photo_Delay);
            captureButton.setOnClickListener(captureStopListener);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.camera_activity);

        //myTiltSensor = new TiltSensor(this);
        recordingStarted = false;
        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!recordingStarted){
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "STOP RECORDING BEFORE EXIT", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraView = (TextureView)  findViewById(R.id.cameraTextureView);
        cameraView.setSurfaceTextureListener(textureListener);
        captureButton = (Button) findViewById(R.id.captureButton);
        captureButton.setOnClickListener(captureStartListener);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(getApplicationContext(), "Camera permission is necessary", Toast.LENGTH_LONG).show();
            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {}

        @Override
        public boolean onSurfaceTextureDestroyed( SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {}
    };

    private final CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void repeatPhotoCaptureOnDelay(){
        try {
            photoSend.send();
            takePicture();
            delay_handler.postDelayed(senderRunnable, Photo_Delay);
        } catch (CameraAccessException cameraAccessException) {
            cameraAccessException.printStackTrace();
        }
    }

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = cameraView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
        Surface surface = new Surface(texture);
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

        captureRequestBuilder.addTarget(surface);
        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured( CameraCaptureSession session) {
                if(cameraDevice == null){
                    return;
                }
                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed( CameraCaptureSession cameraCaptureSession) {
                Toast.makeText(getApplicationContext(), "Configuration Changed", Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if(cameraDevice == null){
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);


        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);

    }

    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        cameraID = manager.getCameraIdList()[0]; //rear camera is at cameraID index 0

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        imageDimensions = map.getOutputSizes(ImageFormat.JPEG)[0];

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return;
        }
        manager.openCamera(cameraID, stateCallBack, null);
    }

    private void takePicture() throws CameraAccessException {
        if(cameraDevice == null){
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = null;
        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

        int width = 640;
        int height = 480;

        if(jpegSizes != null && jpegSizes.length>0){
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces  = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(cameraView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener(){
            public void onImageAvailable(ImageReader reader){
                Image image = null;
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                if(photoSend != null){
                    photoSend.setByteArray(bytes);
                }
            }
        };

        reader.setOnImageAvailableListener(readerListener, backgroundHandler);

        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                try {
                    createCameraPreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        };

        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                try {
                    session.capture(captureBuilder.build(), captureListener, backgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

            }
        }, backgroundHandler);
    }

    protected void onResume(){
        super.onResume();
        startBackgroundThread();
        if(cameraView.isAvailable()){
            try{
                openCamera();
            }catch (CameraAccessException e){
                e.printStackTrace();
            }
        }else{
            cameraView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread() {
        backgroundHandlerThread = new HandlerThread("Camera Background");
        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());

    }

    protected void onPause(){
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    protected void stopBackgroundThread() throws InterruptedException {
        backgroundHandlerThread.quitSafely();
        backgroundHandlerThread.join();
        backgroundHandlerThread = null;
        backgroundHandler = null;
    }
}
