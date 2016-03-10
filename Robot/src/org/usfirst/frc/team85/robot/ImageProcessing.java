package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	private NetworkTable _table;
	
	private static boolean contourFound;
	
	private static double area, height, width, centerX, centerY, aspectRatio;

	private static int IMGXOFFSET = 0, IMGXTOL = 10;
	private static int IMGYOFFSET = 0, IMGYTOL = 20;
	
	private static final double TARGETASPECTRATIO = 2.0; //	width/height
	
	private static final double VISIONCAPTURETIMEOUT = 0.10;
	
	private static Timer _visionUpdateTimer;
	
    public void initSafeCoding(){
    	SmartDashboard.putNumber("ZZZ vision xOff", IMGXOFFSET);
    	SmartDashboard.putNumber("ZZZ vision xTol", IMGXTOL);
    	SmartDashboard.putNumber("ZZZ vision yOff", IMGYOFFSET);
    	SmartDashboard.putNumber("ZZZ vision yTol", IMGYTOL);
    }
    
    public void muchSafeCoding(){
    	//Adjust with FX java on programming computer, along side Driver Station
    	IMGXOFFSET = (int) SmartDashboard.getNumber("ZZZ vision xOff", IMGXOFFSET);
    	IMGXTOL = (int) SmartDashboard.getNumber("ZZZ vision xTol", IMGXTOL);
    	IMGYOFFSET = (int) SmartDashboard.getNumber("ZZZ vision yOff", IMGYOFFSET);
    	IMGYTOL = (int) SmartDashboard.getNumber("ZZZ vision yTol", IMGYTOL);
    }
    
	
	public ImageProcessing() {		
		
		//Setup NetworkTables
        try {
        	NetworkTable.setClientMode();
        	NetworkTable.setIPAddress("roborio-85-frc.local");
        } catch(Exception ex) {
        	System.out.println(ex.toString());
        }
        
        _visionUpdateTimer = new Timer();
        _visionUpdateTimer.start();
	}
	
	double[] areaArray = {0}, heightArray = {1}, widthArray = {1}, centerXArray = {160}, centerYArray = {120};
	
	public void process() {
		
    	try {
    		_table = NetworkTable.getTable("GRIP/contoursReport");
			areaArray = _table.getNumberArray("area", areaArray);
			heightArray = _table.getNumberArray("height", heightArray);	//avoid /0
			widthArray = _table.getNumberArray("width", widthArray);
			centerXArray = _table.getNumberArray("centerX", centerXArray);
			centerYArray = _table.getNumberArray("centerY", centerYArray);
			
			double[] aspectRatioArray = new double[areaArray.length];
			int bestIndex = 0;
			double bestAspectRatio = 999;
			
			for (int i = 0; i < areaArray.length; i++) {
				aspectRatioArray[i] = widthArray[i]/heightArray[i];
				if (Math.abs((aspectRatioArray[i]-TARGETASPECTRATIO)/TARGETASPECTRATIO) 
						< Math.abs((bestAspectRatio-TARGETASPECTRATIO)/TARGETASPECTRATIO)) {
					bestIndex = i;
					bestAspectRatio = aspectRatioArray[i];
				}
			}

			if (areaArray[bestIndex] > 0) {
				_visionUpdateTimer.reset();
				area = areaArray[bestIndex];
				height = heightArray[bestIndex];
				width = widthArray[bestIndex];
				centerX = centerXArray[bestIndex];
				centerY = centerYArray[bestIndex];
				aspectRatio = aspectRatioArray[bestIndex];
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
//			area = SmartDashboard.getNumber("GRIP/contoursReport/area", 0);
		contourFound = (area>0) ? true : false;
//			centerX = SmartDashboard.getNumber("GRIP/contoursReport/centerX", 160) - 160;
//			centerY = SmartDashboard.getNumber("GRIP/contoursReport/centerY", 120) - 120;
	    SmartDashboard.putBoolean("Vision/Target Found", contourFound);
	    SmartDashboard.putNumber("Vision/Area", area);
	    SmartDashboard.putNumber("Vision/Height", height);
	    SmartDashboard.putNumber("Vision/Width", width);
	    SmartDashboard.putNumber("Vision/Center X", centerX);
	    SmartDashboard.putNumber("Vision/Center Y", centerY);
	    SmartDashboard.putNumber("Vision/AspectRatio", aspectRatio);
	}
	
	public static boolean isVisionGone() {
		return (_visionUpdateTimer.get() > VISIONCAPTURETIMEOUT);
	}
	
	private static double relX() {
		return ((isVisionGone()) ? 160 : centerX) - 160;
	}
	
	private static double relY() {
		return ((isVisionGone()) ? 120 : centerY) - 120;
	}
	
	public static double xPixelsToTarget() {
		return IMGXOFFSET - relX();
	}
	
	public static double yPixelsToTarget() {
		return IMGYOFFSET - relY();
	}
	
	public static boolean withinXTolerance() {
		return Math.abs(xPixelsToTarget()) < IMGXTOL;
	}
	
	public static boolean withinYTolerance() {
		return Math.abs(yPixelsToTarget()) < IMGYTOL;
	}
	
}
