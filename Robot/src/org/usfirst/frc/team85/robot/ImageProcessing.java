package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	private NetworkTable _table;
	
	private static boolean contourFound;
	
	private static double area, height, width, centerX, centerY, aspectRatio;

	private static final int IMGXOFFSET = 0, IMGXTOL = 5;
	private static final int IMGYOFFSET = 0, IMGYTOL = 10;
	
	private static final double TARGETASPECTRATIO = 2.0; //	width/height
	
	private static final double VISIONCAPTURETIMEOUT = 0.10;
	
	private static Timer _visionUpdateTimer;
	
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
	
	public void process() {
		
    	try {
    		_table = NetworkTable.getTable("GRIP/contoursReport");
			double[] areaArray = _table.getNumberArray("area", new double[] {0});
			double[] heightArray = _table.getNumberArray("height", new double[] {1});	//avoid /0
			double[] widthArray = _table.getNumberArray("width", new double[] {0});
			double[] centerXArray = _table.getNumberArray("centerX", new double[] {160});
			double[] centerYArray = _table.getNumberArray("centerY", new double[] {120});
			
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
	
	private static double toX() {
		return ((isVisionGone()) ? 160 : centerX) - 160;
	}
	
	private static double toY() {
		return ((isVisionGone()) ? 120 : centerY) - 120;
	}
	
	public static double xAxisChange() {
		return (Math.abs(toX()-IMGXOFFSET) < IMGXTOL) ? 0 : toX();
	}
	
	public static double yAxisChange() {
		return (Math.abs(toX()-IMGYOFFSET) < IMGYTOL) ? 0 : toY();
	}
	
}