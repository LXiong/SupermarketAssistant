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
	private double angleZ;
	private double angleX;
	private double angleY;
	private SensorManager sensorManager;
	private long xs;
	private long xss;

	public RotationDetection(SensorManager sensorManager) {
		this.sensorManager = sensorManager;
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		//		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}

	public void startSensor(){
		sensorManager.registerListener(this, mSensor, 1000);
	}

	public void stopSensor(){
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
		//			double x = event.values[0];
		//			double y = event.values[1];
		//			double z = event.values[2];
		//			if (z > 4){
		//				System.out.println("Delna: " + x + ", " + y + ", " + z);
		//			}
		//		}

		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(
					mRotationMatrix , event.values);
			SensorManager.getOrientation(mRotationMatrix, mOrientation);
			double z = Math.round(Math.toDegrees(mOrientation[0]));
			double x = Math.round(Math.toDegrees(mOrientation[1]));
			double y = Math.round(Math.toDegrees(mOrientation[2]));
			angleZ = z;
			angleX = x;
			angleY = y;
			
			double ap = Math.abs(Math.toDegrees(mOrientation[1])) + 28.5;
			double am = Math.abs(Math.toDegrees(mOrientation[1])) - 28.5;
		
				
			xs = Math.round(Math.tan(Math.abs(mOrientation[1])) * 105);
			xss = Math.round(Math.tan(Math.toRadians(ap)) * 120) - Math.round(Math.tan(Math.toRadians(am)) * 120);
//			System.out.println("Distance: " + Math.tan(mOrientation[1]) * 105);
//			System.out.println("Kasru: " + x + ", " + y + ", " + z);
			//System.out.println("Kasru: " + mRotationMatrix[0] + ", " +  mRotationMatrix[1] + ", " +  mRotationMatrix[2] + ", " +  mRotationMatrix[3] + ", " +  mRotationMatrix[4] + ", " +  mRotationMatrix[5] + "," + event.values.length );
		}
		
		// 290 -> 330 = 40
		// 223 -> 260 = 37
		// 177 -> 202 = 25
		// 90 -> 109 = 19
		// Difference increases but the rate at which it increases decreases
		// x^2 - x
		
		// 16* halfway correct
	}

	public double getAngleZ(){
		if (angleZ < 0){
			return 360 + angleZ;
		}
		return angleZ;
	}

	public double getXs(){
		return xs;
	}

	
	public double getAngleX(){
		if (angleX < 0){
			//			return 360 + angleX;
		}
		return angleX;
	}

	public double getAngleY(){
		if (angleY < 0){
			//return 360 + angleY;
		}
		return angleY;
	}

	public long getXss() {
		return xss;
	}
}
