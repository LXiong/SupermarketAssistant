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
	
	private GLSurfaceView mGLSurfaceView;
	private SensorManager sensorManager;    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mGLSurfaceView =  new MyGLSurfaceView(this, sensorManager);
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

			
				PreviewOverlay previewOverlay = new PreviewOverlay(getApplicationContext(), processFrame, sensorManager);
				addContentView(previewOverlay, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//				setContentView(R.layout.camerapreview);
				
//				previewOverlay = (PreviewOverlay) findViewById(R.id.previewOverlay);
//				mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//				mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//				mSensorManager.registerListener(this, mSensor, 1000);				
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
        mGLSurfaceView.onPause();
    }

	
	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);

        mGLSurfaceView.onResume();
		
	}
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
//		mSensorManager.unregisterListener(this);
	}
	

//	public void onSensorChanged(SensorEvent event) {
//
//        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
//            SensorManager.getRotationMatrixFromVector(
//                    mRotationMatrix , event.values);
//            SensorManager.getOrientation(mRotationMatrix, mOrientation);
//            double z = Math.round(Math.toDegrees(mOrientation[0]));
//            previewOverlay.setZ(z);
//            double x = Math.round(Math.toDegrees(mOrientation[1]));
//            double y = Math.round(Math.toDegrees(mOrientation[2]));
//            System.out.println("Kasru: " + x + ", " + y + ", " + z);
//            //System.out.println("Kasru: " + mRotationMatrix[0] + ", " +  mRotationMatrix[1] + ", " +  mRotationMatrix[2] + ", " +  mRotationMatrix[3] + ", " +  mRotationMatrix[4] + ", " +  mRotationMatrix[5] + "," + event.values.length );
//        }
//		
//	}


}


class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context, SensorManager sensorManager) {
        super(context);

        setEGLContextClientVersion(2);

        setZOrderMediaOverlay(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new MyGLRenderer(sensorManager));
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
