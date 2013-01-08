package abz.chand.supermarketassistant.guide.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MovementDetection implements SensorEventListener {

	private Sensor mSensor;
	private final float[] mRotationMatrix = new float[16];
	private final float[] mOrientation = new float[4];
	private float[] gravity;
	private float[] linear_acceleration;

	public MovementDetection(SensorManager sensorManager) {
		gravity = new float[3];
		linear_acceleration = new float[3];
		
		gravity[0] = 0f;
		gravity[1] = 0f;
		gravity[2] = 0f;
		
		
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		sensorManager.registerListener(this, mSensor, 1000);	
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

			final float alpha = 0.8f;

			// Isolate the force of gravity with the low-pass filter.
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			// Remove the gravity contribution with the high-pass filter.
			linear_acceleration[0] = event.values[0] - gravity[0];
			linear_acceleration[1] = event.values[1] - gravity[1];
			linear_acceleration[2] = event.values[2] - gravity[2];

			double z = linear_acceleration[0];
			double x = linear_acceleration[1];
			double y = linear_acceleration[2];

			System.out.println("linear: " + z + ", " + x + ", " + y);
		}
	}

}
