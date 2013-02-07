package abz.chand.supermarketassistant.guide.frameprocessing;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MatConverter {

	private int width;
	private int height;
	
	public MatConverter(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Mat convertDataToMat(byte[] data) {
		Mat matYuv = new Mat(height + height/2, width, CvType.CV_8UC1);
		matYuv.put(0, 0, data);
		return matYuv;
	}
	
	public Mat convertYuvToRGBA(Mat matYuv){
		Mat mat = new Mat();
		Imgproc.cvtColor(matYuv, mat, Imgproc.COLOR_YUV2RGBA_NV21, 4);
		return mat;
	}
	
	public Mat convertYuvToRgb(Mat matYuv){
		Mat mat = new Mat();
		Imgproc.cvtColor(matYuv, mat, Imgproc.COLOR_YUV2RGB_NV21);
		return mat;
	}
	
	public Mat convertRgbToGray(Mat rgbYuv){
		Mat mat = new Mat();
		Imgproc.cvtColor(rgbYuv, mat, Imgproc.COLOR_RGB2GRAY);
		return mat;
	}
	
	public Mat convertYuvToRgba(Mat matYuv){
		Mat mat = new Mat();
		Imgproc.cvtColor(matYuv, mat, Imgproc.COLOR_YUV2RGBA_NV21);
		return mat;
	}
	
	public Mat convertRgbToHsv(Mat matRgb){
		Mat mat = new Mat();
		Imgproc.cvtColor(matRgb, mat, Imgproc.COLOR_RGB2HSV);
		return mat;
	}

	public Mat convertYuvToBgr(Mat matYuv){
		Mat mat = new Mat();
		Imgproc.cvtColor(matYuv, mat, Imgproc.COLOR_YUV2BGR_NV21);
		return mat;
	}
	
	public Mat convertBgrToHsv(Mat matBgr){
		Mat mat = new Mat();
		Imgproc.cvtColor(matBgr, mat, Imgproc.COLOR_BGR2HSV);
		return mat;
	}
	
	public Mat convertRgbaToBgr(Mat matBgr){
		Mat mat = new Mat();
		Imgproc.cvtColor(matBgr, mat, Imgproc.COLOR_RGBA2BGR);
		return mat;
	}
}
