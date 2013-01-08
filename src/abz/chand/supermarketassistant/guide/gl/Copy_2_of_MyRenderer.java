package abz.chand.supermarketassistant.guide.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class Copy_2_of_MyRenderer implements GLSurfaceView.Renderer, SensorEventListener {
	private Line line;
	private Sensor mRotationVectorSensor;
	private final float[] mRotationMatrix = new float[16];
	private SensorManager mSensorManager;
	private float[] mViewMatrix = new float[16];
	private float[] mModelMatrix = new float[16];

	public Copy_2_of_MyRenderer(SensorManager sensorManager) {
		mSensorManager = sensorManager;
		// find the rotation-vector sensor
		mRotationVectorSensor = mSensorManager.getDefaultSensor(
				Sensor.TYPE_ROTATION_VECTOR);

		line = new Line();

		// initialize the rotation matrix to identity
		mRotationMatrix[ 0] = 1;
		mRotationMatrix[ 4] = 1;
		mRotationMatrix[ 8] = 1;
		mRotationMatrix[12] = 1;

	
	}

	public void start() {
		// enable our sensor when the activity is resumed, ask for
		// 10 ms updates.
		mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
	}

	public void stop() {
		// make sure to turn our sensor off when the activity is paused
		mSensorManager.unregisterListener(this);
	}

	public void onSensorChanged(SensorEvent event) {
		// we received a sensor event. it is a good practice to check
		// that we received the proper event
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			// convert the rotation-vector to a 4x4 matrix. the matrix
			// is interpreted by Open GL as the inverse of the
			// rotation-vector, which is what we want.
			SensorManager.getRotationMatrixFromVector(
					mRotationMatrix , event.values);
		}
	}

	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);

		line.draw();
	}

	private float[] mProjectionMatrix = new float[16];
	private int mMVPMatrixHandle;
	private int mPositionHandle;
	private int mColorHandle;

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// set view-port
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.5f;

		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
	
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = 1;        
        mPositionHandle = 1;
        mColorHandle = 1;        
        
        // Tell OpenGL to use this program when rendering.
//        GLES20.glUseProgram(programHandle);        
	}

	class Line {
		private FloatBuffer mLineBuffer;

		private final int mBytesPerFloat = 4;
		
		private float[] mMVPMatrix = new float[16];
		 
		private final int mStrideBytes = 7 * mBytesPerFloat;
		 
		private final int mPositionOffset = 0;
		 
		private final int mPositionDataSize = 3;
		 
		private final int mColorOffset = 3;
		 
		private final int mColorDataSize = 4;
		
		public Line() {
			final float[] lineVerticesData = {
					// X, Y, Z,
					// R, G, B, A
					-0.5f, -0.25f, 0.0f,
					1.0f, 0.0f, 0.0f, 1.0f,

					0.5f, -0.25f, 0.0f,
					1.0f, 0.0f, 0.0f, 1.0f};

			mLineBuffer = ByteBuffer.allocateDirect(lineVerticesData.length * mBytesPerFloat)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mLineBuffer.put(lineVerticesData).position(0);
		}
		public void draw() {
			mLineBuffer.position(mPositionOffset);
	        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
	        		mStrideBytes, mLineBuffer);        
	                
	        GLES20.glEnableVertexAttribArray(mPositionHandle);        
	        
	        // Pass in the color information
	        mLineBuffer.position(mColorOffset);
	        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
	        		mStrideBytes, mLineBuffer);        
	        
	        GLES20.glEnableVertexAttribArray(mColorHandle);
	        
			// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
	        // (which currently contains model * view).
	        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
	        
	        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	        // (which now contains model * view * projection).
	        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);  
			
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
