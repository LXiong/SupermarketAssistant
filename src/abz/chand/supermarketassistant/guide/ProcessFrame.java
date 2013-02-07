package abz.chand.supermarketassistant.guide;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import abz.chand.supermarketassistant.guide.dataholder.StickerData;
import abz.chand.supermarketassistant.guide.frameprocessing.AngleCorrection;
import abz.chand.supermarketassistant.guide.frameprocessing.AngleDetection;
import abz.chand.supermarketassistant.guide.frameprocessing.CircleDetection;
import abz.chand.supermarketassistant.guide.frameprocessing.MatConverter;
import abz.chand.supermarketassistant.guide.freespace.FreeSpaceTrial;
import android.util.Pair;

public class ProcessFrame {

	private static final int MAX_CONSISTENT_FRAMES = 2;

	private int width;
	private int height;
	private MatConverter matConverter;
	private CircleDetection circleDetection;
	private AngleCorrection angleCorrection;
	private StickerData[] frameData;
	private StickerData latestAccurateStickerData;
	private int frameDataIndex;

	private AngleDetection angleDetection;
	private FreeSpaceTrial freeSpace;

	private Pair<Point, Point> directionLine;

	public ProcessFrame(){
		circleDetection = new CircleDetection();
		angleDetection = new AngleDetection();
		angleCorrection = new AngleCorrection();
		frameData = new StickerData[MAX_CONSISTENT_FRAMES];
		init();
	}

	public ProcessFrame(int width, int height){
		this.width = width;
		this.height = height;	
		matConverter = new MatConverter(width, height);
		freeSpace = new FreeSpaceTrial(width, height);
		circleDetection = new CircleDetection();
		angleCorrection = new AngleCorrection();
		frameData = new StickerData[MAX_CONSISTENT_FRAMES];
		init();
	}

	public void setUpSize(int width, int height){
		this.width = width;
		this.height = height;	
		matConverter = new MatConverter(width, height);
		freeSpace = new FreeSpaceTrial(width, height);
	}

	private void init() {
		frameDataIndex = 0;
		for (int i = 0; i < MAX_CONSISTENT_FRAMES; i++){
			frameData[i] = new StickerData();
		}
		latestAccurateStickerData = new StickerData();
	}

	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}

	public int getActualValue(){
		return latestAccurateStickerData.getActualValue();
	}

	public double getAngle(){
		return latestAccurateStickerData.getAngleFromNorth();
	}

	public Point getCenterPoint(){
		return latestAccurateStickerData.getCenterPoint();
	}

	public Mat getStickerData(byte[] data) {
		long time = System.currentTimeMillis();
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);

		Mat retMat = new FreeSpaceTrial(width, height).freeSpaceDetection(rgbMat);

		System.out.println("TimeLeftX: " + (System.currentTimeMillis() - time));

		return retMat;
	}


	public Mat getMarkerData(byte[] data) {
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);
		Mat grayMat = matConverter.convertRgbToGray(rgbMat);
		Imgproc.GaussianBlur(grayMat, grayMat, new Size(11, 11), 0, 0);
		Imgproc.Canny(grayMat, grayMat, 10, 100);

		Point centerPoint = circleDetection.getCirclePoints(grayMat);

		if (centerPoint == null){
			return grayMat;
		}

		double angle = angleDetection.getAngle(centerPoint, grayMat, rgbMat);

		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		Imgproc.GaussianBlur(rgbMat, rgbMat, new Size(9, 9), 0, 0);
		Imgproc.GaussianBlur(hsvMat, hsvMat, new Size(7, 7), 0, 0);
		StickerData stickerData = angleCorrection.adjustFrameAngle(centerPoint, hsvMat, rgbMat, grayMat, angle);

		// drawCircleCenters(rgbMat, centerPoints); //Comment this later
		
		System.out.println("DATAA: " + stickerData);
		if (stickerData != null && stickerData.validDataValues()){
			frameData[frameDataIndex] = stickerData;
			frameDataIndex = (frameDataIndex + 1) % MAX_CONSISTENT_FRAMES;
			if (checkIfFrameDataConsitent()){
				System.out.println("DATAC: " + latestAccurateStickerData);
			}
		}	
		return rgbMat;
//		return grayMat;
	}


	public Pair<Point, Point> getFreeSpaceDirection(){		
		return directionLine; 
	}
		
	public void updateFreeSpaceDirection(byte[] data){
		long time = System.currentTimeMillis();
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);

		directionLine = freeSpace.detectFreeSpace(rgbMat);
		
		System.out.println("FreeSpaceTimeLeft: " + (System.currentTimeMillis() - time));
	}
	
	public void getStickerDataString(byte[] data) {
		//		long time = System.currentTimeMillis();
		//		System.out.println("TimeLeft: " + (System.currentTimeMillis() - time));

		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);
		Point centerPoint = circleDetection.getCirclePoints(rgbMat);

		if (centerPoint == null){
			return;
		}

		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		StickerData stickerData = angleCorrection.adjustFrameAngle(centerPoint, hsvMat, rgbMat);
		System.out.println("DATAA: " + stickerData);
		if (stickerData != null && stickerData.validDataValues()){
			frameData[frameDataIndex] = stickerData;
			frameDataIndex = (frameDataIndex + 1) % MAX_CONSISTENT_FRAMES;
			System.out.println("DATAB: " + stickerData);
			if (checkIfFrameDataConsitent()){
				System.out.println("DATAC: " + latestAccurateStickerData);
			}
		}
	}

	private void drawCircleCenters(Mat mat, List<Point> centerPoints) {
		Scalar color = new Scalar(0,255, 0);
		for (Point cp : centerPoints){
			Core.circle(mat, cp, 1, color, 1);
		}
	}

	private boolean checkIfFrameDataConsitent(){
		StickerData stickerData = frameData[0]; 

		for (int i = 1; i < MAX_CONSISTENT_FRAMES; i++){
			if (stickerData != null && frameData[i] != null && !stickerData.equalDataValues(frameData[i])){
				return false;
			}
		}

		latestAccurateStickerData = stickerData;
		return true;
	}

}
