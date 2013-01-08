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

public class MyRenderer implements GLSurfaceView.Renderer, SensorEventListener {
	private Line line;
	private Sensor mRotationVectorSensor;
	private final float[] mRotationMatrix = new float[16];
	private SensorManager mSensorManager;
	private float[] mViewMatrix = new float[16];
	private float[] mModelMatrix = new float[16];

	public MyRenderer(SensorManager sensorManager) {
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

		line.draw();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	class Line {
		private final String vertexShaderCode =
			"attribute vec4 vPosition;" +
			"void main() {" +
			"  gl_Position = vPosition;" +
			"}";

		private final String fragmentShaderCode =
			"precision mediump float;" +
			"uniform vec4 vColor;" +
			"void main() {" +
			"  gl_FragColor = vColor;" +
			"}";

		private final FloatBuffer vertexBuffer;
		private final int mProgram;
		private int mPositionHandle;
		private int mColorHandle;

		// number of coordinates per vertex in this array
		static final int COORDS_PER_VERTEX = 3;
		final float triangleCoords[] = { // in counterclockwise order:
				0.0f,  0.622008459f, 0.0f,   // top
				-0.5f, -0.311004243f, 0.0f,   // bottom left
				0.5f, -0.311004243f, 0.0f    // bottom right
		};
		private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
		private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex

		// Set color with red, green, blue and alpha (opacity) values
		float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };


		public Line() {
			ByteBuffer bb = ByteBuffer.allocateDirect(
					// (number of coordinate values * 4 bytes per float)
					triangleCoords.length * 4);
			// use the device hardware's native byte order
			bb.order(ByteOrder.nativeOrder());

			// create a floating point buffer from the ByteBuffer
			vertexBuffer = bb.asFloatBuffer();
			// add the coordinates to the FloatBuffer
			vertexBuffer.put(triangleCoords);
			// set the buffer to read the first coordinate
			vertexBuffer.position(0);

			// prepare shaders and OpenGL program
			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
					vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
					fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);        

		}

		public int loadShader(int type, String shaderCode){
			int shader = GLES20.glCreateShader(type);

			GLES20.glShaderSource(shader, shaderCode);
			GLES20.glCompileShader(shader);

			return shader;
		}

		public void draw() {
	        // Add program to OpenGL environment
	        GLES20.glUseProgram(mProgram);

	        // get handle to vertex shader's vPosition member
	        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

	        // Enable a handle to the triangle vertices
	        GLES20.glEnableVertexAttribArray(mPositionHandle);

	        // Prepare the triangle coordinate data
	        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
	                                     GLES20.GL_FLOAT, false,
	                                     vertexStride, vertexBuffer);

	        // get handle to fragment shader's vColor member
	        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

	        // Set color for drawing the triangle
	        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	        // Draw the triangle
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

	        // Disable vertex array
	        GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
