package abz.chand.supermarketassistant.guide;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import abz.chand.supermarketassistant.R;
import android.app.Activity;
import android.graphics.Bitmap;
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
		
		ProcessFrame processFrame = CameraFrameData.processFrame;
		
		int width = processFrame.getWidth();//320;//1280;//1920;
		int height = processFrame.getHeight();//240;//720;//1080;		
		
		long time = System.currentTimeMillis();
		Mat mat = processFrame.getStickerData(data);
		System.out.println("TimeLeft: " + (System.currentTimeMillis() - time));		
		
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat, bmp);

		data = null;
		ImageView imageView = (ImageView) findViewById(R.id.image);
		imageView.setImageBitmap(bmp);				
	}
}
