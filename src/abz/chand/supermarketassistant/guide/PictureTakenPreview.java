package abz.chand.supermarketassistant.guide;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import abz.chand.supermarketassistant.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PictureTakenPreview extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.picturetakenpreview);

		byte[] data = CameraFrameData.data;
				
		long time = System.currentTimeMillis();	
		int width = 1920;
		int height = 1080;
		
		Mat my = new Mat(height + height/2, width, CvType.CV_8UC1);
		my.put(0, 0, data);
				
		Mat mat2 = new Mat();
		Imgproc.cvtColor(my, mat2, Imgproc.COLOR_YUV2RGBA_NV21, 4);
				
		System.out.println("Time: " + (System.currentTimeMillis() - time));
		
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat2, bmp);

		data = null;
		ImageView imageView = (ImageView) findViewById(R.id.image);
		imageView.setImageBitmap(bmp);				
	}
}
