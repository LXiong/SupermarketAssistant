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
	private ProcessFrame processFrame; 

	public CameraPreview(Context context) {
		super(context);
		init();		
	}

	public CameraPreview(Context context, ProcessFrame processFrame) {
		super(context);
		this.processFrame =  processFrame;
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

			parameters = mCamera.getParameters();
			setPreviewSize(parameters.getSupportedPreviewSizes());
//			processFrame = new ProcessFrame(previewSize.width, previewSize.height);
			processFrame.setUpSize(previewSize.width, previewSize.height);

			mCamera.setPreviewCallbackWithBuffer(this);
			mCamera.addCallbackBuffer(new byte[previewSize.width * previewSize.height * 3 / 2 + 1]);
			
			CameraFrameData.processFrame = processFrame;
			
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	private void setPreviewSize(List<Camera.Size> previewSizes){
//		int resolution = 320 * 240;
//		int resolution = 1920 * 1080;
		int resolution = 640 * 480;
		
		int closestIndex = -1;
		int minDiffResolution = Integer.MAX_VALUE;
		
		for (int i = 0; i < previewSizes.size(); i++){
			Camera.Size size = previewSizes.get(i);	
			
			int currentResolution = size.width * size.height;
			int currentDiffResolution = Math.abs(resolution - currentResolution);
			
			if (currentDiffResolution < minDiffResolution){
				minDiffResolution = currentDiffResolution;
				closestIndex = i;
			}
			System.out.println("W: " + size.width + " H: " + size.height);
		}
		
		previewSize = previewSizes.get(closestIndex);	
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
		System.out.println("WHWHWHHWHWHWHW");
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

	boolean what = true;
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		processFrame.getStickerDataString(data);
//		System.out.println("POPOPOPOP");
//		if (what){
//			CameraFrameData.data = data;
//		}
		mCamera.addCallbackBuffer(data);
	}

	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
//			what = false;
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
