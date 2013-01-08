package abz.chand.supermarketassistant.guide;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class SupermarketGuide extends Activity implements SensorEventListener {
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
    private final float[] mRotationMatrix = new float[16];
    private final float[] mOrientation = new float[4];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
				
				PreviewOverlay previewOverlay = new PreviewOverlay(getApplicationContext(), processFrame);
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
	private PreviewOverlay previewOverlay;


	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
		
	}
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
//		mSensorManager.unregisterListener(this);
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , event.values);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);
            double z = Math.round(Math.toDegrees(mOrientation[0]));
            previewOverlay.setZ(z);
            double x = Math.round(Math.toDegrees(mOrientation[1]));
            double y = Math.round(Math.toDegrees(mOrientation[2]));
            System.out.println("Kasru: " + x + ", " + y + ", " + z);
            //System.out.println("Kasru: " + mRotationMatrix[0] + ", " +  mRotationMatrix[1] + ", " +  mRotationMatrix[2] + ", " +  mRotationMatrix[3] + ", " +  mRotationMatrix[4] + ", " +  mRotationMatrix[5] + "," + event.values.length );
        }
		
	}


}

