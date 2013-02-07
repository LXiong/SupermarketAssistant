package abz.chand.supermarketassistant.guide;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import abz.chand.supermarketassistant.guide.gl.MyGLRenderer;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class SupermarketGuide extends Activity{
	
	private MyGLSurfaceView mGLSurfaceView;
	private SensorManager sensorManager;    
	private PreviewOverlay previewOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//mGLSurfaceView =  new MyGLSurfaceView(this, sensorManager);
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				
				ProcessFrame processFrame = new ProcessFrame();
				CameraPreview cameraPreview = new CameraPreview(getApplicationContext(), processFrame);			
				setContentView(cameraPreview);				
				
//				addContentView(mGLSurfaceView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));		
				previewOverlay = new PreviewOverlay(getApplicationContext(), processFrame, sensorManager);
				previewOverlay.startSensor();
				addContentView(previewOverlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//				setContentView(R.layout.camerapreview);
							
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        previewOverlay.stopSensor();
//        mGLSurfaceView.onPause();
    }

	
	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
//		previewOverlay.startSensor();
//        mGLSurfaceView.onResume();
		
	}
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		previewOverlay.stopSensor();
//		mGLSurfaceView.stopSensor();
	}
	
}


class MyGLSurfaceView extends GLSurfaceView {

    private MyGLRenderer myGLRenderer;

	public MyGLSurfaceView(Context context, SensorManager sensorManager) {
        super(context);

        setEGLContextClientVersion(2);

        setZOrderMediaOverlay(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        myGLRenderer = new MyGLRenderer(sensorManager);
        myGLRenderer.startSensor();
        setRenderer(myGLRenderer);
                
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        myGLRenderer.startSensor();
    }
	
	@Override
	public void onPause() {
		super.onPause();
    	myGLRenderer.stopSensor();
	}
    
    public void stopSensor(){
    	myGLRenderer.stopSensor();
    }
}
