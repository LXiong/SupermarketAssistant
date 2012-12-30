package abz.chand.supermarketassistant.guide;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Parameters parameters;
	private Size previewSize; 

	public CameraPreview(Context context) {
		super(context);
		init();		
	}

	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init(){
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);

			mCamera.setPreviewCallback(this);

			parameters = mCamera.getParameters();
			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();    
			previewSize = previewSizes.get(0);
			System.out.println("W: " + previewSize.width + " , H: " + previewSize.height);

		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		parameters.setPreviewSize(previewSize.width, previewSize.height);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
		//mCamera.autoFocus(myAutoFocusCallback); 
	}

	public boolean onTouchEvent(MotionEvent event){
		int eventaction = event.getAction();
		switch (eventaction){
		case MotionEvent.ACTION_DOWN:
			mCamera.autoFocus(myAutoFocusCallback);
			return true;
		}

		invalidate();
		return true;
	}


	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		CameraFrameData.data = data;
	}

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			mCamera.takePicture(shutter, rawPic, jpeg);
			//	mCamera.autoFocus(myAutoFocusCallback);
		}

	};

	ShutterCallback shutter = new ShutterCallback(){
		@Override
		public void onShutter() {
		}
	};

	PictureCallback rawPic = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
		}
	};

	PictureCallback jpeg = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] data, Camera arg1) {
			System.out.println("Start: " + System.currentTimeMillis());
			pictureTakenPreview();
			closeCam();
		}
	};

	public void closeCam(){
		mCamera.stopPreview();
	}

	public void pictureTakenPreview(){
		Context context = getContext();
		Intent intent = new Intent(context, PictureTakenPreview.class);
		context.startActivity(intent);
	}
}
