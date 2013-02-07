package abz.chand.supermarketassistant.guide;

import org.opencv.core.Point;

import abz.chand.supermarketassistant.guide.sensors.MovementDetection;
import abz.chand.supermarketassistant.guide.sensors.RotationDetection;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

public class PreviewOverlay extends View {

	private int width;
	private int height;
	private Point startDrawPoint;
	private Point endDrawPoint;
	private Paint paint;
	private ProcessFrame processFrame;
	private MovementDetection movementDetection;
	private RotationDetection rotationDetection;
	private SensorManager sensorManager;
	private Point initialDrawPoint;	

	public PreviewOverlay(Context context, ProcessFrame processFrame, SensorManager sensorManager) {
		super(context);		
		this.processFrame = processFrame;
		this.sensorManager = sensorManager;

		movementDetection = new MovementDetection(sensorManager);
		rotationDetection = new RotationDetection(sensorManager);
	}

	public PreviewOverlay(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PreviewOverlay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void startSensor(){
		rotationDetection.startSensor();
	}

	public void stopSensor(){
		rotationDetection.stopSensor();
	}

	public void init(){
		initialDrawPoint = new Point(width/2, height/2);
		startDrawPoint = new Point(width/2, height/2);
		endDrawPoint = new Point(width/2, height/2);
		setAngle(270);
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setDither(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setARGB(255, 0, 255, 0);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		Pair<Point, Point> freeSpaceDirection = processFrame.getFreeSpaceDirection();
		
		if (freeSpaceDirection != null){
			paint.setARGB(255, 255, 0, 0);
			
			drawLine(freeSpaceDirection.first, freeSpaceDirection.second);
			
			canvas.drawLine((float) freeSpaceDirection.first.x, (float) freeSpaceDirection.first.y, 
					(float) freeSpaceDirection.second.x, (float) freeSpaceDirection.second.y, paint);
		}
		
		/* UNCOMMENT FOR THE SCANNER
		setCenterPoint(processFrame.getCenterPoint());

		paint.setARGB(255, 0, 0, 255);
		canvas.drawCircle((float) startDrawPoint.x, (float) startDrawPoint.y, 3, paint);

		paint.setARGB(255, 255, 255, 0);
	
		int val = processFrame.getActualValue();
		if (val > 0){
			String str = "Value: " + val;
			paint.setStrokeWidth(1);
			canvas.drawText(str, 100, 100, paint);
		}
		 */


		//		setAngle(processFrame.getAngle());
		//		setAngle(0);

		//		setNormalAngle(rotationDetection.getAngle());
		//		paint.setARGB(255, 255, 0, 0);
		//		canvas.drawLine((float) startDrawPoint.x, (float) startDrawPoint.y, (float) endDrawPoint.x, (float) endDrawPoint.y, paint);

		//		setAngle(rotationDetection.getAngleZ());
		//		setHorizontal(rotationDetection.getAngleY());
		//		setVertical(rotationDetection.getAngleX());

		//		paint.setARGB(255, 0, 255, 0);
		//		canvas.drawLine((float) startDrawPoint.x, (float) startDrawPoint.y, (float) endDrawPoint.x, (float) endDrawPoint.y, paint);
		//		
		//		paint.setARGB(255, 0, 0, 255);
		//		canvas.drawCircle((float) endDrawPoint.x, (float) endDrawPoint.y, 3, paint);


		float degree = (float) rotationDetection.getAngleX();
		int hh = height/2;
		int wws = (width/2);
		float ww = (float) (0.5 * (16+degree) + wws);

		System.out.println("Degree: " + degree);	

		//		paint.setARGB(255, 255, 0, 0);
		//		canvas.drawCircle(ww, hh, 3, paint);
		//
		//		
		//		paint.setARGB(255, 255, 255, 0);
		//		canvas.drawLine(width-200, 0, width-200, height, paint);
		//
		//		paint.setARGB(255, 255, 255, 0);
		//		canvas.drawLine(width-75-200, height/2, width-200, height/2, paint);


		//		String str = "Distance: " + rotationDetection.getXs() + " with angle: " + rotationDetection.getAngleX();
		//		String str = "Distance: " + rotationDetection.getXss() + " with angle: " + rotationDetection.getAngleX();
		//		paint.setStrokeWidth(1);
		//		canvas.drawText(str, 100, 100, paint);

		//		paint.setARGB(255, 0, 0, 255);
		//		canvas.drawLine((float) startDrawPoint.x, (float) startDrawPoint.y, 0, (float) height/2, paint);

		invalidate();
	}

	private void setVertical(double angle) {
		startDrawPoint.x = (initialDrawPoint.x + (-40-angle)*10);

	}	

	private void setCenterPoint(Point centerPoint) {		
		double widthRatio = width/ (double) processFrame.getWidth();
		double heightRatio = height/ (double) processFrame.getHeight();
		startDrawPoint.x = centerPoint.x * widthRatio;
		startDrawPoint.y = centerPoint.y * heightRatio;
	}

	
	private void drawLine(Point l1, Point l2){
		double widthRatio = width / (double) processFrame.getWidth();
		double heightRatio = height / (double) processFrame.getHeight();
//		l1.x *= widthRatio;
//		l1.y *= heightRatio;
//		l2.x *= widthRatio;
//		l2.y *= heightRatio;		
	}
	
	public void setHorizontal(double angle){
		startDrawPoint.y = (initialDrawPoint.y - angle*10);
		System.out.println("Matki:"  + startDrawPoint.y + "=" + initialDrawPoint.y + "+" + angle);
	}

	public void setNormalAngle(double angle){
		int length = width/4;
		double radians = Math.toRadians(angle);
		double x = Math.cos(radians) * length;
		double y = Math.sin(radians) * length;
		endDrawPoint.x = startDrawPoint.x + y;
		endDrawPoint.y = startDrawPoint.y - x;
	}

	public void setAngle(double angle){
		int length = width/4;
		double k = 315 - angle;
		if (k < 0){
			k = 360 + k;
		}
		System.out.println("Shit::" + angle + ", " + k);
		double radians = Math.toRadians(k);
		double x = Math.cos(radians) * length;
		double y = Math.sin(radians) * length;
		endDrawPoint.x = startDrawPoint.x + y;
		endDrawPoint.y = startDrawPoint.y - x;
	}

	public void setZ(double z) {
		//setAngle()
		if (startDrawPoint != null && endDrawPoint != null){
			setAngle(z);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		width = w;
		height = h;
		init();	
	}
}
