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
import android.text.LoginFilter;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity{

    private final int Photo_Delay = 30000;
    private boolean recordingStarted;
    private Button captureButton, returnButton;
    private PhotoSend photoSendThread;
    private TiltSensor myTiltSensor;
    private TextureView cameraView;  //can be used to display a content stream, for example a camera preview
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray(); //sparse int array with many zero elements. maps int to int. Does not use autoboxing therefore faster than HashMaps

    static{
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
        /*
            MAPS KEY VALUE PAIRS
                  0    90
                  1     0
                  2   270
                  3   180
         */
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
    //private Handler photoDelayHandler;

    private final Runnable senderRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    takePicture();
                    Thread.sleep(30000);
                    photoSendThread.send();
                } catch (Exception exception) {
                    //do nothing
                }
            }
        }
    };

    /*private View.OnClickListener captureStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            photoDelayHandler.removeCallbacks(delayedPhotoSend);
            captureButton.setText("RE START");
            //note that we will be readings bytes at this point on the server
            Thread stopperThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(photoSendThread != null){
                        photoSendThread.interrupt();
                    }
                    try {
                        LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.Image_Stop);
                        LoginActivity.dataOutputStream.flush();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    recordingStarted = false;
                    captureButton.setOnClickListener(captureStartListener);
                }
            });
            stopperThread.start();
        }
    };*/

    private View.OnClickListener captureStartListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
                /*photoSendThread = new PhotoSend();
                recordingStarted = true;
                captureButton.setText("STOP");
                captureButton.setOnClickListener(captureStopListener);
                //new Thread(myTiltSensor).start();
                delayedPhotoSend.run();*/
                photoSendThread = new PhotoSend();
                Thread senderThread = new Thread(senderRunnable);
                senderThread.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.camera_activity);

        //photoDelayHandler = new Handler();

        //myTiltSensor = new TiltSensor(this);
        recordingStarted =false;
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
        cameraView.setSurfaceTextureListener(textureListener); //sets the textureview to the texturelistener...sets the SurfaceTextureListener used to listen to surface texture events
        captureButton = (Button) findViewById(R.id.captureButton);
        captureButton.setOnClickListener(captureStartListener);
    }

    //this function is a callback for the result from requesting permissions
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(getApplicationContext(), "Camera permission is necessary", Toast.LENGTH_LONG).show();
            }
        }
    }

    //TextureView.SurfaceTextureListener is a nested class for TextureView
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        /*
            this listener can be used to be notified when the surface texture associated with this texture view is available
            in this case our content stream is the camera preview
         */
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        /*
            this method is invoked when a TextureView's SurfaceTexture is ready for use
            SurfaceTexture captures frames from an image stream
         */
            try {
                openCamera(); //starting the camera
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        /*
            invoked when the SurfaceTexture's buffers size changed
         */
        }

        @Override
        public boolean onSurfaceTextureDestroyed( SurfaceTexture surfaceTexture) {
        /*
            Invoked when the specified SurfaceTexture is about to be destroyed
         */
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        /*
            Invoked when the specified SurfaceTexture is updated through SurfaceTexture#updateTexImage().
            updateTex(ture)Image updates the texture image to the most recent frame from the image stream
         */

        }
    };

    private final CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        //the states are opened, disconnected and error occured. The statecallback hence means the callback when the cameradevice is in the respective state
        @Override
        public void onOpened(CameraDevice camera) { //called when the camera device is opened
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
            cameraDevice.close(); //if an error has error delete the camera
            cameraDevice = null;
        }
    };

    private void createCameraPreview() throws CameraAccessException { //the exception is thrown if a camera device could not be queried by the cameramanager
        SurfaceTexture texture = cameraView.getSurfaceTexture(); //gets the surfacetexture associated with the textview
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight()); //sets the default size of the image buffers according to image size
        Surface surface = new Surface(texture);  //drawas from the surfacetexture's image buffers
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);  //the constant allows to create a request suitable for a camera preview window
        //createCaptureReq returns CaptureRequest.Builder
        //Create a CaptureRequest.Builder for new capture requests, initialized with template for a target use case.

        /*
            What is CaptureRequest? it is an immutable package of settings and outputs needed to capture  a single image  from the camera device
            It contains the config for the capture hardware(sensor, lens, flash), the processing pipeline, the control algos, and the output buffer. It also contains
            the list of target Surfaces to send image data to for this capture
         */

        /*
            WHAT IS A BUILDER????? a builder pattern builds a complex object (in this case capturerequest) using simple objects and using  a step by step approach
            A builder class builds the final object step by step. This builder is independent of other objects

         */

        captureRequestBuilder.addTarget(surface); //the capture session should draw on this surface
        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() { // a callback object for receiving updates about the state of a camera capture session
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
        //CONTROL MODE Overall mode of 3A (auto-exposure, auto-white-balance, auto-focus) control routines.
        //CONTROL MODE AUTO -> Use settings for each individual 3A routine.
        //set a capture request field to a value

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
        //Request endlessly repeating capture of images by this capture session. The handler parameter lets this run on a thread
    }

    private void openCamera() throws CameraAccessException {
        //we use this to iterate all the cameras that are available in the system, each with a designated cameraID. Using the camereID we can get the properties
        //of the specified camera device. Those properties are represented by class CameraCharacteristics. Things like "is it front or back camera", "output resolutions supported"
        //can be queried there
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE); //returns the handle to a system-level service by name

        cameraID = manager.getCameraIdList()[0]; //rear camera is at cameraID index 0

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID); //getting the associated properties of the camera //quering//immutable object
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        //streamconfigMap is an immutable class meaning once an object is created, we cannot change its content
        //stream configurations? refers to a single camera stream configured in the camera device
        //Surface? is generally created by or from a consumer of image buffers (e.g SurfaceTexture) and is handed to some kind of producer(e.g. CameraDevice) to draw into
        //SCALAR_STREAM_CONFIG_MAP : The available stream configurations that this camera device supports; also includes the minimum frame durations and the stall durations for each format/size combination.
        //StreamConfigMap is the authoritative list for all the output format (and sizes respectively for that format) that are supported by a camera device
        //This also contains the minimum frame durations and stall durations for each format/size combination that can be used to calculate effective frame rate when submitting multiple captures.
        //what is an image size? represents the physical size and resolution of an image measured in pixels

        //NOTE: cameraID and imageDimensions are MainActivity fields

        imageDimensions = map.getOutputSizes(ImageFormat.JPEG)[0]; //SurfaceTexture returns the runtime class of this object
        //immutable class for describing width and height dimensions in pixels

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return;
        }

        manager.openCamera(cameraID, stateCallBack, null);
        //opens a connection to a camera with the given ID

    }

    private void takePicture() throws CameraAccessException {
        Log.i("TAKE PICTURE", "HERE");
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
            width = jpegSizes[0].getWidth(); //this actually matches the open camera hence the sizes end up being same.. the index 0 is same as that in opencamera!!!!
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces  = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(cameraView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getWindowManager().getDefaultDisplay().getRotation(); //gets the current orientation of the device
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener(){
            public void onImageAvailable(ImageReader reader){
                Image image = null;
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                if(photoSendThread != null){
                    //Toast.makeText(getApplicationContext(), "SENDING", Toast.LENGTH_SHORT).show();
                    photoSendThread.setByteArray(bytes);
                }
            }
        };

        reader.setOnImageAvailableListener(readerListener, backgroundHandler);

        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                //Toast.makeText(getApplicationContext(), "Captured", Toast.LENGTH_LONG).show();
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


    //Called when the activity will start interacting with the user. At this point your activity is at the top of its activity stack, with user input going to it.
    protected void onResume(){
        super.onResume();
        startBackgroundThread();
        if(cameraView.isAvailable()){ //returns true if the surfacetexture associated with this textureview is available for rendering
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

    //Called when the activity loses foreground state, is no longer focusable or before transition to stopped/hidden or destroyed state.
    // The activity is still visible to user, so it's recommended to keep it visually active and continue updating the UI.
    // Implementations of this method must be very quick because the next activity will not be resumed until this method returns.
    //Followed by either onResume() if the activity returns back to the front, or onStop() if it becomes invisible to the user.
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
