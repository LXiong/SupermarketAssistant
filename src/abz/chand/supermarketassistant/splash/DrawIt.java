package abz.chand.supermarketassistant.splash;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawIt extends View {

	private ArrayList<Path> graphics;

	private ArrayList<Float> xs;
	private ArrayList<Float> ys;
		
	private static Path path;

	public DrawIt(Context context) {
		super(context);
		init();
	}

	public DrawIt(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DrawIt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		path = new Path();
		graphics = new ArrayList<Path>();
		xs = new ArrayList<Float>();
		ys = new ArrayList<Float>();
	}

	public ArrayList<Path> getPaths(){
		return graphics;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		int eventaction = event.getAction();
		final float newX = event.getX();
		final float newY = event.getY();	

		switch (eventaction){

		case MotionEvent.ACTION_DOWN:
			path.moveTo(newX, newY);
			return true;

		case MotionEvent.ACTION_MOVE:
			xs.add(newX);
			ys.add(newY);
			path.lineTo(newX, newY);
			path.moveTo(newX, newY);
			return true;

		case MotionEvent.ACTION_UP:
			String sx = "XXX: ";
			String sy = "YYY: ";
			for(float f : xs){
				sx += f + ",";
			}
			for(float f : ys){
				sy += f + ",";
			}
			System.out.println(sx);
			xs.clear();
			ys.clear();
			System.out.println(sy);
			
			graphics.add(path);
			path = new Path();
			return true;
		}

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawARGB(255, 255, 255, 255);
		Paint l = new Paint();
		l.setStyle(Paint.Style.STROKE);
		l.setStrokeWidth(10);
		l.setDither(true);
		l.setStrokeCap(Paint.Cap.ROUND);
		l.setStrokeJoin(Paint.Join.ROUND);
		l.setARGB(255, 0, 0, 0);

		for (Path pa : graphics){
			canvas.drawPath(pa, l);
		}

		canvas.drawPath(path, l);
		
		invalidate();
	}
	
	public void undoPath(){
		if (graphics.isEmpty()) return;
		graphics.remove(graphics.size()-1);
	}
		
	public Bitmap getBitmapView(){
		setDrawingCacheEnabled(true);
		Bitmap copy = Bitmap.createBitmap(getDrawingCache());
		setDrawingCacheEnabled(false);
		return copy;
	}
}