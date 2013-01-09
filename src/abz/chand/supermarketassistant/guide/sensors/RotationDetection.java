package abz.chand.supermarketassistant.guide.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RotationDetection implements SensorEventListener {

	private Sensor mSensor;
	private final float[] mRotationMatrix = new float[16];
	private final float[] mOrientation = new float[4];
	private double angle;

	public RotationDetection(SensorManager sensorManager) {
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		sensorManager.registerListener(this, mSensor, 1000);	
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
			double x = Math.round(Math.toDegrees(mOrientation[1]));
			double y = Math.round(Math.toDegrees(mOrientation[2]));
			angle = z;
			System.out.println("Kasru: " + x + ", " + y + ", " + z);
			//System.out.println("Kasru: " + mRotationMatrix[0] + ", " +  mRotationMatrix[1] + ", " +  mRotationMatrix[2] + ", " +  mRotationMatrix[3] + ", " +  mRotationMatrix[4] + ", " +  mRotationMatrix[5] + "," + event.values.length );
		}
	}
	
	public double getAngle(){
		if (angle < 0){
			return 360 + angle;
		}
		return angle;
	}
}
