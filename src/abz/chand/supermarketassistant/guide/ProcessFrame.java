package abz.chand.supermarketassistant.guide;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import abz.chand.supermarketassistant.guide.dataholder.StickerData;
import abz.chand.supermarketassistant.guide.frameprocessing.AngleCorrection;
import abz.chand.supermarketassistant.guide.frameprocessing.CircleDetection;
import abz.chand.supermarketassistant.guide.frameprocessing.MatConverter;

public class ProcessFrame {
	
	private static final int MAX_CONSISTENT_FRAMES = 5;
	
	private int width;
	private int height;
	private MatConverter matConverter;
	private CircleDetection circleDetection;
	private AngleCorrection angleCorrection;
	private StickerData[] frameData;
	private StickerData latestAccurateStickerData;
	private int frameDataIndex;
	
	public ProcessFrame(){
		circleDetection = new CircleDetection();
		angleCorrection = new AngleCorrection();
		frameData = new StickerData[MAX_CONSISTENT_FRAMES];
		init();
	}
	
	public ProcessFrame(int width, int height){
		this.width = width;
		this.height = height;	
		matConverter = new MatConverter(width, height);
		circleDetection = new CircleDetection();
		angleCorrection = new AngleCorrection();
		frameData = new StickerData[MAX_CONSISTENT_FRAMES];
		init();
	}

	public void setUpSize(int width, int height){
		this.width = width;
		this.height = height;	
		matConverter = new MatConverter(width, height);
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

	public int getAngle(){
		return latestAccurateStickerData.getAngleFromNorth();
	}
	
	public Point getCenterPoint(){
		return latestAccurateStickerData.getCenterPoint();
	}
	
	public Mat getStickerData(byte[] data) {
		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);
		List<Point> centerPoints = circleDetection.getCirclePoints(rgbMat);
		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		angleCorrection.adjustFrameAngle(centerPoints, hsvMat, rgbMat);	
		drawCircleCenters(rgbMat, centerPoints); //Comment this later
		return rgbMat;	
	}
	
	public void getStickerDataString(byte[] data) {
//		long time = System.currentTimeMillis();
//		System.out.println("TimeLeft: " + (System.currentTimeMillis() - time));

		Mat mat = matConverter.convertDataToMat(data);
		Mat rgbMat = matConverter.convertYuvToRgb(mat);
		List<Point> centerPoints = circleDetection.getCirclePoints(rgbMat);
		Mat hsvMat = matConverter.convertRgbToHsv(rgbMat);
		StickerData stickerData = angleCorrection.adjustFrameAngle(centerPoints, hsvMat, rgbMat);
		if (stickerData != null){
			frameData[frameDataIndex] = stickerData;
			frameDataIndex = (frameDataIndex + 1) % MAX_CONSISTENT_FRAMES;
			if (checkIfFrameDataConsitent()){
				System.out.println("DATAB: " + latestAccurateStickerData);
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
