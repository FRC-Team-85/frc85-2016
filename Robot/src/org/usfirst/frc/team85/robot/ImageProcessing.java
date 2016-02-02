package org.usfirst.frc.team85.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ImageProcessing {
	
	private NetworkTable _table;
	private CameraServer _server;
	private SmartDashboard _dashboard;
	
	private boolean contourFound;
	private int fov = 180;
	int pixelToAngle = fov/320; //each pixel equals X degrees
	
	private double area, height, width, centerX, centerY;
	
	public ImageProcessing(NetworkTable table, CameraServer server, SmartDashboard dashboard) {
		_table = table;
		_server = server;
		_dashboard = dashboard;
	}
	
	public void process() {
    	
    	try {
			double area = _table.getNumber("area", 0);
			contourFound = (area>0) ? true : false;
			centerX = _table.getNumber("centerX", 160) - 160;
			centerY = _table.getNumber("centerX", 120) - 120;
			if (area>0) {
				System.out.println("Area: " + area);
			} else {
				System.out.println("No Contour Found");
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	/*
	method that returns targeting angle change for drive
	 */
	public void center() {
		if (contourFound /*&& button pressed*/) {
			if (centerX < 10) {
				//move robot (to the right?) until centerX is less than 10  
			} else if (centerX > -10) {
				//move robot (to the left?) until centerX is more than -10
			}
			
			if (centerX > -10 && centerX < 10 /*and if the ball is in possession*/) {
				//Shoot the ball
			}
		}
	}
}

