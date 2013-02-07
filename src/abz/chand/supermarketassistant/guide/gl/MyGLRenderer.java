package abz.chand.supermarketassistant.guide.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer, SensorEventListener {

	private static final String TAG = "MyGLRenderer";
	private Square mSquare;

	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjMatrix = new float[16];
	private final float[] mVMatrix = new float[16];
	private final float[] mRotationMatrix = new float[16];

	// Declare as volatile because we are updating it from another thread
	public volatile float mAngle;
	private Sensor sensor;
	private SensorManager sensorManager;

	public MyGLRenderer(SensorManager sensorManager) {
		this.sensorManager = sensorManager;
		sensor = sensorManager.getDefaultSensor(
				Sensor.TYPE_ROTATION_VECTOR);
	}

	public void startSensor(){
		//sensorManager.registerListener(this, sensor, 1000);
	}

	public void stopSensor(){
		//sensorManager.unregisterListener(this);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		mSquare = new Square();
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		Matrix.setLookAtM(mVMatrix, 0, 0, 0, 0, 0f, 0f, -200f, 0f, 1.0f, 0.0f);	
		//		Matrix.setLookAtM(mVMatrix, 0, 0, -3, 0.0f, 0f, 0f, -50f, 0f, 1.0f, 0.0f);
		//		Matrix.setLookAtM(mVMatrix, 0, 0, 0, 0.0f, mRotationMatrix[0], mRotationMatrix[4], -mRotationMatrix[8], 0f, 1.0f, 0.0f);

		//        Matrix.setLookAtM(rm, rmOffset, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

		Matrix.setRotateM(rotationToMakeItLookLikeAnArrowMatrix, 0, 20, 0f, 1f, 0.0f);
		Matrix.multiplyMM(mMVPMatrix, 0, rotationToMakeItLookLikeAnArrowMatrix, 0, mMVPMatrix, 0);



		System.out.println("Nick: " + angle);
		//				float t = 270 - Math.round(Math.toDegrees(orientationMatrix[2]));
		//		if (t < 0){
		//			t+=360;
		//		}
		//		Matrix.setRotateM(rotationMatrix, 0, t, 0f, 0f, 1.0f);
		//		Matrix.setRotateM(rotationMatrix, 0, 225, -3f, 0f, 1.0f);

		// Draw square
		// Create a rotation for the triangle
		//        long time = SystemClock.uptimeMillis() % 4000L;
		//        float angle = 0.090f * ((int) time);
		//		Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);		
		//		Matrix.transposeM(mRotationMatrix, 0, mRotationMatrix, 0);
		//		Matrix.multiplyMM(mMVPMatrix, 0, rotationMatrix, 0, mMVPMatrix, 0);

		long time = SystemClock.uptimeMillis() % 4000L;
		float angle = 0.090f * ((int) time);
		Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, 1.0f);


		Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

		String str = "";
		for (int i = 0; i < 16; i++){
			str += mMVPMatrix[i] + ",";
		}
		//		System.out.println("Dabang: " + str);

		mSquare.draw(mMVPMatrix);

	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 50);

	}

	public static int loadShader(int type, String shaderCode){
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private float[] rotationMatrix = new float[16];
	private float[] rotation2Matrix = new float[16];
	private float[] rotationToMakeItLookLikeAnArrowMatrix = new float[16];
	private float[] orientationMatrix = new float[16];
	private long angle = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(
					mRotationMatrix , event.values);
			SensorManager.getOrientation(mRotationMatrix, orientationMatrix);

			//			SensorManager.getOrientation(rotationMatrix, orientationMatrix);
			//			angle  = Math.round(Math.toDegrees(orientationMatrix[0]));
			String str = "";
			for (int i = 0; i < 16; i++){
				str += mRotationMatrix[i] + ",";
			}
			System.out.println("Basru: " + str);
		}
	}
}


class Square {

	private final String vertexShaderCode =
		// This matrix member variable provides a hook to manipulate
		// the coordinates of the objects that use this vertex shader
		"uniform mat4 uMVPMatrix;" +

		"attribute vec4 vPosition;" +
		"void main() {" +
		// the matrix must be included as a modifier of gl_Position
		"  gl_Position = vPosition * uMVPMatrix;" +
		"}";

	private final String fragmentShaderCode =
		"precision mediump float;" +
		"uniform vec4 vColor;" +
		"void main() {" +
		"  gl_FragColor = vColor;" +
		"}";

	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = {
		5000f, -20.0f, -200.0f,   // bottom right
		5000f, 20.0f, -200.0f,  // bottom left
		0f,  -20.00f, -20.0f,   // top right
		0f, 20.0f, -20.0f};   // top left


	//	static float squareCoords[] = { 100f,  5000.00f, -100.0f,   // top left
	//		100f, 0.0f, -100.0f,   // bottom left
	//		130f, 0.0f, -100.0f,   // bottom right
	//		130f,  5000.00f, -100.0f }; // top right

	//	static float squareCoords[] = { -15f,  -15.00f, 0.0f,   // top left
	//		-15f, 15.0f, 0.0f,   // bottom left
	//		15f, 15.0f, 0.0f,   // bottom right
	//		15f,  -15.00f, 0.0f }; // 

	private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	public Square() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
				// (# of coordinate values * 4 bytes per float)
				squareCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
				// (# of coordinate values * 2 bytes per short)
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		// prepare shaders and OpenGL program
		int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
	}

	public void draw(float[] mvpMatrix) {
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

		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		MyGLRenderer.checkGlError("glGetUniformLocation");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		MyGLRenderer.checkGlError("glUniformMatrix4fv");

		// Draw the square
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}

